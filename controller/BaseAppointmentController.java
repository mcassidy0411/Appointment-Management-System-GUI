package com.mc.controller;

import com.mc.helper.JDBCQuery;
import com.mc.helper.NavigateToScene;
import com.mc.helper.NotifyUser;
import com.mc.helper.TimeUtils;
import com.mc.model.Appointment;
import com.mc.model.CurrentUser;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.*;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Abstract base class for appointment controllers, providing common functionality
 * and fields for AddAppointmentController and ModifyAppointmentController.
 * Implements the SharedControllerInterface to support currentUser handling.
 * @author Michael Cassidy
 */
public abstract class BaseAppointmentController implements SharedControllerInterface {
    private static final Locale userLocale = Locale.getDefault();
    private static final ZoneId userTimeZone = ZoneId.systemDefault();
    private static final ResourceBundle messages = ResourceBundle.getBundle("LanguageBundle", userLocale);
    protected CurrentUser currentUser;

    /**
     * Sets the current user for this controller.
     * @param currentUser the current user
     */
    @Override
    public void setCurrentUser(CurrentUser currentUser) { this.currentUser = currentUser; }

    /**
     * Creates and returns a ComboBox containing LocalTime objects for appointment times.
     * Time slots are incremented by 15 minutes and converted to the user's local time zone.
     *
     * @param startingHour the starting hour for the time slots
     * @param startingMinute the starting minute for the time slots
     * @param endingHour the ending hour for the time slots
     * @param endingMinute the ending minute for the time slots
     * @return a ComboBox containing LocalTime objects for appointment times
     */
    protected static ComboBox<LocalTime> getTimeComboBox(int startingHour, int startingMinute, int endingHour,
                                                         int endingMinute) {
        ObservableList<LocalTime> hours = FXCollections.observableArrayList();
        ComboBox<LocalTime> timeComboBox = new ComboBox<>();
        ZoneId estZone = ZoneId.of("America/New_York");

//      Starting time EST
        LocalTime time = LocalTime.of(startingHour, startingMinute);

        while (time.isBefore(LocalTime.of(endingHour, endingMinute))) {
//          Convert to EST time zone
            LocalDateTime estDateTime = LocalDateTime.now().atZone(estZone).with(time).toLocalDateTime();

//          Convert to user's time zone
            LocalDateTime userDateTime = estDateTime.atZone(estZone).withZoneSameInstant(userTimeZone).toLocalDateTime();

//          Add to the list
            hours.add(userDateTime.toLocalTime());

//          Increment by 15 minutes
            time = time.plusMinutes(15);
        }

        timeComboBox.setItems(hours);
        return timeComboBox;
    }

    /**
     * Creates and returns a ComboBox containing contact names.
     * The contact names are fetched using the JDBCQuery.getContactList() method.
     *
     * @return a ComboBox containing contact names
     */
    protected static ComboBox<String> getContactComboBox() {
        ComboBox<String> contactComboBox = new ComboBox<>();
        contactComboBox.setItems(JDBCQuery.getContactList());
        return contactComboBox;
    }

    /**
     * Creates and returns a ComboBox containing customer IDs.
     * The customer IDs are fetched using the JDBCQuery.getCustomerIdList() method.
     *
     * @return a ComboBox containing customer IDs
     */
    protected static ComboBox<Integer> getCustomerIdComboBox() {
        ComboBox<Integer> customerIdComboBox = new ComboBox<>();
        customerIdComboBox.setItems(JDBCQuery.getCustomerIdList());
        return customerIdComboBox;
    }

