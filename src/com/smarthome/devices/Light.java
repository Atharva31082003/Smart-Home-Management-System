package com.smarthome.devices;

import com.smarthome.automation.Schedulable;
import com.smarthome.exception.AuthenticationException;
import com.smarthome.exception.DeviceOperationException;

// Light device implementation
public class Light extends Device implements Schedulable {
    private Integer brightness;
    
    // Overloaded constructors
    public Light(String deviceId, String name) {
        super(deviceId, name);
        this.brightness = 100;
    }
    
    public Light(String deviceId, String name, String location) {
        super(deviceId, name, location);
        this.brightness = 100;
    }
    
    @Override
    public String getDeviceType() {
        return "Light";
    }
    
    // Implementing Controllable interface
    @Override
    public void control(String command, String... parameters) {
        try {
            switch (command) {
                case "ON":
                    turnOn();
                    break;
                case "OFF":
                    turnOff();
                    break;
                case "BRIGHTNESS":
                    if (parameters.length > 0) {
                        setBrightness(Integer.parseInt(parameters[0]));
                    }
                    break;
                default:
                    System.out.println("Unknown command: " + command);
            }
        } catch (DeviceOperationException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Invalid parameter format");
        }
    }
    
    @Override
    public String[] getControlOptions() {
        return new String[]{"ON", "OFF", "BRIGHTNESS"};
    }
    
    // Implementing Schedulable interface
    @Override
    public void scheduleTask(String time, String action) {
        System.out.println("Scheduled " + action + " for " + name + " at " + time);
    }
    
    @Override
    public void cancelSchedule(String scheduleId) {
        System.out.println("Cancelled schedule " + scheduleId + " for " + name);
    }
    
    // Light-specific methods
    public void setBrightness(int brightness) {
        this.brightness = brightness;
        System.out.println(name + " brightness set to " + brightness);
    }
    
    public Integer getBrightness() {
        return brightness;
    }
}


