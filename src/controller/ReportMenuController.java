package com.mc.controller;

import com.mc.helper.JDBCQuery;
import com.mc.helper.NavigateToScene;
import com.mc.model.Appointment;
import com.mc.model.CurrentUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Controller class that handles actions and interactions in the Report Menu screen.
 * Allows the user to view and filter appointments by month & type, contact, and country.
 * @author Michael Cassidy
 */
public class ReportMenuController implements SharedControllerInterface {
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
    private TableColumn<Appointment, String> descriptionCol;
    @FXML
    private TableColumn<Appointment, LocalDate> endDateCol;
    @FXML
    private TableColumn<Appointment, LocalTime> endTimeCol;
    @FXML
    private ComboBox<String> filter1ComboBox;
    @FXML
    private ComboBox<String> filter2ComboBox;
    @FXML
    private TableColumn<Appointment, Integer> idCol;
    @FXML
    private TableColumn<Appointment, String> locationCol;
    @FXML
    private Button logoffBtn;
    @FXML
    private ComboBox<String> reportTypeComboBox;
    @FXML
    private Label reportsLbl;
    @FXML
    private TableColumn<Appointment, LocalDate> startDateCol;
    @FXML
    private TableColumn<Appointment, LocalTime> startTimeCol;
    @FXML
    private Label timeZoneLbl;
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
    @FXML
    private Label messageLbl;
    private final Locale userLocale = Locale.getDefault();
    private final ZoneId timeZone = ZoneId.systemDefault();
    ResourceBundle messages = ResourceBundle.getBundle("LanguageBundle", userLocale);
    private ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
    private CurrentUser currentUser;

    /**
     * Sets the current user.
     * @param currentUser The current user object.
     */
    @Override
    public void setCurrentUser(CurrentUser currentUser) { this.currentUser = currentUser; }

    /**
     * Initializes the ReportMenuController, setting the language, report type combo box, and listeners.
     */
    @FXML
    private void initialize() {
        setLanguage();
        setReportTypeComboBox();
        setListeners();
        messageLbl.setText(null);
    }

