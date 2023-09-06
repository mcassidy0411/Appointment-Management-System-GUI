package com.mc.helper;

import com.mc.controller.MainMenuController;
import com.mc.controller.ModifyAppointmentController;
import com.mc.controller.ModifyCustomerController;
import com.mc.controller.SharedControllerInterface;
import com.mc.model.Appointment;
import com.mc.model.CurrentUser;
import com.mc.model.Customer;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Abstract class containing static methods for navigating between scenes in the application.
 * @author Michael Cassidy
 */
public abstract class NavigateToScene {

    /**
     * Navigates to the main menu scene with the specified view, sets the Current User and triggers the appointment
     * notifier if required.
     *
     * @param event The ActionEvent that triggered the scene change.
     * @param view The appointment view to display in the main menu scene.
     * @param triggerApptNotifier Indicates if the appointment notifier should be triggered.
     * @param currentUser The current user of the application.
     * @throws IOException If an error occurs while loading the FXML file.
     */
    public static void goToMainMenu(ActionEvent event, String view, boolean triggerApptNotifier,
                                    CurrentUser currentUser) throws IOException {
        FXMLLoader loader = new FXMLLoader(NavigateToScene.class.getResource("/com/mc/view/MainMenu.fxml"));

        MainMenuController controller = new MainMenuController();
        controller.setCurrentUser(currentUser);
        controller.setAppointmentView(view);
        loader.setControllerFactory(param -> controller);
        Parent newViewParent = loader.load();

        // Get the current stage
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Set the new view as the scene of the current stage
        Scene newViewScene = new Scene(newViewParent);
        stage.setScene(newViewScene);
        stage.show();
        if(triggerApptNotifier) { controller.notifyOnLogin(); }
    }

    /**
     * Navigates to the specified scene and sets the current user.
     *
     * @param event The ActionEvent that triggered the scene change.
     * @param fileName The name of the FXML file to load.
     * @param currentUser The current user of the application.
     * @throws IOException If an error occurs while loading the FXML file.
     */
    public static void goToScene(ActionEvent event, String fileName, CurrentUser currentUser) throws IOException {
        FXMLLoader loader = new FXMLLoader(NavigateToScene.class.getResource("/com/mc/view/" + fileName + ".fxml"));
        Parent newViewParent = loader.load();

        Object controller = loader.getController();
        if(controller instanceof SharedControllerInterface) {
            ((SharedControllerInterface)controller).setCurrentUser(currentUser);
        }

        // Get the current stage
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Set the new view as the scene of the current stage
        Scene newViewScene = new Scene(newViewParent);
        stage.setScene(newViewScene);
        stage.show();
    }

    /**
     * Navigates to the specified scene, sets the current user, and initializes the scene with the provided object.
     *
     * @param event The ActionEvent that triggered the scene change.
     * @param fileName The name of the FXML file to load.
     * @param object The object to be used for initializing the scene's controller.
     * @param currentUser The current user of the application.
     * @throws IOException If an error occurs while loading the FXML file.
     */
    public static void goToScene(ActionEvent event, String fileName, Object object, CurrentUser currentUser)
            throws IOException {
        FXMLLoader loader = new FXMLLoader(NavigateToScene.class.getResource("/com/mc/view/" + fileName + ".fxml"));
        Parent newViewParent = loader.load();

        if(object instanceof Appointment) {
            ModifyAppointmentController controller = loader.getController();
            controller.setCurrentUser(currentUser);
            controller.setAppointment((Appointment)object);
            controller.setFields();
        } else if(object instanceof Customer) {
            ModifyCustomerController controller = loader.getController();
            controller.setCurrentUser(currentUser);
            controller.setCustomer((Customer)object);
            controller.setFields();
        }
        // Get the current stage
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Set the new view as the scene of the current stage
        Scene newViewScene = new Scene(newViewParent);
        stage.setScene(newViewScene);
        stage.show();
    }
}