    /**
     * Creates and returns a Callback for a DatePicker that disables past dates.
     * Uses a lambda expression to return a new DateCell with an overridden updateItem method.
     * The lambda expression is used to create a concise and readable implementation of the Callback.call() method,
     * which returns an object of type DateCell. The lambda expression provides this implementation
     * without having to create an inner class or a separate class that implements the Callback interface.
     *
     * @return a Callback for a DatePicker that disables past dates
     */
    protected static Callback<DatePicker, DateCell> getDateCellFactory() {
        return dp -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(empty || item.isBefore(LocalDate.now()));
            }
        };
    }

    /**
     * Sets various listeners to enforce appointment constraints for the provided ComboBoxes and DatePickers.
     * The constraints include:
     * - ensuring start and end times are within allowed timeframes
     * - enforcing appointment duration limits
     * Lambda Expressions Used:
     * 1) ChangeListener<LocalTime> (observable, oldValue, newValue) ->
     * 2) DatePicker.valueProperty().addListener((observable, oldValue, newValue) ->
     * These are both lambda implementations of the changed() method from the ChangeListener interface.
     * Using lambda expressions here allows for a more concise and readable way to define the behavior when the value
     * of the associated DatePicker or ComboBox changes, instead of creating an inner class or a separate class that
     * implements the ChangeListener interface.
     *
     * @param startTimeComboBox the ComboBox for the start time
     * @param endTimeComboBox the ComboBox for the end time
     * @param startDatePicker the DatePicker for the start date
     * @param endDatePicker the DatePicker for the end date
     */
    protected void setListeners(ComboBox<LocalTime> startTimeComboBox, ComboBox<LocalTime> endTimeComboBox,
                                DatePicker startDatePicker, DatePicker endDatePicker) {
        startDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
//          Changes end date to match start date when new start date is selected unless user's local time would cause
//          a valid appointment to extend past midnight and into the next day
            if(newValue != null && newValue.isAfter(endDatePicker.getValue())) {
                endDatePicker.setValue(newValue);
            } else if(newValue != null && newValue.isBefore(endDatePicker.getValue()) &&
                    (endTimeComboBox.getValue() == null ||
                            endTimeComboBox.getValue().isAfter(LocalTime.of(8,0)))) {
                endDatePicker.setValue(newValue);
            }
        });

        endDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
