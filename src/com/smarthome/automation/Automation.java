package com.smarthome.automation;

import com.smarthome.devices.Device;
import com.smarthome.exception.AuthenticationException;
import com.smarthome.exception.AutomationException;

public class Automation {
    // Nested class for rules
    public class Rule {
        private String ruleName;
        private Device triggerDevice;
        private String triggerCondition;
        private Device[] targetDevices;
        private String action;
        
        public Rule(String ruleName, Device triggerDevice, String triggerCondition, 
                   String action, Device... targetDevices) {
            this.ruleName = ruleName;
            this.triggerDevice = triggerDevice;
            this.triggerCondition = triggerCondition;
            this.action = action;
            this.targetDevices = targetDevices;
        }
        
        public void execute() {
            for (Device device : targetDevices) {
                device.control(action);
            }
        }
        
        // Getters
        public String getRuleName() { return ruleName; }
        public Device getTriggerDevice() { return triggerDevice; }
        public String getTriggerCondition() { return triggerCondition; }
    }
    
    private Rule[] rules;
    private int ruleCount;
    
    public Automation() {
        this.rules = new Rule[100]; // Fixed size array instead of ArrayList
        this.ruleCount = 0;
    }
    
    // Vararg overloaded methods for creating rules
    public void createRule(String ruleName, Device triggerDevice, String triggerCondition, 
                          String action, Device... targetDevices) throws AutomationException {
        if (ruleCount >= rules.length) {
            throw new AutomationException("Maximum number of rules reached");
        }
        
        Rule rule = new Rule(ruleName, triggerDevice, triggerCondition, action, targetDevices);
        rules[ruleCount++] = rule;
        System.out.println("Rule created: " + ruleName);
    }
    
    public void createRule(String ruleName, String triggerCondition, 
                          String action, Device... targetDevices) throws AutomationException {
        createRule(ruleName, null, triggerCondition, action, targetDevices);
    }
    
    // Method to simulate a trigger event
    public void triggerEvent(Device device, String condition) {
        for (int i = 0; i < ruleCount; i++) {
            Rule rule = rules[i];
            if (rule.getTriggerDevice() == device && 
                rule.getTriggerCondition().equals(condition)) {
                System.out.println("Executing rule: " + rule.getRuleName());
                rule.execute();
            }
        }
    }
    
    // Vararg overloaded methods for scheduling
    public void scheduleTask(String time, String action, Device... devices) {
        System.out.println("Scheduled " + action + " at " + time + " for " + devices.length + " devices");
        for (Device device : devices) {
            if (device instanceof Schedulable) {
                ((Schedulable) device).scheduleTask(time, action);
            } else {
                System.out.println(device.getName() + " is not schedulable");
            }
        }
    }
    
    public void scheduleTask(String time, Device... devices) {
        scheduleTask(time, "ON", devices);
    }
}

