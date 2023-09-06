package com.mc.controller;

import com.mc.helper.JDBCQuery;
import com.mc.helper.NavigateToScene;
import com.mc.helper.NotifyUser;
import com.mc.helper.TimeUtils;
import com.mc.model.Appointment;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Controller for the Modify Appointment view.
 * Allows the user to modify existing appointment details.
 * Extends BaseAppointmentController for shared functionality between this class and AddAppointmentController
 * @author Michael Cassidy
 */
public class ModifyAppointmentController extends BaseAppointmentController {
    @FXML
    private TextField apptIdField;
    @FXML
    private Label apptIdLbl;
    @FXML
    private Button cancelBtn;
    @FXML
    private ComboBox<String> contactComboBox;
    @FXML
    private Label contactLbl;
    @FXML
    private ComboBox<Integer> customerIdComboBox;
    @FXML
    private Label customerIdLbl;
    @FXML
    private TextField descriptionField;
    @FXML
    private Label descriptionLbl;
    @FXML
    private Label endDateLbl;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private ComboBox<LocalTime> endTimeComboBox;
    @FXML
    private Label endTimeLbl;
    @FXML
    private Label headerLbl;
    @FXML
    private TextField locationField;
    @FXML
    private Label locationLbl;
    @FXML
    private Button saveBtn;
    @FXML
    private Label startDateLbl;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private ComboBox<LocalTime> startTimeComboBox;
    @FXML
    private Label startTimeLbl;
    @FXML
    private TextField titleField;
    @FXML
    private Label titleLbl;
    @FXML
    private TextField typeField;
    @FXML
    private Label typeLbl;
    @FXML
    private Label timeZoneMessageLbl;
    private Appointment appointment;

    /**
     * Initializes the ModifyAppointmentController.
     * Sets up combo boxes, date pickers, and labels.
     */
    @FXML
    private void initialize() {
        startTimeComboBox.setItems(getTimeComboBox(8,0, 22, 0).getItems());
        endTimeComboBox.setItems(getTimeComboBox(8,15, 22, 15).getItems());
        contactComboBox.setItems(getContactComboBox().getItems());
        customerIdComboBox.setItems(getCustomerIdComboBox().getItems());

        startDatePicker.setValue(LocalDate.now());
        startDatePicker.setDayCellFactory(getDateCellFactory());
        endDatePicker.setValue(LocalDate.now());
        endDatePicker.setDayCellFactory(getDateCellFactory());

        setListeners(startTimeComboBox, endTimeComboBox, startDatePicker, endDatePicker);

        setLabels("mod", headerLbl, apptIdLbl, cancelBtn, contactLbl, customerIdLbl, descriptionLbl, endDateLbl,
                endTimeLbl, locationLbl, saveBtn, startDateLbl, startTimeLbl, titleLbl, typeLbl, timeZoneMessageLbl,
                apptIdField);
    }

    /**
     * Sets the appointment to be modified.
     *
     * @param appointment The appointment to be modified
     */
    public void setAppointment(Appointment appointment) { this.appointment = appointment; }

    /**
     * Sets the fields in the form with the appointment's data.
     */
    public void setFields() {
        apptIdField.setStyle("-fx-font-weight: normal");
        apptIdField.setText(Integer.toString(appointment.getId()));
        titleField.setText(appointment.getTitle());
        descriptionField.setText(appointment.getDescription());
        locationField.setText(appointment.getLocation());
        typeField.setText(appointment.getType());
        startTimeComboBox.setValue(appointment.getStartTime());
        endTimeComboBox.setValue(appointment.getEndTime());
        startDatePicker.setValue(appointment.getStartDate());
        endDatePicker.setValue(appointment.getEndDate());
        contactComboBox.setValue(appointment.getContactName());
        customerIdComboBox.setValue(appointment.getCustomerId());
    }

    /**
     * Creates a modified appointment with the updated information from the form.
     *
     * @param updatedTimestamp The timestamp when the appointment was last updated
     * @param userId The ID of the user who is updating the appointment
     * @param updatedBy The username of the user who is updating the appointment
     * @return A new Appointment object with the updated information
     */
    private Appointment createModifiedAppointment(Timestamp updatedTimestamp, int userId, String updatedBy) {
        int id = appointment.getId();
        String title = titleField.getText();
        String description = descriptionField.getText();
        String location = locationField.getText();
        String type = typeField.getText();
        Timestamp startTimestamp = TimeUtils.convertToUtcTimestamp(startDatePicker.getValue(),
                startTimeComboBox.getValue());
        Timestamp endTimestamp = TimeUtils.convertToUtcTimestamp(endDatePicker.getValue(),
                endTimeComboBox.getValue());
        Timestamp createdTimestamp = appointment.getCreatedTimestamp();
        String createdBy = appointment.getCreatedBy();
        int customerId = customerIdComboBox.getValue();

        int contactId = JDBCQuery.getContactId(contactComboBox.getValue());

        return new Appointment(id, title, description, location, type, startTimestamp, endTimestamp, createdTimestamp,
                createdBy, updatedTimestamp, updatedBy, customerId, userId, contactId);
    }

    /**
     * Validates the appointment form and saves the changes to the database.
     * Navigates back to the main menu.
     *
     * @param event The ActionEvent that triggered the method
     * @throws IOException If there's an issue loading the FXML file
     */
    @FXML
    private void onActionSave(ActionEvent event) throws IOException {
        if(validateAppointment(customerIdComboBox, contactComboBox, titleField, descriptionField,
                locationField, typeField, startTimeComboBox, endTimeComboBox, startDatePicker, endDatePicker,
                appointment.getId())) {
            Timestamp updatedTimestamp = appointment.getUpdatedTimestamp();
            int userId = currentUser.getUserId();
            String updatedBy = appointment.getUpdatedBy();
            Appointment modifiedAppointment = createModifiedAppointment(updatedTimestamp, userId, updatedBy);

            if(modifiedAppointment.equals(appointment)) {
                if(NotifyUser.noChangesMade()) {
                    NavigateToScene.goToMainMenu(event, "all", false, currentUser);
                } else {
                    NavigateToScene.goToScene(event, "ModifyAppointment", appointment, currentUser);
                }
            } else {
                updatedTimestamp = TimeUtils.convertToUtcTimestamp(LocalDate.now(), LocalTime.now());
                updatedBy = currentUser.getUsername();
                Appointment newAppointment = createModifiedAppointment(updatedTimestamp, userId, updatedBy);
                JDBCQuery.updateAppointment(newAppointment);
                NotifyUser.objectUpdated(newAppointment);
                NavigateToScene.goToMainMenu(event, "all", false, currentUser);
            }
        }
    }
}
