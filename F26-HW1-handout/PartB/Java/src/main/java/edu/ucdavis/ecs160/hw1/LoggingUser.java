package edu.ucdavis.ecs160.hw1;

import java.util.Objects;

public class LoggingUser implements User {
    private final User delegate;

    public LoggingUser(User delegate) {
        this.delegate = Objects.requireNonNull(delegate);
    }

    @Override
    public String getName() {
        System.out.println("[LOG] " + delegate.getClass().getSimpleName() + ".getName()");
        return delegate.getName();
    }

    @Override
    public String getEmail() {
        System.out.println("[LOG] " + delegate.getClass().getSimpleName() + ".getEmail()");
        return delegate.getEmail();
    }

    @Override
    public void setEmail(String email) {
        String argument = email == null ? "null" : "\"" + email
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r") + "\"";
        System.out.println("[LOG] " + delegate.getClass().getSimpleName()
                + ".setEmail(" + argument + ")");
        delegate.setEmail(email);
    }
}