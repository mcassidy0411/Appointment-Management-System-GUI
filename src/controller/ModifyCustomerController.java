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
 * Controller for the Modify Customer view.
 * Allows the user to modify existing customer details.
 * Extends BaseCustomerController class for shared functionality between this class and AddCustomerController
 * @author Michael Cassidy
 */
public class ModifyCustomerController extends BaseCustomerController {
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
    private Customer customer;

    /**
     * Initializes the ModifyCustomerController.
     * Sets up labels and combo boxes.
     */
    @FXML
    private void initialize() {
        setLabels("mod", headerLbl, custIdLbl, nameLbl, addressLbl, countryLbl, divisionLbl, postalCodeLbl,
                phoneLbl, custIdField, saveBtn, cancelBtn);
        countryComboBox.setItems(getCountryComboBox().getItems());
        setCountryListener(countryComboBox, divisionComboBox);
    }

    /**
     * Sets the customer to be modified.
     *
     * @param customer The customer to be modified
     */
    public void setCustomer(Customer customer) { this.customer = customer; }

    /**
     * Sets the fields in the form with the customer's data.
     */
    public void setFields() {
        custIdField.setText(Integer.toString(customer.getId()));
        nameField.setText(customer.getName());
        addressField.setText(customer.getAddress());
        postCodeField.setText(customer.getPostalCode());
        phoneField.setText(customer.getPhone());
        countryComboBox.setValue(customer.getCountry());
        divisionComboBox.setValue(customer.getDivision());
    }

    /**
     * Creates a modified customer with the updated information from the form.
     *
     * @return A new Customer object with the updated information
     */
    private Customer makeModifiedCustomer() {
        int id = customer.getId();
        String name = nameField.getText();
        String address = addressField.getText();
        String postalCode = postCodeField.getText();
        String phone = phoneField.getText();
        Timestamp createdTimestamp = customer.getCreatedTimestamp();
        String createdBy = customer.getCreatedBy();
        Timestamp updatedTimestamp = TimeUtils.convertToUtcTimestamp(LocalDate.now(), LocalTime.now());
        String updatedBy = currentUser.getUsername();
        int divisionId = JDBCQuery.getDivisionId(divisionComboBox.getValue());

        return new Customer(id, name, address, postalCode, phone, createdTimestamp, createdBy, updatedTimestamp,
                updatedBy, divisionId);
    }

    /**
     * Validates the form and saves the changes to the database.
     * Navigates back to the Customer Menu.
     *
     * @param event The ActionEvent that triggered the method
     * @throws IOException If there's an issue loading the FXML file
     */
    @FXML
    private void onActionSave(ActionEvent event) throws IOException {
        if(validateFields(nameField, addressField, countryComboBox, divisionComboBox, postCodeField, phoneField)) {
            Customer modifiedCustomer = makeModifiedCustomer();
            NotifyUser.objectUpdated(modifiedCustomer);
            JDBCQuery.updateCustomer(modifiedCustomer);

            NavigateToScene.goToScene(event, "CustomerMenu", currentUser);
        }
    }
}
