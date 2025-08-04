package com.smarthome.user;

import com.smarthome.exception.AuthenticationException;

public class User {
    private String username;
    private String password;
    private String role; // "ADMIN" or "USER"
    
    // Overloaded constructors
    public User(String username, String password) {
        this(username, password, "USER");
    }
    
    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
    
    // Authentication method
    public boolean authenticate(String password) throws AuthenticationException {
        if (!this.password.equals(password)) {
            throw new AuthenticationException("Invalid password for user: " + username);
        }
        return true;
    }
    
    // Permission check
    public boolean hasPermission(String action) {
        if (role.equals("ADMIN")) {
            return true; // Admins can do everything
        } else {
            // Regular users can't perform certain admin actions
            return !action.startsWith("ADMIN_");
        }
    }
    
    // Getters
    public String getUsername() { return username; }
    public String getRole() { return role; }
}
