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
 * Controller class for adding appointments.
 * Extends BaseAppointmentController for shared functionality between this class and ModifyAppointmentController
 * Handles UI interactions and data input validation for adding appointments.
 * @author Michael Cassidy
 */
public class AddAppointmentController extends BaseAppointmentController {
    @FXML
    private Label headerLbl;
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

    /**
     * Initializes the AddAppointmentController by setting up UI components and listeners.
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

        setLabels("add", headerLbl, apptIdLbl, cancelBtn, contactLbl, customerIdLbl, descriptionLbl, endDateLbl,
                endTimeLbl, locationLbl, saveBtn, startDateLbl, startTimeLbl, titleLbl, typeLbl, timeZoneMessageLbl,
                apptIdField);
    }

    /**
     * Handles the action of saving a new appointment.
     * @param event the action event
     * @throws IOException if there is an error while navigating to the main menu
     */
    @FXML
    private void onActionSave(ActionEvent event) throws IOException {
        if(validateAppointment(customerIdComboBox, contactComboBox, titleField, descriptionField, locationField,
                typeField, startTimeComboBox, endTimeComboBox, startDatePicker, endDatePicker, -1)){

            int customerId = customerIdComboBox.getValue();
            int userId = currentUser.getUserId();
            int contactId = JDBCQuery.getContactId(contactComboBox.getValue());
            String title = titleField.getText();
            String description = descriptionField.getText();
            String location = locationField.getText();
            String type = typeField.getText();
            Timestamp start = TimeUtils.convertToUtcTimestamp(startDatePicker.getValue(), startTimeComboBox.getValue());
            Timestamp end = TimeUtils.convertToUtcTimestamp(endDatePicker.getValue(), endTimeComboBox.getValue());
            Timestamp created = TimeUtils.convertToUtcTimestamp(LocalDate.now(), LocalTime.now());
            String createdBy = JDBCQuery.getUserName(userId);

            Appointment appointment = new Appointment(title, description, location, type, start, end, created, createdBy,
                    created, createdBy, customerId, userId, contactId);

            JDBCQuery.addAppointment(appointment);
            NotifyUser.objectSaved(appointment);
            NavigateToScene.goToMainMenu(event, "all", false, currentUser);
        }
    }
}