//          Prevent user from setting end date prior to start date
            if(newValue != null && newValue.isBefore(startDatePicker.getValue())) {
                endDatePicker.setValue(startDatePicker.getValue());
            }
        });

        ChangeListener<LocalTime> endTimeChangeListener = (observable, oldValue, newValue) -> {
            if (startTimeComboBox.getValue() != null && newValue != null) {
//              Convert the start and end times to the user's local time
                LocalTime startTimeLocal = TimeUtils.convertUtcToEst(
                        TimeUtils.convertToUtcTimestamp(startDatePicker.getValue(), startTimeComboBox.getValue()));
                LocalTime endTimeLocal = TimeUtils.convertUtcToEst(
                        TimeUtils.convertToUtcTimestamp(endDatePicker.getValue(), newValue));

                if (newValue.isBefore(startTimeComboBox.getValue())) {
                    endDatePicker.setValue(startDatePicker.getValue().plusDays(1));
                } else {
                    endDatePicker.setValue(startDatePicker.getValue());
                }

//              Calculate the appointment duration based on the user's local time
                long duration = checkDuration(startDatePicker.getValue(), startTimeLocal,
                        endDatePicker.getValue(), endTimeLocal);

                if (duration > 120) {
                    NotifyUser.invalidAppointmentDuration();
                    endTimeComboBox.setValue(oldValue);
                }
            }
        };

        ChangeListener<LocalTime> startTimeChangeListener = (observable, oldValue, newValue) -> {
            // If selected appointment time/date is in the past, set start and end date to the next day
            long minutesFromNow = checkDuration(LocalDate.now(), LocalTime.now(), startDatePicker.getValue(), newValue);
            if (minutesFromNow <= 0) {
                startDatePicker.setValue(startDatePicker.getValue().plusDays(1));
            }
            // Set end time based on start time
            if (newValue != null) {
                // Convert the start time to the user's local time
                LocalTime startTimeLocal = TimeUtils.convertUtcToEst(
                        TimeUtils.convertToUtcTimestamp(startDatePicker.getValue(), newValue));

                // Perform comparisons and calculations based on the user's local time
                if (startTimeLocal.isAfter(LocalTime.of(21, 0))) {
//                    endTimeComboBox.setValue(newValue.plusMinutes(15));
                    endTimeComboBox.valueProperty().removeListener(endTimeChangeListener);
                    endTimeComboBox.setValue(newValue.plusMinutes(15));
                    endTimeComboBox.valueProperty().addListener(endTimeChangeListener);
                } else {
                    endTimeComboBox.valueProperty().removeListener(endTimeChangeListener);
                    endTimeComboBox.setValue(newValue.plusHours(1));
                    endTimeComboBox.valueProperty().addListener(endTimeChangeListener);
                }
            }
        };

        startTimeComboBox.valueProperty().addListener(startTimeChangeListener);

        endTimeComboBox.valueProperty().addListener(endTimeChangeListener);
    }

    /**
     * Checks the duration between two date-time pairs in minutes.
     *
     * @param startDate the start date
     * @param startTime the start time
     * @param endDate the end date
     * @param endTime the end time
     * @return the duration between the two date-time pairs in minutes
     */
    private long checkDuration(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        Timestamp startTimestamp = TimeUtils.convertToUtcTimestamp(startDate, startTime);
        Timestamp endTimestamp = TimeUtils.convertToUtcTimestamp(endDate, endTime);
        Duration duration = Duration.between(startTimestamp.toInstant(), endTimestamp.toInstant());

        return duration.toMinutes();
    }

    /**
     * Sets the text for various labels, buttons, and fields based on the provided addMod parameter.
     * addMod should be either "add" or "mod" for adding or modifying appointments, respectively.
     *
     * @param addMod a String representing the action, either "add" or "mod"
     * @param headerLbl the header label
     * @param apptIdLbl the appointment ID label
     * @param cancelBtn the cancel button
     * @param contactLbl the contact label
     * @param customerIdLbl the customer ID label
     * @param descriptionLbl the description label
     * @param endDateLbl the end date label
     * @param endTimeLbl the end time label
     * @param locationLbl the location label
     * @param saveBtn the save button
     * @param startDateLbl the start date label
     * @param startTimeLbl the start time label
     * @param titleLbl the title label
     * @param typeLbl the type label
     * @param timeZoneMessageLbl the time zone message label
     * @param apptIdField the appointment ID field
     */
    protected void setLabels(String addMod, Label headerLbl, Label apptIdLbl, Button cancelBtn, Label contactLbl,
                             Label customerIdLbl, Label descriptionLbl, Label endDateLbl, Label endTimeLbl, Label locationLbl,
                             Button saveBtn, Label startDateLbl, Label startTimeLbl, Label titleLbl, Label typeLbl,
                             Label timeZoneMessageLbl, TextField apptIdField) {
        if(addMod.equals("add")) {
            headerLbl.setText(messages.getString("main.addAppt"));
            apptIdField.setText(messages.getString("appt.autoGen"));
        } else if(addMod.equals("mod")) {
            headerLbl.setText(messages.getString("main.modAppt"));
        }

        apptIdLbl.setText(messages.getString("appt.apptId"));
        cancelBtn.setText(messages.getString("appt.cancel"));
        contactLbl.setText(messages.getString("appt.contact"));
        customerIdLbl.setText(messages.getString("appt.customerId"));
        descriptionLbl.setText(messages.getString("appt.description"));
        endDateLbl.setText(messages.getString("appt.endDate"));
        endTimeLbl.setText(messages.getString("appt.endTime"));
        locationLbl.setText(messages.getString("appt.location"));
        saveBtn.setText(messages.getString("appt.save"));
        startDateLbl.setText(messages.getString("appt.startDate"));
        startTimeLbl.setText(messages.getString("appt.startTime"));
        titleLbl.setText(messages.getString("appt.title"));
        typeLbl.setText(messages.getString("appt.type"));

        timeZoneMessageLbl.setText(messages.getString("appt.businessHours"));
        if(!userTimeZone.equals(ZoneId.of("America/New_York"))) {
            timeZoneMessageLbl.setText(timeZoneMessageLbl.getText() + "  " +
                    messages.getString("appt.timeZoneMessage") + " " + userTimeZone + ".");
        }
    }

    /**
     * Handles the cancel button action event, returning the user to the main menu.
     *
     * @param event the ActionEvent representing the cancel button click
     * @throws IOException if an error occurs while navigating to the main menu
     */
    @FXML
    private void onActionCancel(ActionEvent event) throws IOException {
        NavigateToScene.goToMainMenu(event, "all", false, currentUser);
    }

    /**
     * Validates appointment data before saving.
     * Ensures that no fields are empty, appointment times are within business hours,
     * appointments are in the future, and no appointment overlaps occur.
     *
     * @param customerIdComboBox the customer ID ComboBox
     * @param contactIdComboBox the contact ComboBox
     * @param titleField the title TextField
     * @param descriptionField the description TextField
     * @param locationField the location TextField
     * @param typeField the type TextField
     * @param startTimeComboBox the start time ComboBox
     * @param endTimeComboBox the end time ComboBox
     * @param startDatePicker the start date DatePicker
     * @param endDatePicker the end date DatePicker
     * @param appointmentId the appointment ID
     * @return true if the appointment data is valid, false otherwise
     */
    protected boolean validateAppointment(ComboBox<Integer> customerIdComboBox, ComboBox<String> contactIdComboBox,
                                          TextField titleField, TextField descriptionField, TextField locationField,
                                          TextField typeField, ComboBox<LocalTime> startTimeComboBox,
                                          ComboBox<LocalTime> endTimeComboBox, DatePicker startDatePicker,
                                          DatePicker endDatePicker, int appointmentId) {
//      Check for null or empty values in all Fields, ComboBoxes and DatePickers
        StringBuilder nullInputMessage = new StringBuilder();

        if(titleField.getText() == null || titleField.getText().isEmpty()) {
            nullInputMessage.append(messages.getString("main.title")).append("\n");
        }
        if(descriptionField.getText() == null || descriptionField.getText().isEmpty()) {
            nullInputMessage.append(messages.getString("main.description")).append("\n");
        }
        if(locationField.getText() == null || locationField.getText().isEmpty()) {
            nullInputMessage.append(messages.getString("main.location")).append("\n");
        }
        if(typeField.getText() == null || typeField.getText().isEmpty()) {
            nullInputMessage.append(messages.getString("main.type")).append("\n");
        }
        if(startTimeComboBox.getValue() == null) {
            nullInputMessage.append(messages.getString("main.startTime")).append("\n");
        }
        if(endTimeComboBox.getValue() == null) {
            nullInputMessage.append(messages.getString("main.endTime")).append("\n");
        }
        if(startDatePicker.getValue() == null) {
            nullInputMessage.append(messages.getString("main.startDate")).append("\n");
        }
        if(endDatePicker.getValue() == null) {
            nullInputMessage.append(messages.getString("main.endDate")).append("\n");
        }
        if(customerIdComboBox.getValue() == null) {
            nullInputMessage.append(messages.getString("main.customerId")).append("\n");
        }
        if(contactIdComboBox.getValue() == null) {
            nullInputMessage.append(messages.getString("main.contactId")).append("\n");
        }

        if(nullInputMessage.length() > 0) {
            String nullInputMessageFinalString = nullInputMessage.toString();
            NotifyUser.nullInput(nullInputMessageFinalString);
            return false;
        }

//      Validate Appointment Times and Dates
        Timestamp startTimestamp =
                TimeUtils.convertToUtcTimestamp(startDatePicker.getValue(), startTimeComboBox.getValue());
        Timestamp endTimestamp =
                TimeUtils.convertToUtcTimestamp(endDatePicker.getValue(), endTimeComboBox.getValue());
        Timestamp currentTimeUTCTimestamp = TimeUtils.convertToUtcTimestamp(LocalDate.now(), LocalTime.now());
        ObservableList<Appointment> appointmentList;

//      Ensure start and end times fall within 8AM-10PM EST
        if (!isWithinBusinessHours(startTimestamp, endTimestamp)) {
            NotifyUser.outsideBusinessHours();
            return false;
//      Ensure start and end times are in the future
        } else if(startTimestamp.before(currentTimeUTCTimestamp) || endTimestamp.before(currentTimeUTCTimestamp)) {
            NotifyUser.pastAppointmentDates();
            return false;
//      Check for overlapping appointments
        } else {
            appointmentList = JDBCQuery.getOverlappingAppointmentList(startTimestamp, endTimestamp, appointmentId);

            if (appointmentList.size() > 0) {
                Appointment overlappingAppointment = appointmentList.get(0);
                NotifyUser.overlappingAppointment(overlappingAppointment);
                return false;
            }
        }
        
        return true;
    }

    /**
     * Determines if the appointment start and end times fall within business hours.
     * Business hours are defined as 8:00 AM to 10:00 PM EST.
     *
     * @param startTimestamp the appointment start time as a Timestamp
     * @param endTimestamp the appointment end time as a Timestamp
     * @return true if the appointment times fall within business hours, false otherwise
     */
    private boolean isWithinBusinessHours(Timestamp startTimestamp, Timestamp endTimestamp) {
        LocalTime startTimeEST = TimeUtils.convertUtcToEst(startTimestamp);
        LocalTime endTimeEST = TimeUtils.convertUtcToEst(endTimestamp);
        LocalTime businessStart = LocalTime.of(8, 0);
        LocalTime businessEnd = LocalTime.of(22, 0);

        return !startTimeEST.isBefore(businessStart) && !startTimeEST.isAfter(businessEnd)
                && !endTimeEST.isBefore(businessStart) && !endTimeEST.isAfter(businessEnd);
    }
}
