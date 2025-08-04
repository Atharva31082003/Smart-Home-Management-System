	package com.smarthome.core;

import java.io.*;
import java.util.Scanner;

import com.smarthome.automation.Automation;
import com.smarthome.devices.*;
import com.smarthome.exception.*;
import com.smarthome.user.User;

public class SmartHomeSystem {
    // Static nested class for system configuration
    public static class SystemConfig {
        private String systemName;
        private String configVersion;
        private boolean debugMode;
        
        public SystemConfig(String systemName, String configVersion) {
            this.systemName = systemName;
            this.configVersion = configVersion;
            this.debugMode = false;
        }
        
        public void setDebugMode(boolean debugMode) {
            this.debugMode = debugMode;
        }
        
        public boolean isDebugMode() {
            return debugMode;
        }
        
        public String getSystemInfo() {
            return systemName + " v" + configVersion;
        }
    }
    
    // System components
    private SystemConfig config;
    private Device[] devices;
    private int deviceCount;
    private User[] users;
    private int userCount;
    private User currentUser;
    private Automation automation;
    
    // Overloaded constructors
    public SmartHomeSystem() {
        this("Smart Home");
    }
    
    public SmartHomeSystem(String systemName) {
        this.config = new SystemConfig(systemName, "1.0");
        this.devices = new Device[100]; // Fixed size array instead of ArrayList
        this.deviceCount = 0;
        this.users = new User[50]; // Fixed size array instead of ArrayList
        this.userCount = 0;
        this.automation = new Automation();
        
        // Add default admin user
        try {
            addUser(new User("admin", "admin123", "ADMIN"));
        } catch (SystemException e) {
            System.out.println("Error initializing system: " + e.getMessage());
        }
    }
    
    // User management
    public void addUser(User user) throws SystemException {
        if (userCount >= users.length) {
            throw new SystemException("Maximum number of users reached");
        }
        
        for (int i = 0; i < userCount; i++) {
            if (users[i].getUsername().equals(user.getUsername())) {
                throw new SystemException("User already exists: " + user.getUsername());
            }
        }
        
        users[userCount++] = user;
        System.out.println("User added: " + user.getUsername());
    }
    
    public boolean login(String username, String password) {
        for (int i = 0; i < userCount; i++) {
            User user = users[i];
            if (user.getUsername().equals(username)) {
                try {
                    if (user.authenticate(password)) {
                        currentUser = user;
                        System.out.println("Login successful: " + username);
                        return true;
                    }
                } catch (AuthenticationException e) {
                    System.out.println("Authentication failed: " + e.getMessage());
                    return false;
                }
            }
        }
        System.out.println("User not found: " + username);
        return false;
    }
    
    public void logout() {
        if (currentUser != null) {
            System.out.println("Logged out: " + currentUser.getUsername());
            currentUser = null;
        }
    }
    
    // Device management - Overloaded methods
    public void addDevice(Device device) throws SystemException {
        if (currentUser == null || !currentUser.hasPermission("ADMIN_ADD_DEVICE")) {
            throw new PermissionDeniedException("Only admins can add devices");
        }
        
        if (deviceCount >= devices.length) {
            throw new SystemException("Maximum number of devices reached");
        }
        
        devices[deviceCount++] = device;
        System.out.println("Device added: " + device.getName());
    }
    
    public void addDevice(Device device, String location) throws SystemException {
        device = changeDeviceLocation(device, location);
        addDevice(device);
    }
    
    private Device changeDeviceLocation(Device device, String location) {
        // This method simulates changing the device location
        // In a real system, you might need to create a new device or modify existing one
        device.location = location;
        return device;
    }
    
    // Vararg overloaded methods for controlling multiple devices
    public void controlDevices(String command, Device... devicesToControl) {
        if (currentUser == null) {
            System.out.println("You must be logged in to control devices");
            return;
        }
        
        for (Device device : devicesToControl) {
            device.control(command);
        }
    }
    
    public void controlDevices(String command, String parameter, Device... devicesToControl) {
        if (currentUser == null) {
            System.out.println("You must be logged in to control devices");
            return;
        }
        
        for (Device device : devicesToControl) {
            device.control(command, parameter);
        }
    }
    
