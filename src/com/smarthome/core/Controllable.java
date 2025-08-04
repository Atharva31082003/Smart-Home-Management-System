
package com.smarthome.core;

// Interface definition for controllable devices
public interface Controllable {
    void control(String command, String... parameters);
    String[] getControlOptions();
}
