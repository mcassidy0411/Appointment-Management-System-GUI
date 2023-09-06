package com.mc.controller;

import com.mc.helper.JDBCQuery;
import com.mc.helper.NavigateToScene;
import com.mc.helper.NotifyUser;
import com.mc.helper.TimeUtils;
import com.mc.model.Customer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * AddCustomerController is a JavaFX controller responsible for handling the process of creating a new customer.
 * Extends BaseCustomerController class for shared functionality between this class and ModifyCustomerController
 * @author Michael Cassidy
 */
public class AddCustomerController extends BaseCustomerController {
    @FXML
    private TextField addressField;
    @FXML
    private Label addressLbl;
    @FXML
    private Button cancelBtn;
    @FXML
    private ComboBox<String> countryComboBox;
    @FXML
    private Label countryLbl;
    @FXML
    private TextField custIdField;
    @FXML
    private Label custIdLbl;
    @FXML
    private ComboBox<String> divisionComboBox;
    @FXML
    private Label divisionLbl;
    @FXML
    private Label headerLbl;
    @FXML
    private TextField nameField;
    @FXML
    private Label nameLbl;
    @FXML
    private TextField postCodeField;
    @FXML
    private Label postalCodeLbl;
    @FXML
    private Button saveBtn;
    @FXML
    private Label phoneLbl;
    @FXML
    private TextField phoneField;

    /**
     * Initializes the AddCountryController by setting up UI components and listeners.
     */
    @FXML
    private void initialize() {
        setLabels("add", headerLbl, custIdLbl, nameLbl, addressLbl, countryLbl, divisionLbl, postalCodeLbl,
                phoneLbl, custIdField, saveBtn, cancelBtn);
        countryComboBox.setItems(getCountryComboBox().getItems());
        setCountryListener(countryComboBox, divisionComboBox);
    }

    /**
     * Handles the action of saving a new customer.
     * @param event the action event
     * @throws IOException if there is an error while navigating to the customer menu
     */
    @FXML
    private void onActionSave(ActionEvent event) throws IOException {
        if(validateFields(nameField, addressField, countryComboBox, divisionComboBox, postCodeField, phoneField)) {
            String name = nameField.getText();
            String address = addressField.getText();
            String postalCode = postCodeField.getText();
            String phone = phoneField.getText();
            Timestamp createdTimestamp = TimeUtils.convertToUtcTimestamp(LocalDate.now(), LocalTime.now());
            String createdBy = currentUser.getUsername();
            int divisionId = JDBCQuery.getDivisionId(divisionComboBox.getValue());

            Customer customer = new Customer(name, address, postalCode, phone, createdTimestamp, createdBy, createdTimestamp,
                    createdBy, divisionId);

            NotifyUser.objectSaved(customer);
            JDBCQuery.addCustomer(customer);
            NavigateToScene.goToScene(event, "CustomerMenu", currentUser);
        }

    }

}