    // Methods to get devices
    public Device getDevice(String deviceId) {
        for (int i = 0; i < deviceCount; i++) {
            if (devices[i].getDeviceId().equals(deviceId)) {
                return devices[i];
            }
        }
        return null;
    }
    
    public Device[] getDevicesByType(String type) {
        // Count matching devices first
        int count = 0;
        for (int i = 0; i < deviceCount; i++) {
            if (devices[i].getDeviceType().equals(type)) {
                count++;
            }
        }
        
        // Create array of exact size
        Device[] result = new Device[count];
        int index = 0;
        for (int i = 0; i < deviceCount; i++) {
            if (devices[i].getDeviceType().equals(type)) {
                result[index++] = devices[i];
            }
        }
        
        return result;
    }
    
    // Method to get automation system
    public Automation getAutomation() {
        return automation;
    }
    
    // File I/O methods for persistence
    public void saveSystemState(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            // Write system info
            writer.println("SYSTEM:" + config.getSystemInfo());
            
            // Write devices
            writer.println("DEVICES:" + deviceCount);
            for (int i = 0; i < deviceCount; i++) {
                Device device = devices[i];
                writer.println(device.getDeviceType() + "," + 
                              device.getDeviceId() + "," + 
                              device.getName() + "," + 
                              device.getLocation() + "," + 
                              device.isOn());
            }
            
            System.out.println("System state saved to " + filename);
        } catch (IOException e) {
            System.out.println("Error saving system state: " + e.getMessage());
        }
    }
    
    public void loadSystemState(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            
            // Read system info
            line = reader.readLine();
            if (line != null && line.startsWith("SYSTEM:")) {
                // Parse system info if needed
            }
            
            // Read devices
            line = reader.readLine();
            if (line != null && line.startsWith("DEVICES:")) {
                int count = Integer.parseInt(line.substring(8));
                
                // Clear existing devices
                deviceCount = 0;
                
                // Read each device
                for (int i = 0; i < count; i++) {
                    line = reader.readLine();
                    if (line != null) {
                        String[] parts = line.split(",");
                        if (parts.length >= 5) {
                            String type = parts[0];
                            String id = parts[1];
                            String name = parts[2];
                            String location = parts[3];
                            boolean isOn = Boolean.parseBoolean(parts[4]);
                            
                            // Create device based on type
                            Device device = null;
                            switch (type) {
                                case "Light":
                                    device = new Light(id, name, location);
                                    break;
                                case "AirConditioner":
                                    device = new AirConditioner(id, name, location);
                                    break;
                                // Add other device types as needed
                            }
                            
                            if (device != null) {
                                try {
                                    // Set device state
                                    if (isOn) {
                                        device.turnOn();
                                    }
                                    
                                    // Add device to system
                                    devices[deviceCount++] = device;
                                } catch (DeviceOperationException e) {
                                    System.out.println("Error restoring device state: " + e.getMessage());
                                }
                            }
                        }
                    }
                }
            }
            
            System.out.println("System state loaded from " + filename);
        } catch (IOException e) {
            System.out.println("Error loading system state: " + e.getMessage());
        }
    }
    
    // Method to display system status
    public void displaySystemStatus() {
        System.out.println("\n===== " + config.getSystemInfo() + " =====");
        System.out.println("Logged in user: " + (currentUser != null ? currentUser.getUsername() + " (" + currentUser.getRole() + ")" : "None"));
        System.out.println("Total devices: " + deviceCount);
        
        // Display devices
        System.out.println("\nDevices:");
        for (int i = 0; i < deviceCount; i++) {
            Device device = devices[i];
            System.out.println((i+1) + ". " + device.getName() + " (" + device.getDeviceType() + 
                              ") - Location: " + device.getLocation() + 
                              ", Status: " + (device.isOn() ? "ON" : "OFF") + 
                              ", Energy: " + device.getEnergyConsumption() + " kWh");
        }
        
        System.out.println("\n==============================\n");
    }
}

