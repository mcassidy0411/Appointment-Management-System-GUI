package com.mc.controller;

import com.mc.helper.NavigateToScene;
import com.mc.helper.JDBCQuery;
import com.mc.helper.NotifyUser;
import com.mc.model.Appointment;
import com.mc.model.CurrentUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import java.sql.Time;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Controller class for the MainMenu scene.
 * Handles the display and management of appointments and navigation to other scenes.
 * Implements SharedControllerInterface to manage currentUser.
 * @author Michael Cassidy
 */
public class MainMenuController implements SharedControllerInterface {
    @FXML
    private Label timeZoneLbl;
    @FXML
    private Button addApptBtn;
    @FXML
    private Label apptLbl;
    @FXML
    private RadioButton apptAllBtn;
    @FXML
    private RadioButton apptMonthBtn;
    @FXML
    private TableView<Appointment> apptTbl;
    @FXML
    private ToggleGroup apptTglGrp;
    @FXML
    private RadioButton apptWeekBtn;
    @FXML
    private TableColumn<Appointment, Integer> contactIdCol;
    @FXML
    private TableColumn<Appointment, Integer> customerIdCol;
    @FXML
    private Button deleteApptBtn;
    @FXML
    private TableColumn<Appointment, String> descriptionCol;
    @FXML
    private TableColumn<Appointment, Date> endDateCol;
    @FXML
    private TableColumn<Appointment, Time> endTimeCol;
    @FXML
    private TableColumn<Appointment, Integer> idCol;
    @FXML
    private TableColumn<Appointment, String> locationCol;
    @FXML
    private Button logoffBtn;
    @FXML
    private Button modifyApptBtn;
    @FXML
    private TableColumn<Appointment, Date> startDateCol;
    @FXML
    private TableColumn<Appointment, LocalTime> startTimeCol;
    @FXML
    private TableColumn<Appointment, String> titleCol;
    @FXML
    private TableColumn<Appointment, String> typeCol;
    @FXML
    private TableColumn<Appointment, Integer> userIdCol;
    @FXML
    private RadioButton viewCustomersBtn;
    @FXML
    private RadioButton viewReportsBtn;
    private String appointmentView;
    private final ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

    private CurrentUser currentUser;

    /**
     * Initializes the MainMenuController.
     * Sets up the UI elements and populates the appointment table based on the current appointment view.
     */
    @FXML
    private void initialize() {
        setLanguage();

        switch (this.appointmentView) {
            case "month" -> {
                apptMonthBtn.setSelected(true);
                onActionShowMonth();
            }
            case "week" -> {
                apptWeekBtn.setSelected(true);
                onActionShowWeek();
            }
            default -> {
                apptAllBtn.setSelected(true);
                onActionShowAll();
            }
        }
    }

    /**
     * Sets the current user for this controller.
     *
     * @param currentUser The CurrentUser object representing the logged-in user
     */
    @Override
    public void setCurrentUser(CurrentUser currentUser) { this.currentUser = currentUser; }

    /**
     * Sets the appointment view for the controller.
     *
     * @param appointmentView A string representing the current appointment view ("all", "month", or "week")
     */
    public void setAppointmentView(String appointmentView) { this.appointmentView = appointmentView; }

    /**
     * Notifies the user of upcoming appointments.
     */
    public void notifyOnLogin() {
        NotifyUser.upcomingAppointments(appointmentList);
    }

    /**
     * Sets the language for the UI elements according to the user's locale.
     */
    private void setLanguage() {
        Locale userLocale = Locale.getDefault();
        ZoneId timeZone = ZoneId.systemDefault();

        ResourceBundle messages = ResourceBundle.getBundle("LanguageBundle", userLocale);
        apptLbl.setText(messages.getString("main.appointments"));
        apptAllBtn.setText(messages.getString("main.viewAll"));
        apptMonthBtn.setText(messages.getString("main.viewMonth"));
        apptWeekBtn.setText(messages.getString("main.viewWeek"));
        viewCustomersBtn.setText(messages.getString("main.viewCustomers"));
        viewReportsBtn.setText(messages.getString("main.viewReports"));
        timeZoneLbl.setText(messages.getString("login.timezone") + " : " + timeZone);
        idCol.setText(messages.getString("main.id"));
        titleCol.setText(messages.getString("main.title"));
        descriptionCol.setText(messages.getString("main.description"));
        locationCol.setText(messages.getString("main.location"));
        typeCol.setText(messages.getString("main.type"));
        startTimeCol.setText(messages.getString("main.startTime"));
        endTimeCol.setText(messages.getString("main.endTime"));
        startDateCol.setText(messages.getString("main.startDate"));
        endDateCol.setText(messages.getString("main.endDate"));
        customerIdCol.setText(messages.getString("main.customerId"));
        userIdCol.setText(messages.getString("main.userId"));
        contactIdCol.setText(messages.getString("main.contactId"));
        addApptBtn.setText(messages.getString("main.addAppt"));
        modifyApptBtn.setText(messages.getString("main.modAppt"));
        deleteApptBtn.setText(messages.getString("main.delAppt"));
        logoffBtn.setText(messages.getString("main.logoff"));
    }

