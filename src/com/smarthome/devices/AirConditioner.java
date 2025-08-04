package com.smarthome.devices;

import com.smarthome.automation.Schedulable;
import com.smarthome.exception.AuthenticationException;
import com.smarthome.exception.DeviceOperationException;

public class AirConditioner extends Device implements Schedulable {
    private Integer temperature;
    private String mode;
    
    public AirConditioner(String deviceId, String name) {
        super(deviceId, name);
        this.temperature = 24;
        this.mode = "COOL";
    }
    
    public AirConditioner(String deviceId, String name, String location) {
        super(deviceId, name, location);
        this.temperature = 24;
        this.mode = "COOL";
    }
    
    @Override
    public String getDeviceType() {
        return "AirConditioner";
    }
    
    @Override
    public void control(String command, String... parameters) {
     try {
    if (command.equals("ON")) {
        turnOn();
    } else if (command.equals("OFF")) {
        turnOff();
    } else if (command.equals("TEMP")) {
        if (parameters.length > 0) {
            setTemperature(Integer.parseInt(parameters[0]));
        }
    } else if (command.equals("MODE")) {
        if (parameters.length > 0) {
            setMode(parameters[0]);
        }
    }else System.out.println("Unknown command: " + command);

}
            
        catch (DeviceOperationException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Invalid parameter format");
        }
    }
    
    @Override
    public String[] getControlOptions() {
        return new String[]{"ON", "OFF", "TEMP", "MODE"};
    }
    
    @Override
    public void scheduleTask(String time, String action) {
        System.out.println("Scheduled " + action + " for " + name + " at " + time);
    }
    
    @Override
    public void cancelSchedule(String scheduleId) {
        System.out.println("Cancelled schedule " + scheduleId + " for " + name);
    }
    
    public void setTemperature(int temperature) {
        this.temperature = temperature;
        System.out.println(name + " temperature set to " + temperature);
    }
    
    public void setMode(String mode) {
        this.mode = mode;
        System.out.println(name + " mode set to " + mode);
    }
    
    public Integer getTemperature() {
        return temperature;
    }
    
    public String getMode() {
        return mode;
    }
}

