package edu.ucdavis.ecs160.hw1;

public class Main {
    public static void main(String[] args) {

        User user = new LoggingUser(new AdminUser("Alice", "alice@example.com")); // Initialize user here!
        user.setEmail("alice@ucdavis.edu");
        user.getName();
        user.getEmail();

    }
}
