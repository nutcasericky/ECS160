 package com.hw1.persistence;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;

public class SQLiteDB {
    private Connection connection;
    private SQLiteDBHelper dbHelper;
    
    public SQLiteDB(String dbPath) throws SQLException {
        this.connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
        this.dbHelper = new SQLiteDBHelper(this.connection);
    }
    
    public void createTable(Class<?> clazz) throws SQLException {
        
        StringBuilder sql = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
        sql.append(clazz.getSimpleName()).append(" (");
        
        Field[] fields = clazz.getDeclaredFields();
        List<Field> persistableFields = new ArrayList<>();
        
        // Only include fields marked with @Persistable annotation
        for (Field field : fields) {
            if (field.isAnnotationPresent(Persistable.class)) {
                persistableFields.add(field);
            }
        }
        
        for (int i = 0; i < persistableFields.size(); i++) {
            Field field = persistableFields.get(i);
            String fieldName = dbHelper.camelToSnakeCase(field.getName());
            String sqlType = dbHelper.getSQLType(field.getType());
            
            sql.append(fieldName).append(" ").append(sqlType);
            
            // Check if field is marked as PrimaryKey
            if (field.isAnnotationPresent(PrimaryKey.class)) {
                sql.append(" PRIMARY KEY");
            }
            
            if (i < persistableFields.size() - 1) {
                sql.append(", ");
            }
        }
        
        sql.append(")");
        
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql.toString());
        }
    }
    

    public void droptTable(Class<?> clazz) throws SQLException {
        String sql = "DROP TABLE IF EXISTS " + clazz.getSimpleName();
        
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }   
    
    /**
     * Insert the given object into the database using reflection.
     * Only fields annotated with @Persistable should be stored.
     * Check the handout for more details.
     */
    public void insertRow(Object obj) throws SQLException, IllegalAccessException {
        Class<?> clazz = obj.getClass();
        List<Field> fields = new ArrayList<>();

        //add @Persistable fields into the list
        for(Field field: clazz.getDeclaredFields()){
            if(field.isAnnotationPresent(Persistable.class)){
                field.setAccessible(true);
                fields.add(field);
            }
        }

        //build columns
        StringBuilder columns = new StringBuilder();
        StringBuilder placeholders = new StringBuilder();

        for(int i = 0;i < fields.size();i++){
            if(i > 0){
                columns.append(", ");
                placeholders.append(", ");
            }

            columns.append(dbHelper.camelToSnakeCase(fields.get(i).getName()));
            placeholders.append("?");
        }

        String sql = "INSERT INTO " + clazz.getSimpleName() + " (" + columns + 
        ") VALUES (" + placeholders + ")";

        //bind values
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
        for (int i = 0; i < fields.size(); i++) {
            Field field = fields.get(i);
            int position = i + 1;

            if (field.getType() == String.class) {
                pstmt.setString(position, (String) field.get(obj));
            } else if (field.getType() == int.class) {
                pstmt.setInt(position, field.getInt(obj));
            } else if (field.getType() == byte[].class) {
                pstmt.setBytes(position, (byte[]) field.get(obj));
            }
        }

        pstmt.executeUpdate();
    }


    }

    /**
     * Load a row from the database using reflection.
     * The object passed in should have its primary key field populated.
     * This method will load the other persistable fields from the database
     * and return a proxy object that lazy-loads fields annotated with @RemoteLazyLoad.
     * Check the handout for more details. 
     * 
     */
    @SuppressWarnings("unchecked")
    public <T> T loadRow(T obj) throws SQLException, IllegalAccessException {
        Class<?> clazz = obj.getClass();
        List<Field> persistableFields = new ArrayList<>();
        List<Field> lazyFields = new ArrayList<>();
        Field primaryKey = null;

        // Find the fields to load and the primary key.
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Persistable.class)) {
                field.setAccessible(true);
                persistableFields.add(field);

                if (field.isAnnotationPresent(PrimaryKey.class)) {
                    primaryKey = field;
                }

                if (field.isAnnotationPresent(RemoteLazyLoad.class)) {
                    lazyFields.add(field);
                }
            }
        }

        if (primaryKey == null) {
            throw new SQLException("No primary key field found");
        }

        String keyColumn = dbHelper.camelToSnakeCase(primaryKey.getName());
        String sql = "SELECT * FROM " + clazz.getSimpleName()
                + " WHERE " + keyColumn + " = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            // Bind the primary key's value.
            if (primaryKey.getType() == String.class) {
                pstmt.setString(1, (String) primaryKey.get(obj));
            } else if (primaryKey.getType() == int.class) {
                pstmt.setInt(1, primaryKey.getInt(obj));
            } else if (primaryKey.getType() == byte[].class) {
                pstmt.setBytes(1, (byte[]) primaryKey.get(obj));
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                if (!rs.next()) {
                    System.out.println(
                            "No row found for primary key: " + primaryKey.get(obj));
                    System.exit(0);
                    return null;
                }

                T result = obj;

                if (!lazyFields.isEmpty()) {
                    ProxyFactory factory = new ProxyFactory();
                    factory.setSuperclass(clazz);

                    MethodHandler handler = (self, method, proceed, args) -> {
                        for (Field field : lazyFields) {
                            String fieldName = field.getName();
                            String getterName = "get"
                                    + Character.toUpperCase(fieldName.charAt(0))
                                    + fieldName.substring(1);

                            if (method.getName().equals(getterName)
                                    && method.getParameterCount() == 0) {
                                byte[] value = (byte[]) field.get(self);

                                if (value != null) {
                                    String possibleUrl = new String(
                                            value,
                                            java.nio.charset.StandardCharsets.UTF_8);

                                    if (possibleUrl.startsWith("http://")
                                            || possibleUrl.startsWith("https://")) {
                                        byte[] content =
                                                dbHelper.fetchContentFromUrl(possibleUrl);

                                        // Replace the URL with downloaded bytes.
                                        field.set(self, content);
                                    }
                                }

                                break;
                            }
                        }

                        // Invoke the original implementation of the method.
                        return proceed.invoke(self, args);
                    };

                    try {
                        result = (T) factory.create(
                                new Class<?>[0],
                                new Object[0],
                                handler);
                    } catch (ReflectiveOperationException e) {
                        throw new SQLException(
                                "Could not create lazy-loading proxy", e);
                    }
                }

                // Populate the supplied object.
                // When using a proxy, populate its fields as well.
                for (Field field : persistableFields) {
                    String column = dbHelper.camelToSnakeCase(field.getName());
                    Object value;

                    if (field.getType() == String.class) {
                        value = rs.getString(column);
                    } else if (field.getType() == int.class) {
                        value = rs.getInt(column);
                    } else {
                        value = rs.getBytes(column);
                    }

                    field.set(obj, value);

                    if (result != obj) {
                        field.set(result, value);
                    }
                }

                return result;
            }
        }
    }
    
    
    public Connection getConnection() {
        return connection;
    }
    
    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}