    /**
     * Sets up the listeners for the report type, filter 1, and filter 2 combo boxes.
     */
    private void setListeners() {
        reportTypeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            reportComboBoxListener(newValue);
        });

        filter1ComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            filter1ComboBoxListener(newValue);
        });

        filter2ComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            filter2ComboBoxListener(newValue);
        });
    }

    /**
     * Sets the language for the UI elements according to the user's locale.
     */
    private void setLanguage() {
        reportsLbl.setText(messages.getString("main.reports"));
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
        logoffBtn.setText(messages.getString("main.logoff"));
    }

    /**
     * Sets the report type combo box with available report options.
     */
    private void setReportTypeComboBox() {
        ObservableList<String> reportTypeList = FXCollections.observableArrayList(
                messages.getString("report.selectReport"),
                messages.getString("report.month"),
                messages.getString("report.contact"),
                messages.getString("report.country")
        );
        reportTypeComboBox.setItems(reportTypeList);
        filter1ComboBox.setVisible(false);
        filter2ComboBox.setVisible(false);

        reportTypeComboBox.getSelectionModel().selectFirst();
    }

    /**
     * Handles actions when the report type combo box value is changed.
     * @param newValue The new value of the report type combo box.
     */
    private void reportComboBoxListener(String newValue) {
        if(newValue != null) {
            appointmentList.clear();
            messageLbl.setText(null);
            if(newValue.equals(messages.getString("report.month"))) {
                reportTypeComboBox.getItems().remove(messages.getString("report.selectReport"));

                ObservableList<String> typeList = JDBCQuery.getTypeList();
                filter1ComboBox.setItems(typeList);
                typeList.add(0, messages.getString("report.selectType"));
                filter1ComboBox.getSelectionModel().selectFirst();
                filter1ComboBox.setVisible(true);

                ObservableList<String> monthList = FXCollections.observableArrayList(
//                        messages.getString("report.january"),
//                        messages.getString("report.february"),
//                        messages.getString("report.march"),
//                        messages.getString("report.april"),
//                        messages.getString("report.may"),
//                        messages.getString("report.june"),
//                        messages.getString("report.july"),
//                        messages.getString("report.august"),
//                        messages.getString("report.september"),
//                        messages.getString("report.october"),
//                        messages.getString("report.november"),
//                        messages.getString("report.december")
                );

                for (Month month : Month.values()) {
                    String localizedMonthName = month.getDisplayName(TextStyle.FULL, userLocale);
                    monthList.add(localizedMonthName);
                }


                filter2ComboBox.setItems(monthList);
                monthList.add(0, messages.getString("report.selectMonth"));
                filter2ComboBox.getSelectionModel().selectFirst();
                filter2ComboBox.setVisible(true);
            } else if(newValue.equals(messages.getString("report.contact"))) {
                reportTypeComboBox.getItems().remove(messages.getString("report.selectReport"));
                ObservableList<String> contactList = JDBCQuery.getContactList();
                filter1ComboBox.setItems(contactList);
                contactList.add(0, messages.getString("report.selectContact"));
                filter1ComboBox.getSelectionModel().selectFirst();
                filter1ComboBox.setVisible(true);
                filter2ComboBox.setVisible(false);
            } else if(newValue.equals(messages.getString("report.country"))) {
                reportTypeComboBox.getItems().remove(messages.getString("report.selectReport"));
                ObservableList<String> countryList = JDBCQuery.getCountryList();
                filter1ComboBox.setItems(countryList);
                countryList.add(0, messages.getString("report.selectCountry"));
                filter1ComboBox.getSelectionModel().selectFirst();
                filter1ComboBox.setVisible(true);
                filter2ComboBox.setVisible(false);
            }
        }
    }

    /**
     * Handles actions when the filter 1 combo box value is changed.
     * @param newValue The new value of the filter 1 combo box.
     */
    private void filter1ComboBoxListener(String newValue) {
        if(newValue != null) {
//          Filter by Type and Month Report:
            if(reportTypeComboBox.getValue().equals(messages.getString("report.month")) &&
                    !Objects.equals(newValue, messages.getString("report.selectType"))) {
//              Remove prompt text when option is selected by user
                filter1ComboBox.getItems().remove(messages.getString("report.selectType"));
//              If filter1ComboBox and filter2ComboBox both have user-selected values, get appointments matching selected
//              criteria: Type and Month
                if(!Objects.equals(filter2ComboBox.getValue(), messages.getString("report.selectMonth")) &&
                        filter1ComboBox.getValue() != null && filter2ComboBox.getValue() != null) {
                    setAppointmentList(JDBCQuery.getAppointmentListByTypeAndMonth(filter1ComboBox.getValue(),
                            filter2ComboBox.getValue()));
                    populateTable();
                    setMessageLbl();
                }
//          Filter by Contact Report:
            } else if(reportTypeComboBox.getValue().equals(messages.getString("report.contact")) &&
                    !Objects.equals(newValue, messages.getString("report.selectContact"))) {
//              Remove prompt text when option is selected by user
                filter1ComboBox.getItems().remove(messages.getString("report.selectContact"));

                int contactId = JDBCQuery.getContactId(filter1ComboBox.getValue());
                setAppointmentList(JDBCQuery.getAppointmentListByContactId(contactId));
                populateTable();
                setMessageLbl();
            } else if(reportTypeComboBox.getValue().equals(messages.getString("report.country")) &&
                    !Objects.equals(newValue, messages.getString("report.selectCountry"))) {
//              Remove prompt text when option is selected by user
                filter1ComboBox.getItems().remove(messages.getString("report.selectCountry"));

                setAppointmentList(JDBCQuery.getAppointmentListByCountry(filter1ComboBox.getValue()));
                populateTable();
                setMessageLbl();
            }
        }
    }

    /**
     * Handles actions when the filter 2 combo box value is changed.
     * @param newValue The new value of the filter 2 combo box.
     */
    private void filter2ComboBoxListener(String newValue) {
        if(newValue != null) {
            if(reportTypeComboBox.getValue().equals(messages.getString("report.month")) &&
                    !Objects.equals(newValue, messages.getString("report.selectMonth"))) {
//              Remove prompt text when option is selected by user
                filter2ComboBox.getItems().remove(messages.getString("report.selectMonth"));
//              If filter1ComboBox and filter2ComboBox both have user-selected values, get appointments matching selected
//              criteria: Type and Month
                if(!Objects.equals(filter1ComboBox.getValue(), messages.getString("report.selectType")) &&
                        filter1ComboBox.getValue() != null && filter2ComboBox.getValue() != null) {
                    setAppointmentList(JDBCQuery.getAppointmentListByTypeAndMonth(filter1ComboBox.getValue(),
                            filter2ComboBox.getValue()));
                    populateTable();
                    setMessageLbl();
                }
            }
        }
    }

    /**
     * Sets the message label based on the results of the filtering.
     */
    private void setMessageLbl() {
        String appointment;
        int results = appointmentList.size();

        if(results == 1) {
            appointment = " " + messages.getString("report.apptSingular") + " ";
        } else {
            appointment = " " + messages.getString("report.apptPlural") + " ";
        }

        if(reportTypeComboBox.getValue().equals(messages.getString("report.month"))) {
            messageLbl.setText(results + " " + filter1ComboBox.getValue() + appointment +
                    messages.getString("report.foundIn") + " " + filter2ComboBox.getValue() + ".");
        } else if(reportTypeComboBox.getValue().equals(messages.getString("report.contact"))) {
            messageLbl.setText(results + appointment + messages.getString("report.foundFor") + " " +
                    filter1ComboBox.getValue() + ".");
        } else if(reportTypeComboBox.getValue().equals(messages.getString("report.country"))) {
            messageLbl.setText(results + appointment + messages.getString("report.foundFor") + " " +
                    filter1ComboBox.getValue() + ".");
        }
    }

    /**
     * Sets the appointment list for the table view.
     * @param appointmentList The list of appointments to display.
     */
    private void setAppointmentList(ObservableList<Appointment> appointmentList) {
        this.appointmentList.clear();
        this.appointmentList = appointmentList;
    }

    /**
     * Populates the table view with the appointment list data.
     */
    private void populateTable() {
        if(appointmentList != null) {
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
    }

    /**
     * Handles the logoff action, navigating to the login screen and resetting currentUser
     * @param event The ActionEvent triggering the method.
     * @throws IOException if there is an error loading the scene.
     */
    @FXML
    private void onActionLogoff(ActionEvent event) throws IOException {
        NavigateToScene.goToScene(event, "LoginScreen", null);
    }

    /**
     * Handles the action to show all appointments.
     * @param event The ActionEvent triggering the method.
     * @throws IOException if there is an error loading the scene.
     */
    @FXML
    private void onActionShowAll(ActionEvent event) throws IOException {
        NavigateToScene.goToMainMenu(event, "all", false, currentUser);
    }

    /**
     * Handles the action to show appointments for the current month.
     * @param event The ActionEvent triggering the method.
     * @throws IOException if there is an error loading the scene.
     */
    @FXML
    private void onActionShowMonth(ActionEvent event) throws IOException {
        NavigateToScene.goToMainMenu(event, "month", false, currentUser);
    }

    /**
     * Handles the action to show appointments for the current week.
     * @param event The ActionEvent triggering the method.
     * @throws IOException if there is an error loading the scene.
     */
    @FXML
    private void onActionShowWeek(ActionEvent event) throws IOException {
        NavigateToScene.goToMainMenu(event, "week", false, currentUser);
    }

    /**
     * Handles the action to view customers, navigating to the customer menu.
     * @param event The ActionEvent triggering the method.
     * @throws IOException if there is an error loading the scene.
     */
    @FXML
    private void onActionViewCustomers(ActionEvent event) throws IOException {
        NavigateToScene.goToScene(event, "CustomerMenu", currentUser);
    }
}
