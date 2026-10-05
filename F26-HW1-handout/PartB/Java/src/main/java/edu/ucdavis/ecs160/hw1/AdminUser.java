package edu.ucdavis.ecs160.hw1;

public class AdminUser implements User {
    private String name;
    private String email;

    public AdminUser(String name, String email) {
        this.name = name;
        this.email = email;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }
}
