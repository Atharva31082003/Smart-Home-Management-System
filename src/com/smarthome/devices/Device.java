// Abstract Device class
//import java.io.*;
package com.smarthome.devices;

import com.smarthome.core.Controllable;
//import com.smarthome.exceptions.DeviceOperationException;
import com.smarthome.exception.DeviceOperationException;

public abstract class Device implements Controllable {
    // Properties with wrapper classes
    public String deviceId;
    public String name;
    public String location;
    public Boolean isOn;
    public Double energyConsumption;
    
    // Overloaded constructors
    public Device(String deviceId, String name) {
        this(deviceId, name, "Unknown");
    }
    
    public Device(String deviceId, String name, String location) {
        this.deviceId = deviceId;
        this.name = name;
        this.location = location;
        this.isOn = false;
        this.energyConsumption = 0.0;
    }
    
    // Abstract methods
    public abstract String getDeviceType();
    
    // Common methods
    public void turnOn() throws DeviceOperationException {
        if (isOn) {
            throw new DeviceOperationException("Device is already ON");
        }
        isOn = true;
        System.out.println(name + " turned ON");
    }
    
    public void turnOff() throws DeviceOperationException {
        if (isOn==false) {
            throw new DeviceOperationException("Device is already OFF");
        }
        isOn = false;
        System.out.println(name + " turned OFF");
    }
    
    // Getters and setters
    public String getDeviceId() { return deviceId; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public Boolean isOn() { return isOn; }
    public Double getEnergyConsumption() { return energyConsumption; }
    
    // Method to update energy consumption
    public void updateEnergyConsumption() {
        if (isOn) {
            energyConsumption += 0.1; // Simplified calculation
        }
    }
}


