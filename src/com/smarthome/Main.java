package com.smarthome;

import java.util.Scanner;


import com.smarthome.automation.Automation;
import com.smarthome.core.SmartHomeSystem;
import com.smarthome.devices.*;
import com.smarthome.exception.*;

public class Main {
    private static SmartHomeSystem system;
    private static Scanner scanner;
    
    public static void main(String[] args) {
        // Initialize system
        system = new SmartHomeSystem("MySmartHome");
        scanner = new Scanner(System.in);
        
        // Initialize some devices
        try {
            system.addDevice(new Light("L1", "Living Room Light", "Living Room"));
            system.addDevice(new Light("L2", "Bedroom Light", "Bedroom"));
            system.addDevice(new AirConditioner("AC1", "Living Room AC", "Living Room"));
        } catch (SystemException e) {
            System.out.println("Error initializing devices: " + e.getMessage());
        }
        
        // Login
        System.out.println("Welcome to Smart Home Automation System");
        System.out.println("Please login to continue");
        
        boolean loggedIn = false;
        while (!loggedIn) {
            System.out.print("Username: ");
            String username = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();
            
            loggedIn = system.login(username, password);
            if (!loggedIn) {
                System.out.println("Login failed. Try again? (y/n)");
                String choice = scanner.nextLine();
                if (!choice.equalsIgnoreCase("y")) {
                    System.out.println("Exiting system.");
                    return;
                }
            }
        }
        
        // Main menu
        boolean running = true;
        while (running) {
            system.displaySystemStatus();
            
            System.out.println("Choose an option:");
            System.out.println("1. Control Devices");
            System.out.println("2. Create Automation Rule");
            System.out.println("3. Schedule Device Operation");
            System.out.println("4. Save System State");
            System.out.println("5. Load System State");
            System.out.println("6. Logout");
            System.out.println("7. Exit");
            
            System.out.print("Option: ");
            String option = scanner.nextLine();
            
            switch (option) {
                case "1":
                    controlDevicesMenu();
                    break;
                case "2":
                    createAutomationRuleMenu();
                    break;
                case "3":
                    scheduleDeviceMenu();
                    break;
                case "4":
                    System.out.print("Enter filename: ");
                    String saveFile = scanner.nextLine();
                    system.saveSystemState(saveFile);
                    break;
                case "5":
                    System.out.print("Enter filename: ");
                    String loadFile = scanner.nextLine();
                    system.loadSystemState(loadFile);
                    break;
                case "6":
                    system.logout();
                    running = false;
                    break;
                case "7":
                    System.out.println("Exiting system. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
        
        scanner.close();
    }
    
    private static void controlDevicesMenu() {
        system.displaySystemStatus();
        
        System.out.print("Enter device ID to control: ");
        String deviceId = scanner.nextLine();
        
        Device device = system.getDevice(deviceId);
        if (device == null) {
            System.out.println("Device not found: " + deviceId);
            return;
        }
        
        System.out.println("Device: " + device.getName() + " (" + device.getDeviceType() + ")");
        System.out.println("Current status: " + (device.isOn() ? "ON" : "OFF"));
        System.out.println("Control options: ");
        
        String[] options = device.getControlOptions();
        for (int i = 0; i < options.length; i++) {
            System.out.println((i+1) + ". " + options[i]);
        }
        
        System.out.print("Enter command: ");
        String command = scanner.nextLine().toUpperCase();
        
        if (command.equals("ON") || command.equals("OFF")) {
            system.controlDevices(command, device);
        } else {
            System.out.print("Enter parameter: ");
            String parameter = scanner.nextLine();
            system.controlDevices(command, parameter, device);
        }
    }
    
    private static void createAutomationRuleMenu() {
        System.out.println("\n--- Create Automation Rule ---");
        
        System.out.print("Enter rule name: ");
        String ruleName = scanner.nextLine();
        
        system.displaySystemStatus();
        
        System.out.print("Enter trigger device ID (or leave empty for time-based trigger): ");
        String triggerDeviceId = scanner.nextLine();
        
        Device triggerDevice = null;
        if (!triggerDeviceId.isEmpty()) {
            triggerDevice = system.getDevice(triggerDeviceId);
            if (triggerDevice == null) {
                System.out.println("Trigger device not found: " + triggerDeviceId);
                return;
            }
        }
        
        System.out.print("Enter trigger condition: ");
        String triggerCondition = scanner.nextLine();
        
        System.out.print("Enter action to perform: ");
        String action = scanner.nextLine().toUpperCase();
        
        System.out.print("Enter target device IDs (comma-separated): ");
        String targetDeviceIds = scanner.nextLine();
        String[] ids = targetDeviceIds.split(",");
        
        Device[] targetDevices = new Device[ids.length];
        for (int i = 0; i < ids.length; i++) {
            targetDevices[i] = system.getDevice(ids[i].trim());
            if (targetDevices[i] == null) {
                System.out.println("Target device not found: " + ids[i].trim());
                return;
            }
        }
        
        Automation automation = system.getAutomation();
        try {
            if (triggerDevice != null) {
                automation.createRule(ruleName, triggerDevice, triggerCondition, action, targetDevices);
            } else {
                automation.createRule(ruleName, triggerCondition, action, targetDevices);
            }
            System.out.println("Automation rule created successfully.");
        } catch (AutomationException e) {
            System.out.println("Error creating automation rule: " + e.getMessage());
        }
    }
    
    private static void scheduleDeviceMenu() {
        System.out.println("\n--- Schedule Device Operation ---");
        
        system.displaySystemStatus();
        
        System.out.print("Enter device IDs (comma-separated): ");
        String deviceIds = scanner.nextLine();
        String[] ids = deviceIds.split(",");
        
        Device[] devices = new Device[ids.length];
        for (int i = 0; i < ids.length; i++) {
            devices[i] = system.getDevice(ids[i].trim());
            if (devices[i] == null) {
                System.out.println("Device not found: " + ids[i].trim());
                return;
            }
        }
        
        System.out.print("Enter time (HH:MM): ");
        String time = scanner.nextLine();
        
        System.out.print("Enter action (default: ON): ");
        String action = scanner.nextLine().toUpperCase();
        
        if (action.isEmpty()) {
            system.getAutomation().scheduleTask(time, devices);
        } else {
           system.getAutomation().scheduleTask(time, action, devices);
        }
        
        System.out.println("Schedule created successfully.");
    }
}

