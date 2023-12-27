# Scheduling Desktop Application
## Overview

This project involves the development of a GUI-based scheduling desktop application for a global consulting organization. 
The application is designed to interact with a MySQL database provided by the client, and it must adhere to specific business requirements outlined in the scenario. 
The primary focus is on creating a user-friendly interface with functionalities related to customer records, appointments, and scheduling. 
The project aims to showcase essential skills relevant to software design and development, providing a potential portfolio piece for future employment.
## Project Structure

The application is written using Java and JavaFX SDK, adhering to the guidelines specified in the task requirements. 
The code includes functionalities related to user authentication, customer record management, appointment handling, and various reporting features.

## Key Functionalities
<b>1. Log-in Form </b>

* Accepts username and password, providing appropriate error messages.
* Determines the user’s location and displays it on the form.
* Displays the form in English or French based on the user’s computer language setting.
* Translates error control messages into English or French based on language settings.

<b>2. Customer Record Functionalities </b>

* Allows adding, updating, and deleting of customer records.
* Ensures that all customer appointments are deleted first when deleting a customer record (cascade deletion).
* Collects and displays customer data, including name, address, postal code, and phone number.
* Auto-generates Customer IDs and has options for selecting first-level division and country data.

<b>3. Scheduling Functionalities </b>

a. Appointment Management

* Enables adding, updating, and deleting of appointments.
* Associates a contact name with an appointment using a drop-down menu.
* Displays custom messages in the user interface for various actions.
* Auto-generates Appointment IDs and disables them to prevent modification.
* Records data such as title, description, location, contact, type, start/end date and time, Customer_ID, and User_ID.

b. Viewing Schedules

* Allows viewing appointments by month and week.
* Provides options to filter appointments.
* Displays relevant columns, including Appointment_ID, title, description, location, contact, type, start/end date and time, Customer_ID, and User_ID.

c. Time Zone Handling

* Stores appointment times in Coordinated Universal Time (UTC).
* Automatically updates appointment times according to the local time zone set on the user’s computer.

d. Input Validation and Error Checks

* Implements checks to prevent scheduling outside business hours, overlapping appointments, and incorrect username/password.
* Displays custom messages in the user interface for each error check.

e. Appointment Alerts

* Alerts the user when there is an appointment within 15 minutes of log-in.
* Displays custom messages indicating upcoming appointments or no upcoming appointments.

f. Reporting

* Generates reports for the total number of customer appointments by type and month.
* Provides a schedule for each contact, including appointment details.
* Allows customization for an additional report.

## Code Improvements

The code incorporates at least two different lambda expressions to enhance readability and efficiency.
User Activity Tracking

User log-in attempts, dates, and timestamps are recorded in a file named login_activity.txt. Each new record is appended to the existing file, providing a comprehensive log of user activities.
## Documentation

Descriptive Javadoc comments cover at least 70% of the classes and their members throughout the code. The comments include justifications for each lambda expression used in the methods.
## Conclusion

This scheduling desktop application demonstrates a robust and user-friendly solution that meets the specified business requirements. The use of Java and JavaFX SDK, adherence to coding standards, and comprehensive documentation make this project a valuable addition to a software developer's portfolio. The application's versatility and feature-rich interface showcase essential skills relevant to real-world job assignments and technical interviews.
