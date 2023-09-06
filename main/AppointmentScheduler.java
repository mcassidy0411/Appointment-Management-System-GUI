package com.mc.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.mc.helper.JDBCConnector;

import java.io.IOException;

/**
 * AppointmentScheduler class is the main entry point of the application.
 * Sets up the initial scene for the application.
 * Extends the JavaFX Application class.
 * @author Michael Cassidy
 */
public class AppointmentScheduler extends Application {
    /**
     * The start method sets up the initial scene for the application using the LoginScreen.fxml file.
     *
     * @param stage The primary stage for the JavaFX application.
     * @throws IOException If an error occurs while loading the FXML file.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppointmentScheduler.class.getResource("/com/mc/view/LoginScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    /**
     * The main method serves as the entry point for the application.
     * It opens the JDBC connection, launches the JavaFX application, and closes the JDBC connection.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        JDBCConnector.openConnection();
        launch();
        JDBCConnector.closeConnection();
    }
}