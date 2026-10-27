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
    public <T> T loadRow(T obj) throws SQLException, IllegalAccessException {
        // Return null if no row was found
        return null;
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


