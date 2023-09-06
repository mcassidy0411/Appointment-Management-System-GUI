package com.mc.model;

import com.mc.helper.JDBCQuery;
import com.mc.helper.TimeUtils;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

/**
 * Represents an appointment with its various attributes
 * @author Michael Cassidy
 */
public class Appointment {
    private int id;
    private String title;
    private String description;
    private String location;
    private String type;
    private Timestamp startTimestamp;
    private Timestamp endTimestamp;
    private Timestamp createdTimestamp;
    private String createdBy;
    private Timestamp updatedTimestamp;
    private String updatedBy;
    private int customerId;
    private int userId;
    private int contactId;

    /**
     * Constructor to create an Appointment object with all attributes.
     * Used to store appointments from the database and display them in the main menu.
     *
     * @param id              The appointment's ID.
     * @param title           The appointment's title.
     * @param description     The appointment's description.
     * @param location        The appointment's location.
     * @param type            The appointment's type.
     * @param startTimestamp  The appointment's start time as a Timestamp.
     * @param endTimestamp    The appointment's end time as a Timestamp.
     * @param createdTimestamp The appointment's creation timestamp.
     * @param createdBy        The appointment's creator.
     * @param updatedTimestamp The appointment's last update timestamp.
     * @param updatedBy        The appointment's last updater.
     * @param customerId      The appointment's customer ID.
     * @param userId          The appointment's user ID.
     * @param contactId       The appointment's contact ID.
     */
    public Appointment(int id, String title, String description, String location, String type, Timestamp startTimestamp,
                       Timestamp endTimestamp, Timestamp createdTimestamp, String createdBy, Timestamp updatedTimestamp, String updatedBy,
                       int customerId, int userId, int contactId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.createdTimestamp = createdTimestamp;
        this.createdBy = createdBy;
        this.updatedTimestamp = updatedTimestamp;
        this.updatedBy = updatedBy;
        this.customerId = customerId;
        this.userId = userId;
        this.contactId = contactId;
    }

    /**
     * Constructor to create an Appointment object from the add appointment screen.
     * Omits id variable as the database auto-generates one for the appointment.
     *
     * @param title           The appointment's title.
     * @param description     The appointment's description.
     * @param location        The appointment's location.
     * @param type            The appointment's type.
     * @param startTimestamp  The appointment's start time as a Timestamp.
     * @param endTimestamp    The appointment's end time as a Timestamp.
     * @param createdTimestamp The appointment's creation timestamp.
     * @param createdBy        The appointment's creator.
     * @param updatedTimestamp The appointment's last update timestamp.
     * @param updatedBy        The appointment's last updater.
     * @param customerId      The appointment's customer ID.
     * @param userId          The appointment's user ID.
     * @param contactId       The appointment's contact ID.
     */
    public Appointment(String title, String description, String location, String type, Timestamp startTimestamp,
                       Timestamp endTimestamp, Timestamp createdTimestamp, String createdBy, Timestamp updatedTimestamp,
                       String updatedBy, int customerId, int userId, int contactId) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.createdTimestamp = createdTimestamp;
        this.createdBy = createdBy;
        this.updatedTimestamp = updatedTimestamp;
        this.updatedBy = updatedBy;
        this.customerId = customerId;
        this.userId = userId;
        this.contactId = contactId;
    }

    /**
     * Checks if the given object is equal to the current Appointment object.
     * Two Appointment objects are considered equal if all their attributes are equal.
     *
     * @param obj The object to compare with the current Appointment object.
     * @return True if the given object is equal to the current Appointment object, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Appointment other)) {
            return false;
        }
        return Objects.equals(id, other.id)
                && Objects.equals(title, other.title)
                && Objects.equals(description, other.description)
                && Objects.equals(location, other.location)
                && Objects.equals(type, other.type)
                && Objects.equals(startTimestamp, other.startTimestamp)
                && Objects.equals(endTimestamp, other.endTimestamp)
                && Objects.equals(createdTimestamp, other.createdTimestamp)
                && Objects.equals(createdBy, other.createdBy)
                && Objects.equals(updatedTimestamp, other.updatedTimestamp)
                && Objects.equals(updatedBy, other.updatedBy)
                && Objects.equals(customerId, other.customerId)
                && Objects.equals(userId, other.userId)
                && Objects.equals(contactId, other.contactId);
    }

    public LocalTime getStartTime() {
        return TimeUtils.convertToLocalDateTime(startTimestamp).toLocalTime();
    }

    public LocalTime getEndTime() {
        return TimeUtils.convertToLocalDateTime(endTimestamp).toLocalTime();
    }

    public LocalDate getStartDate() {
        return TimeUtils.convertToLocalDateTime(startTimestamp).toLocalDate();
    }

    public LocalDate getEndDate() {
        return TimeUtils.convertToLocalDateTime(endTimestamp).toLocalDate();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Timestamp getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(Timestamp startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public Timestamp getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(Timestamp endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    public Timestamp getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Timestamp createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getUpdatedTimestamp() {
        return updatedTimestamp;
    }

    public void setUpdatedTimestamp(Timestamp updatedTimestamp) {
        this.updatedTimestamp = updatedTimestamp;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public String getContactName() { return JDBCQuery.getContactName(contactId); }
}
