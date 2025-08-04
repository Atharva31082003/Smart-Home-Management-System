// Interface for schedulable devices

package com.smarthome.automation;

public interface Schedulable {
    void scheduleTask(String time, String action);
    void cancelSchedule(String scheduleId);
}
