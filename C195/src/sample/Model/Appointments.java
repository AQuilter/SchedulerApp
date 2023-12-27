package sample.Model;

import java.time.LocalDateTime;

public class Appointments {

    private int appointmentId;
    private String title;
    private String description;
    private String location;
    private String type;
    private LocalDateTime start;
    private LocalDateTime end;
    private int customerId;
    private int userId;
    private int contactId;


    /** Constructor
     * @param appointmentId The appointment ID.
     * @param title the appointment title/name.
     * @param description An appointment description.
     * @param location The appointment location.
     * @param type The appointment type.
     * @param start the appointment start date/time.
     * @param end The appointment end date/time.
     * @param customerId The customer ID.
     * @param userId The user ID.
     * @param contactId The contact ID.
     */
    public Appointments(int appointmentId, String title, String description, String location, String type,
                        LocalDateTime start, LocalDateTime end, int customerId, int userId, int contactId) {
        this.appointmentId = appointmentId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerId = customerId;
        this.userId = userId;
        this.contactId = contactId;
    }


    /**
     * @return Returns the appointment ID.
     */
    public int getAppointmentId() {
        return appointmentId;
    }

    /**
     * @return Returns the appointment name/title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return Returns the appointment description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return Returns the appointment location.
     */
    public String getLocation() {
        return location;
    }

    /**
     * @return Returns the appointment type.
     */
    public String getType() {
        return type;
    }

    /**
     * @return Returns the appointment start date/time.
     */
    public LocalDateTime getStart() {
        return start;
    }

    /**
     * @return Returns the appointment end date/time.
     */
    public LocalDateTime getEnd() {
        return end;
    }

    /**
     * @return Returns the customer ID.
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * @return Returns the user ID.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * @return Returns the contact ID.
     */
    public int getContactId() {
        return contactId;
    }
}
