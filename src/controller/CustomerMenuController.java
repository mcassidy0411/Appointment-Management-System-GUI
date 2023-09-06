package com.mc.controller;

import com.mc.helper.JDBCQuery;
import com.mc.helper.NavigateToScene;
import com.mc.helper.NotifyUser;
import com.mc.model.CurrentUser;
import com.mc.model.Customer;
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
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * A controller class for managing the customer menu UI.
 * Implements the SharedControllerInterface to support currentUser handling.
 * @author Michael Cassidy
 */
public class CustomerMenuController implements SharedControllerInterface {
    @FXML
    private Button addCustBtn;
    @FXML
    private TableColumn<Customer, String> addressCol;
    @FXML
    private RadioButton apptAllBtn;
    @FXML
    private RadioButton apptMonthBtn;
    @FXML
    private ToggleGroup apptTglGrp;
    @FXML
    private RadioButton apptWeekBtn;
    @FXML
    private Label customerLbl;
    @FXML
    private TableView<Customer> customerTbl;
    @FXML
    private Button deleteCustBtn;
    @FXML
    private TableColumn<Customer, Integer> idCol;
    @FXML
    private TableColumn<Customer, String> postalCodeCol;
    @FXML
    private Button logoffBtn;
    @FXML
    private Button modifyCustBtn;
    @FXML
    private TableColumn<Customer, String> nameCol;
    @FXML
    private TableColumn<Customer, Integer> divisionCol;
    @FXML
    private Label timeZoneLbl;
    @FXML
    private TableColumn<Customer, String> phoneCol;
    @FXML
    private RadioButton viewCustomersBtn;
    @FXML
    private TableColumn<Customer, String> countryCol;
    @FXML
    private RadioButton viewReportsBtn;

    private final ObservableList<Customer> customerList = FXCollections.observableArrayList();

    private CurrentUser currentUser;

    /**
     * Sets the current user for this controller.
     *
     * @param currentUser The CurrentUser object representing the logged-in user.
     */
    @Override
    public void setCurrentUser(CurrentUser currentUser) { this.currentUser = currentUser; }

    /**
     * Initializes the controller, sets the language and populates the table.
     */
    @FXML
    private void initialize() {
        setLanguage();
        populateTable();
    }

    /**
     * Sets the language for the UI elements based on the user's locale.
     */
    private void setLanguage() {
        Locale userLocale = Locale.getDefault();
        ZoneId timeZone = ZoneId.systemDefault();

        ResourceBundle messages = ResourceBundle.getBundle("LanguageBundle", userLocale);
        customerLbl.setText(messages.getString("cust.customer"));
        apptAllBtn.setText(messages.getString("main.viewAll"));
        apptMonthBtn.setText(messages.getString("main.viewMonth"));
        apptWeekBtn.setText(messages.getString("main.viewWeek"));
        viewCustomersBtn.setText(messages.getString("main.viewCustomers"));
        viewReportsBtn.setText(messages.getString("main.viewReports"));
        timeZoneLbl.setText(messages.getString("login.timezone") + " : " + timeZone);
        idCol.setText(messages.getString("main.customerId"));
        nameCol.setText(messages.getString("cust.name"));
        addressCol.setText(messages.getString("cust.address"));
        divisionCol.setText(messages.getString("cust.division"));
        postalCodeCol.setText(messages.getString("cust.postalCode"));
        countryCol.setText(messages.getString("cust.country"));
        phoneCol.setText(messages.getString("cust.phone"));
        addCustBtn.setText(messages.getString("cust.addCust"));
        modifyCustBtn.setText(messages.getString("cust.modCust"));
        deleteCustBtn.setText(messages.getString("cust.delCust"));
        logoffBtn.setText(messages.getString("main.logoff"));
    }

    /**
     * Populates the customer table with data from the customerList.
     */
    private void populateTable() {
        customerList.clear();
        customerList.setAll(JDBCQuery.getAllCustomerList());
        customerTbl.setItems(customerList);

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        divisionCol.setCellValueFactory(new PropertyValueFactory<>("division"));
        postalCodeCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        countryCol.setCellValueFactory(new PropertyValueFactory<>("country"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));

        customerTbl.getSortOrder().add(idCol);
        customerTbl.autosize();
    }

    /**
     * Navigates to the AddCustomer scene.
     *
     * @param event The ActionEvent associated with the button click
     * @throws IOException If an error occurs while loading the scene
     */
    @FXML
    private void onActionAddCustomer(ActionEvent event) throws IOException {
        NavigateToScene.goToScene(event, "AddCustomer", currentUser);
    }

    /**
     * Deletes the selected customer from the database and updates the table.
     * Notifies user if no customer is selected.
     *
     * @param event The ActionEvent associated with the button click
     * @throws IOException If an error occurs while loading the scene
     */
    @FXML
    private void onActionDeleteCustomer(ActionEvent event) throws IOException {
        Customer selectedCustomer = customerTbl.getSelectionModel().getSelectedItem();
        if (selectedCustomer != null) {
            if(NotifyUser.confirmDeleteObject(selectedCustomer)) {
                JDBCQuery.deleteCustomer(selectedCustomer.getId());
                populateTable();
                NotifyUser.objectDeleted(selectedCustomer);
            }
        } else {
            NotifyUser.nothingSelected("customer");
        }
    }

    /**
     * Logs user off by navigating to the LoginScreen scene and passing 'null' as the current user.
     *
     * @param event The ActionEvent associated with the button click
     * @throws IOException If an error occurs while loading the scene
     */
    @FXML
    private void onActionLogoff(ActionEvent event) throws IOException {
        NavigateToScene.goToScene(event, "LoginScreen", null);
    }

    /**
     * Navigates to the ModifyCustomer scene with the selected customer.
     * Notifies user if no customer is selected.
     *
     * @param event The ActionEvent associated with the button click
     * @throws IOException If an error occurs while loading the scene
     */
    @FXML
    private void onActionModifyCustomer(ActionEvent event) throws IOException {
        Customer selectedCustomer = customerTbl.getSelectionModel().getSelectedItem();
        if (selectedCustomer != null) {
            NavigateToScene.goToScene(event, "ModifyCustomer", selectedCustomer, currentUser);
        } else {
            NotifyUser.nothingSelected("customer");
        }
    }

    /**
     * Navigates to the main menu scene with the "all" filter applied.
     *
     * @param event The ActionEvent associated with the button click
     * @throws IOException If an error occurs while loading the scene
     */
    @FXML
    private void onActionShowAll(ActionEvent event) throws IOException {
        NavigateToScene.goToMainMenu(event,"all", false, currentUser);
    }

    /**
     * Navigates to the main menu scene with the "month" filter applied.
     *
     * @param event The ActionEvent associated with the button click
     * @throws IOException If an error occurs while loading the scene
     */
    @FXML
    private void onActionShowMonth(ActionEvent event) throws IOException {
        NavigateToScene.goToMainMenu(event, "month", false, currentUser);
    }


    /**
     * Navigates to the main menu scene with the "week" filter applied.
     *
     * @param event The ActionEvent associated with the button click
     * @throws IOException If an error occurs while loading the scene
     */
    @FXML
    private void onActionShowWeek(ActionEvent event) throws IOException {
        NavigateToScene.goToMainMenu(event, "week", false, currentUser);
    }

    /**
     * Navigates to the ReportMenu scene.
     *
     * @param event The ActionEvent associated with the button click
     * @throws IOException If an error occurs while loading the scene
     */
    @FXML
    private void onActionViewReports(ActionEvent event) throws IOException {
        NavigateToScene.goToScene(event, "ReportMenu", currentUser);
    }

}
