package com.mc.controller;

import com.mc.helper.JDBCQuery;
import com.mc.helper.NavigateToScene;
import com.mc.helper.NotifyUser;
import com.mc.model.CurrentUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Abstract BaseCustomerController class provides common functionalities for
 * the AddCustomerController and the ModifyCustomerController.
 * Implements the SharedControllerInterface to support currentUser handling.
 * @author Michael Cassidy
 */
public abstract class BaseCustomerController implements SharedControllerInterface {
    protected CurrentUser currentUser;
    Locale userLocale = Locale.getDefault();
    ResourceBundle messages = ResourceBundle.getBundle("LanguageBundle", userLocale);

    /**
     * Sets the current user for the controller.
     *
     * @param currentUser the current user
     */
    @Override
    public void setCurrentUser(CurrentUser currentUser) { this.currentUser = currentUser; }

    /**
     * Returns a ComboBox containing a list of countries.
     *
     * @return ComboBox containing country names
     */
    protected static ComboBox<String> getCountryComboBox() {
        ComboBox<String> countryComboBox = new ComboBox<>();
        countryComboBox.setItems(JDBCQuery.getCountryList());
        return countryComboBox;
    }

    /**
     * Sets a listener for the country ComboBox to update the division ComboBox based on the selected country.
     *
     * @param countryComboBox the ComboBox containing country names
     * @param divisionComboBox the ComboBox containing division names
     */
    protected static void setCountryListener(ComboBox<String> countryComboBox, ComboBox<String> divisionComboBox) {
        countryComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
                divisionComboBox.setItems(JDBCQuery.getDivisionList(JDBCQuery.getCountryId(newValue)));
        });
    }

    /**
     * Sets the text for labels, buttons, and text fields based on the given mode ("add" or "mod").  Modes add or modify
     * customers, respectively.
     *
     * @param addMod a string indicating the mode
     * @param headerLbl the header label
     * @param custIdLbl the customer ID label
     * @param nameLbl the name label
     * @param addressLbl the address label
     * @param countryLbl the country label
     * @param divisionLbl the division label
     * @param postalCodeLbl the postal code label
     * @param phoneLbl the phone label
     * @param custIdField the customer ID text field
     * @param saveBtn the save button
     * @param cancelBtn the cancel button
     */
    protected void setLabels(String addMod, Label headerLbl, Label custIdLbl, Label nameLbl, Label addressLbl,
                             Label countryLbl, Label divisionLbl, Label postalCodeLbl, Label phoneLbl,
                             TextField custIdField, Button saveBtn, Button cancelBtn) {
        if(addMod.equals("add")) {
            headerLbl.setText(messages.getString("cust.addCust"));
            custIdField.setText(messages.getString("appt.autoGen"));
        } else if(addMod.equals("mod")) {
            headerLbl.setText(messages.getString("cust.modCust"));
        }

        custIdLbl.setText(messages.getString("appt.customerId"));
        nameLbl.setText(messages.getString("cust.name"));
        addressLbl.setText(messages.getString("cust.address"));
        countryLbl.setText(messages.getString("cust.country"));
        divisionLbl.setText(messages.getString("cust.division"));
        postalCodeLbl.setText(messages.getString("cust.postalCode"));
        phoneLbl.setText(messages.getString("cust.phone"));
        cancelBtn.setText(messages.getString("appt.cancel"));
        saveBtn.setText(messages.getString("appt.save"));
    }

    /**
     * Handles the cancel button action event.
     *
     * @param event the action event
     * @throws IOException if an error occurs navigating to the Customer menu
     */
    @FXML
    private void onActionCancel(ActionEvent event) throws IOException {
        NavigateToScene.goToScene(event, "CustomerMenu", currentUser);
    }

    /**
     * Validates input fields and returns a boolean indicating whether the fields are valid.
     *
     * @param nameField the name text field
     * @param addressField the address text field
     * @param countryComboBox the country ComboBox
     * @param divisionComboBox the division ComboBox
     * @param postalCodeField the postal code text field
     * @param phoneField the phone text field
     * @return true if all fields are valid, false otherwise
     */
    protected boolean validateFields(TextField nameField, TextField addressField, ComboBox<String> countryComboBox,
                                  ComboBox<String> divisionComboBox, TextField postalCodeField, TextField phoneField) {
        StringBuilder body = new StringBuilder();
        if(nameField.getText() == null || nameField.getText().isEmpty()) {
            body.append(messages.getString("cust.name")).append("\n");
        }
        if(addressField.getText() == null || addressField.getText().isEmpty()) {
            body.append(messages.getString("cust.address")).append("\n");
        }
        if(countryComboBox.getValue() == null) {
            body.append(messages.getString("cust.country")).append("\n");
        }
        if(divisionComboBox.getValue() == null) {
            body.append(messages.getString("cust.division")).append("\n");
        }
        if(postalCodeField.getText() == null || postalCodeField.getText().isEmpty()) {
            body.append(messages.getString("cust.postalCode")).append("\n");
        }
        if(phoneField.getText() == null || phoneField.getText().isEmpty()) {
            body.append(messages.getString("cust.phone")).append("\n");
        }

        if(body.isEmpty()) {
            return true;
        } else {
            String finalBodyString = body.toString();
            NotifyUser.nullInput(finalBodyString);
            return false;
        }
    }
}
