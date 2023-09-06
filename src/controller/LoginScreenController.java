package com.mc.controller;

import com.mc.helper.JDBCConnector;
import com.mc.helper.NavigateToScene;
import com.mc.helper.NotifyUser;
import com.mc.model.CurrentUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Controller class for the LoginScreen scene.
 * Handles user login and navigation to the main menu.
 * Sets the currentUser on successful login.
 * @author Michael Cassidy
 */
public class LoginScreenController {
    @FXML
    private Button exitBtn;
    @FXML
    private Button loginBtn;
    @FXML
    private Label loginLbl;
    @FXML
    private PasswordField passwordFld;
    @FXML
    private Label passwordLbl;
    @FXML
    private Button resetBtn;
    @FXML
    private Label timezoneLbl;
    @FXML
    private TextField usernameFld;
    @FXML
    private Label usernameLbl;
    private static final Locale userLocale = Locale.getDefault();
    private static final ZoneId timeZone = ZoneId.systemDefault();

    /**
     * Initializes the LoginScreenController.
     * Sets up the UI elements according to the user's locale.
     */
    @FXML
    private void initialize() {
        ResourceBundle messages = ResourceBundle.getBundle("LanguageBundle", userLocale);
        loginLbl.setText(messages.getString("login.login"));
        loginBtn.setText(messages.getString("login.login"));
        passwordLbl.setText(messages.getString("login.password") + ":");
        usernameLbl.setText(messages.getString("login.username") + ":");
        timezoneLbl.setText(messages.getString("login.timezone") + " : " + timeZone);
        resetBtn.setText(messages.getString("login.reset"));
        exitBtn.setText(messages.getString("login.exit"));
    }

    /**
     * Authenticates the user.  Sets currentUser and navigates to the main menu if successful.
     *
     * @param event The ActionEvent associated with the button click
     */
    @FXML
    private void onActionLogin(ActionEvent event) {
        String usernameInput = usernameFld.getText();
        String passwordInput = passwordFld.getText();

        try (Statement statement = JDBCConnector.connection.createStatement()) {
            String query = "SELECT * FROM users;";
            ResultSet resultSet = statement.executeQuery(query);
            while(resultSet.next()) {
                String usernameDB = resultSet.getString("User_Name");
                String passwordDB = resultSet.getString("Password");

                if(usernameDB.equals(usernameInput) && passwordDB.equals(passwordInput)) {
                    CurrentUser currentUser = new CurrentUser(usernameInput);
                    NavigateToScene.goToMainMenu(event, "all", true, currentUser);
                    logActivity(usernameInput, true);
                    return;
                }
            }
            logActivity(usernameInput, false);
            NotifyUser.unableToLogin();
            onActionReset();
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Logs the user's login activity, including the timestamp and success status.
     *
     * @param userName The username of the user attempting to log in
     * @param loginSuccessful A boolean indicating whether the login was successful
     */
    private static void logActivity(String userName, boolean loginSuccessful) {
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
        try (FileWriter writer = new FileWriter("login_activity.txt", true)) {
                writer.write(userName + ", " + timestamp + ", " + timeZone + ", " + loginSuccessful + "\n");
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    /**
     * Resets the username and password input fields.
     */
    @FXML
    private void onActionReset() {
        usernameFld.setText(null);
        passwordFld.setText(null);
    }

    /**
     * Closes the application window.
     *
     * @param event The ActionEvent associated with the button click
     */
    @FXML
    private void onActionExit(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
