# Smart Home Management System
This Java-based Smart Home Management System is a modular and object-oriented solution that simulates the control and automation of devices in a smart home environment. 
It supports user authentication, device management, scheduling, automation rules, and exception handling, making it an ideal project for demonstrating core OOP concepts.
#Technologies used 
-Object orientd programming
-Java
-Eclipse IDE
-Git and Github for version control

## Project structure
src/
└── com/
└── smarthome/
├── Main.java
├── user/
│ └── User.java
├── core/
│ ├── Controllable.java
│ └── SmartHomeSystem.java
├── automation/
│ ├── Automation.java
│ └── Schedulable.java
├── devices/
│ ├── Device.java
│ ├── Light.java
│ └── AirConditioner.java
└── exception/
├── SystemException.java
├── DeviceOperationException.java
├── AuthenticationException.java
├── PermissionDeniedException.java
└── AutomationException.java

###Key Features

##Device Management
- Abstract `Device` class defines a common structure for all devices.
- Devices implement `Controllable` and optionally `Schedulable` interfaces.
- `Light` and `AirConditioner` classes extend `Device` and support features like brightness and temperature control.

##User Authentication
- `User` class allows login/logout functionality with role-based permission checks (`ADMIN`, `USER`).
- `AuthenticationException` is thrown for failed login attempts.

##Smart Home Core
- `SmartHomeSystem` class manages:
  - System configuration
  - User sessions
  - Adding and controlling devices
  - File I/O for system persistence
  - Displaying system status
##Scheduling & Automation
- Devices implement `Schedulable` for time-based control.
- `Automation` class supports rule creation, event triggering, and device coordination.

##Custom Exception Handling
- Exception classes (`SystemException`, `DeviceOperationException`, etc.) provide robust error reporting and handling.

##Compile the project
   Open terminal in project directory and run:
   ```bash
   javac -d bin src/com/smarthome/**/*.java
java -cp bin com.smarthome.Main

This project was developed for academic purposes.