    /**
     * Populates the appointment table with data from the appointmentList.
     */
    private void populateTable() {
        apptTbl.setItems(appointmentList);
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        startTimeCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endTimeCol.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        startDateCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        userIdCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
        contactIdCol.setCellValueFactory(new PropertyValueFactory<>("contactId"));

        apptTbl.autosize();
    }

    /**
     * Navigates to the "AddAppointment" scene.
     *
     * @param event The ActionEvent that triggered the method
     * @throws IOException If there's an issue loading the FXML file
     */
    @FXML
    private void onActionAddAppt(ActionEvent event) throws IOException{
        NavigateToScene.goToScene(event, "AddAppointment", currentUser);
    }

    /**
     * Handles the "Delete Appointment" button action.
     * Deletes the selected appointment from the database and updates the table view.
     * Notifies user if no Appointment is selected.
     */
    @FXML
    private void onActionDeleteAppt() {
        Appointment selectedAppt = apptTbl.getSelectionModel().getSelectedItem();
        if(selectedAppt != null) {
            if(NotifyUser.confirmDeleteObject(selectedAppt)) {
                JDBCQuery.deleteAppointment(selectedAppt);

                if (apptMonthBtn.isSelected()) {
                    onActionShowMonth();
                } else if (apptWeekBtn.isSelected()) {
                    onActionShowWeek();
                } else {
                    onActionShowAll();
                }

                NotifyUser.objectDeleted(selectedAppt);
            }
        } else {
            NotifyUser.nothingSelected("appointment");
        }
    }

    /**
     * Navigates to the "ModifyAppointment" scene.
     * Notifies user if no Appointment is selected.
     *
     * @param event The ActionEvent that triggered the method
     * @throws IOException If there's an issue loading the FXML file
     */
    @FXML
    private void onActionModifyAppt(ActionEvent event) throws IOException {
        Appointment selectedAppt = apptTbl.getSelectionModel().getSelectedItem();
        if (selectedAppt != null) {
            NavigateToScene.goToScene(event, "ModifyAppointment", selectedAppt, currentUser);
        } else {
            NotifyUser.nothingSelected("appointment");
        }
    }

    /**
     * Populates the appointment table with all appointments.
     */
    @FXML
    private void onActionShowAll() {
        appointmentList.setAll(JDBCQuery.getAllAppointmentList());
        populateTable();
    }

    /**
     * Populates the appointment table with appointments for the current month.
     */
    @FXML
    private void onActionShowMonth() {
        appointmentList.clear();
        appointmentList.setAll(JDBCQuery.getMonthAppointmentList());
        populateTable();
    }

    /**
     * Populates the appointment table with appointments for the current week.
     */
    @FXML
    private void onActionShowWeek() {
        appointmentList.clear();
        appointmentList.setAll(JDBCQuery.getWeekAppointmentList());
        populateTable();
    }

    /**
     * Navigates to the "CustomerMenu" scene.
     * Passes currentUSer to new scene.
     *
     * @param event The ActionEvent that triggered the method
     * @throws IOException If there's an issue loading the FXML file
     */
    @FXML
    private void onActionViewCustomers(ActionEvent event) throws IOException {
        NavigateToScene.goToScene(event, "CustomerMenu", currentUser);
    }

    /**
     * Navigates back to the "LoginScreen" scene and clears the currentUser.
     *
     * @param event The ActionEvent that triggered the method
     * @throws IOException If there's an issue loading the FXML file
     */
    @FXML
    private void onActionLogoff(ActionEvent event) throws IOException {
        NavigateToScene.goToScene(event, "LoginScreen", null);
    }

    /**
     * Navigates to the "ReportMenu" scene.
     * Passes currentUSer to new scene.
     *
     * @param event The ActionEvent that triggered the method
     * @throws IOException If there's an issue loading the FXML file
     */
    @FXML
    private void onActionViewReports(ActionEvent event) throws IOException {
        NavigateToScene.goToScene(event, "ReportMenu", currentUser);
    }
}
