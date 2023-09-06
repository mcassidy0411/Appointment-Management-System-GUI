package com.mc.helper;

import com.mc.model.Appointment;
import com.mc.model.Customer;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Abstract class containing static methods for displaying different types of alert dialogs to the user.
 * @author Michael Cassidy
 */
public abstract class NotifyUser {
    private static final Locale userLocale = Locale.getDefault();
    private static final ResourceBundle messages = ResourceBundle.getBundle("LanguageBundle", userLocale);

    /**
     * Displays a warning alert dialog with the specified title and content.
     *
     * @param title The title of the warning alert dialog.
     * @param content The content of the warning alert dialog.
     */
    private static void warningPanel(String title, String content) {
        ButtonType okButton = new ButtonType(messages.getString("notify.ok"), ButtonBar.ButtonData.OK_DONE);
        Alert warningAlert = new Alert(Alert.AlertType.WARNING);
        warningAlert.getDialogPane().getButtonTypes().remove(ButtonType.CANCEL);
        warningAlert.getButtonTypes().setAll(okButton);
        warningAlert.setTitle(title);
        warningAlert.setHeaderText(null);
        warningAlert.setContentText(content);

        Label contentLabel = (Label) warningAlert.getDialogPane().lookup(".content");
        contentLabel.setWrapText(true);
        contentLabel.setMaxWidth(Region.USE_COMPUTED_SIZE);
        contentLabel.setMaxHeight(Region.USE_COMPUTED_SIZE);

        warningAlert.showAndWait();
    }

    /**
     * Displays an information alert dialog with the specified title and content.
     *
     * @param title The title of the information alert dialog.
     * @param content The content of the information alert dialog.
     */
    private static void informationPanel(String title, String content) {
        ButtonType okButton = new ButtonType(messages.getString("notify.ok"), ButtonBar.ButtonData.OK_DONE);
        Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
        informationAlert.getDialogPane().getButtonTypes().remove(ButtonType.CANCEL);
        informationAlert.getButtonTypes().setAll(okButton);
        informationAlert.setTitle(title);
        informationAlert.setHeaderText(null);
        informationAlert.setContentText(content);
        Label contentLabel = (Label) informationAlert.getDialogPane().lookup(".content");
        contentLabel.setWrapText(true);
        contentLabel.setMaxWidth(Region.USE_COMPUTED_SIZE);
        contentLabel.setMaxHeight(Region.USE_COMPUTED_SIZE);
        informationAlert.showAndWait();
    }

