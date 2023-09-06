# Appointment Management System GUI (JavaFX)
## Purpose
This application is an appointment management system designed to help users organize and manage their
tasks and to-do lists. It provides a graphical user interface (GUI) where users can create, update,
and track their tasks, set deadlines, and prioritize tasks based on their importance. The application
is built using object-oriented principles, follows the Model-View-Controller (MVC) architecture, and
integrates with a database to store task-related information.

## Dependencies

-[Java 17 or greater](https://www.java.com/en/)<br>
-[JavaFX](https://openjfx.io/)<br>
-[JDBC Driver for MySQL](https://www.mysql.com/products/connector/)<br>
-[MySQL](https://www.mysql.com/)<br>
-Java IDE ([JetBrains IntelliJ](https://www.jetbrains.com/idea/) is recommended)<br>


## Installation
1. Install the latest version of [Java](https://www.java.com/en/)<br>
2. Install the latest version of [MySQL](https://www.mysql.com/)<br>
3. Install [JavaFX](https://openjfx.io/)<br>
4. Download [JDBC Driver for MySQL](https://www.mysql.com/products/connector/)<br>
5. Import the files from the 'database' folder into a new MySQL schema<br>
6. In the JDBCConnector.java file, update the following varaibles with your MySQL login informaiton:
```
private static final String userName = "[USERNAME]";
private static final String password = "[PASSWORD]";
```
7. In your chosen IDE, run the main method in the AppointmentScheduler.java file.
### Initial Login Information:
```
USERNAME: admin
PASSWORD: admin
```

## Key Features
**-User Authentication:** Users can create accounts and log in to the application to access their personalized task lists.<br>
**-Task Creation and Management:** Users can create new appointments, set date and time, and add descriptions to each task.<br>
**-Task Listing and Sorting:** Tasks are listed in the main interface, and users can sort tasks based on any attribute.<br>
**-Task Editing and Deletion:** Users can edit task details delete tasks.<br>
**-Automatic Time Zone/Language:** Time Zone and language is displayed throughout the application based on the user's locaiton.<br>
**-Automatic Time Conversion:** Business hours are 8AM-10PM EST.  Appointment times are translated into the user's time zone and appointments cannot be scheduled outside business hours.<br>
**-Reporting:** Users can view appointments by type and month, by contact or by country.<br>
**-Customer Management:** Users can add, view, and modify customer data.<br>
**-User-Friendly Interface:** The GUI provides an intuitive and visually appealing layout for easy interaction.<br>
**-Error Handling and Logging:** The application employs error-handling mechanisms to gracefully handle unexpected situations and logs events for troubleshooting.<br>
**-Database Integration:** Task data is stored in a database, allowing users to persist their tasks across application sessions.<br>
**-Documentation and Comments:** The codebase includes thorough documentation and comments to explain its structure and functionality.  A Javadoc explaining the project methods is included.<br><br>
![UI](/screenshots/ui.jpg?raw=true "UI")