    /**
     * Displays a confirmation alert dialog with the specified title and content.
     *
     * @param title The title of the confirmation alert dialog.
     * @param content The content of the confirmation alert dialog.
     * @return true if the user clicks the 'Yes' button, false otherwise.
     */
    private static boolean confirmationPanel(String title, String content) {
        ButtonType yesButton = new ButtonType(messages.getString("notify.yes"), ButtonBar.ButtonData.OK_DONE);
        ButtonType noButton = new ButtonType(messages.getString("notify.no"), ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.getDialogPane();
        confirmationAlert.getButtonTypes().setAll(yesButton, noButton);
        confirmationAlert.setTitle(title);
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText(content);

        Label contentLabel = (Label) confirmationAlert.getDialogPane().lookup(".content");
        contentLabel.setWrapText(true);
        contentLabel.setMaxWidth(Region.USE_COMPUTED_SIZE);
        contentLabel.setMaxHeight(Region.USE_COMPUTED_SIZE);

        int numberOfLines = content.split("\n").length;
        double lineHeight = contentLabel.getFont().getSize();
        double heightPadding = 30; // Add some padding to accommodate other elements in the Alert dialog

        contentLabel.setMinHeight(lineHeight * numberOfLines + heightPadding);

        Optional<ButtonType> result = confirmationAlert.showAndWait();

        return result.isPresent() && result.get() == yesButton;
    }

    /**
     * Displays a warning alert dialog when the user fails to log in.
     */
    public static void unableToLogin() {
        warningPanel(messages.getString("notify.loginFailedTitle"), messages.getString("notify.loginFailedBody"));
    }

    /**
     * Displays a warning alert dialog when no item is selected in the specified type.
     *
     * @param type The type of item not selected ("appointment" or "customer").
     */
    public static void nothingSelected(String type) {
        if(type.equals("appointment")) {
            warningPanel(messages.getString("notify.noApptSelected"), messages.getString("notify.pleaseSelectAppt"));
        } else if(type.equals("customer")) {
            warningPanel(messages.getString("notify.noCustSelected"), messages.getString("notify.pleaseSelectCust"));
        }
    }

    /**
     * Checks for upcoming appointments within the next 15 minutes in the provided appointment list.
     * If an appointment is found, an information alert dialog is displayed with details of the upcoming appointment.
     * If no appointments are found within the next 15 minutes, an information alert dialog is displayed, indicating
     * that there are no upcoming appointments.
     * Lambda expression of forEach used in this method (appointmentList.forEach(appointment ->) which allows for
     * more concise code and doesn't require an explicit loop.
     *
     * @param appointmentList The list of appointments to be checked.
     */
    public static void upcomingAppointments(ObservableList<Appointment> appointmentList) {
        AtomicBoolean noUpcomingAppointments = new AtomicBoolean(true);
        appointmentList.forEach(appointment -> {
            Timestamp appointmentStart = appointment.getStartTimestamp();
            Timestamp currentTime = TimeUtils.convertToUtcTimestamp(LocalDate.now(), LocalTime.now());
            if(TimeUtils.isWithinFifteenMinutes(appointmentStart, currentTime)) {
                noUpcomingAppointments.set(false);
                String content = messages.getString("notify.upcomingAppt") + " #" + appointment.getId() + " " +
                        messages.getString("notify.at") + " " + appointment.getStartDate() + ", " +
                        appointment.getStartTime();
                informationPanel(messages.getString("notify.upcomingAppt"), content);
            }
        });

        if(noUpcomingAppointments.get()) {
            informationPanel(messages.getString("notify.noUpcomingAppt"),
                    messages.getString("notify.noApptScheduled"));
        }
    }

    /**
     * Displays a warning panel for a null input, notifying the user that the input cannot be empty.
     *
     * @param body The specific field that cannot be empty.
     */
    public static void nullInput(String body) {
        warningPanel(messages.getString("notify.cannotBeEmptyTitle"),
                messages.getString("notify.cannotBeEmptyBody") + "\n" + body);
    }

    /**
     * Displays a warning panel, notifying the user that an appointment is scheduled outside business hours.
     */
    public static void outsideBusinessHours() {
        ZoneId userTimeZone = ZoneId.systemDefault();
        LocalTime startTimeEst = TimeUtils.getEstTimeInLocalTime(8, 0);
        LocalTime endTimeEst = TimeUtils.getEstTimeInLocalTime(22, 0);
        LocalTime startTimeLocal = TimeUtils.convertEstToLocalTime(startTimeEst);
        LocalTime endTimeLocal = TimeUtils.convertEstToLocalTime(endTimeEst);
        String body = messages.getString("notify.outsideBusinessHoursBody") + " " + startTimeLocal + " " +
                messages.getString("notify.and") + " " + endTimeLocal + " " +
                messages.getString("notify.outsideBusinessHoursTimeZone") + " " + userTimeZone + ".";

        warningPanel(messages.getString("notify.outsideBusinessHoursTitle"), body);
    }

    /**
     * Displays a warning panel, notifying the user that the appointment duration is invalid.
     */
    public static void invalidAppointmentDuration() {
        warningPanel(messages.getString("notify.appointmentDurationTitle"),
                messages.getString("notify.appointmentDurationBody"));
    }

    /**
     * Displays a warning panel, notifying the user that the appointment date is in the past.
     */
    public static void pastAppointmentDates() {
        warningPanel(messages.getString("notify.appointmentDateInPastTitle"),
                messages.getString("notify.appointmentDateInPastBody"));
    }

    /**
     * Displays a warning panel, notifying the user that there is an overlapping appointment.
     *
     * @param appointment The appointment object that overlaps with another appointment.
     */
    public static void overlappingAppointment(Appointment appointment) {
        int id = appointment.getId();
        Timestamp startTimestamp = appointment.getStartTimestamp();
        Timestamp endTimestamp = appointment.getEndTimestamp();
        LocalTime localStartTime = TimeUtils.getTimeFromTimestamp(startTimestamp);
        LocalTime localEndTime = TimeUtils.getTimeFromTimestamp(endTimestamp);
        String body = messages.getString("notify.overlappingAppointmentBody") + " " + id + ", " + localStartTime +
                "-" + localEndTime + " " + messages.getString("notify.localTime") + ".";

        warningPanel(messages.getString("notify.overlappingAppointmentTitle"), body);
    }

    /**
     * Displays a confirmation panel, notifying the user that no changes were made and asks the user if they want to
     * return to the menu.
     *
     * @return true if the user wants to return to the menu, false otherwise.
     */
    public static boolean noChangesMade() {
       return confirmationPanel(messages.getString("notify.noChangesTitle"),
               messages.getString("notify.noChangesBody"));
    }

    /**
     * Displays an information panel, notifying the user that an object has been updated.
     *
     * @param object The object that has been updated.
     */
    public static void objectUpdated(Object object) {
        int id;
        StringBuilder title = new StringBuilder(messages.getString("notify.saved"));
        StringBuilder body = new StringBuilder(messages.getString("notify.saved").toLowerCase() + ".");
        if(object instanceof Appointment) {
            title.insert(0, messages.getString("notify.appointment") + " ");
            id = ((Appointment) object).getId();
            body.insert(0, messages.getString("notify.appointment") + " " + id + " ");
        } else if(object instanceof Customer) {
            title.insert(0, messages.getString("notify.customer") + " ");
            id = ((Customer) object).getId();
            body.insert(0, messages.getString("notify.customer") + " " + id + " ");
        }

        informationPanel(title.toString(), body.toString());
    }

    /**
     * Displays an information panel, notifying the user that an object has been saved.
     *
     * @param object The object that has been saved.
     */
    public static void objectSaved(Object object) {
        StringBuilder title = new StringBuilder(messages.getString("notify.saved"));
        StringBuilder body = new StringBuilder(messages.getString("notify.saved").toLowerCase() + ".");
        if(object instanceof Appointment) {
            title.insert(0, messages.getString("notify.appointment") + " ");
            body.insert(0, messages.getString("notify.appointment") + " ");
        } else if(object instanceof Customer) {
            title.insert(0, messages.getString("notify.customer") + " ");
            body.insert(0, messages.getString("notify.customer") + " ");
        }

        informationPanel(title.toString(), body.toString());
    }

    /**
     * Displays a confirmation panel, asking the user if they want to delete an object (Appointment or Customer).
     *
     * @param object The object to be deleted.
     * @return true if the user wants to delete the object, false otherwise.
     */
    public static boolean confirmDeleteObject(Object object) {
        if(object instanceof Appointment appointment) {
            int appointmentId = appointment.getId();
            String appointmentType = appointment.getType();
            String body = messages.getString("notify.deleteAppointmentBody") + " " + appointmentId + " - " +
                    appointmentType + "?";

            return confirmationPanel(messages.getString("notify.deleteAppointmentTitle"), body);
        } else if(object instanceof Customer customer) {
            int customerId = customer.getId();
            ObservableList<Appointment> appointmentList = JDBCQuery.getAppointmentListByCustomerId(customerId);
            StringBuilder body = new StringBuilder();

            if(appointmentList.size() > 0) {
                body.append(messages.getString("notify.appointmentsFound")).append(" ").append(customerId)
                        .append(":\n");

                for (Appointment a : appointmentList) {
                    body.append(messages.getString("main.id")).append(" ").append(a.getId()).append(" - ")
                            .append(a.getType()).append("\n");
                }

                body.append("\n").append(messages.getString("notify.confirmDeleteCustomer")).append(" ")
                        .append(messages.getString("notify.confirmDeleteAssociatedAppts")).append("?");
            } else {
                body.append(messages.getString("notify.confirmDeleteCustomer")).append("?");
            }

            String finalBodyString = body.toString();

            return confirmationPanel(messages.getString("notify.deleteCustomerTitle"), finalBodyString);
        }
        return false;
    }

    /**
     * Displays an information panel, notifying the user that an object has been deleted.
     *
     * @param object The object that has been deleted.
     */
    public static void objectDeleted(Object object) {
        if(object instanceof Appointment appointment) {
            int appointmentId = appointment.getId();
            String appointmentType = appointment.getType();
            String body = messages.getString("main.id") + " " + appointmentId + " - " + appointmentType + " " +
                    messages.getString("notify.wasDeleted");

            informationPanel(messages.getString("notify.appointmentDeletedTitle"), body);
        } else if(object instanceof Customer customer) {
            int customerId = customer.getId();
            String customerName = customer.getName();
            String body = messages.getString("main.customerId") + " " + customerId + " - " + customerName + " " +
                    messages.getString("notify.wasDeleted");

            informationPanel(messages.getString("notify.customerDeletedTitle"), body);
        }
    }

}
