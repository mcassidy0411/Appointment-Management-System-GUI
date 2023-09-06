package com.mc.helper;

import com.mc.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Abstract class that queries a connected MySQl Database and returns the specified data.  Extends JDBCConnector.
 * @author Michael Cassidy
 */
public abstract class JDBCQuery extends JDBCConnector {
    /**
     * Retrieves all appointments from the database and returns them as an ObservableList of Appointment objects.
     *
     * @return an ObservableList of all Appointment objects.
     */
    public static ObservableList<Appointment> getAllAppointmentList() {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

        try(Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT * FROM appointments;");
            // Loop through the ResultSet and add each row to the list
            while (rs.next()) {
                Appointment appointment = new Appointment(
                        rs.getInt("Appointment_ID"),
                        rs.getString("Title"),
                        rs.getString("Description"),
                        rs.getString("Location"),
                        rs.getString("Type"),
                        rs.getTimestamp("Start"),
                        rs.getTimestamp("End"),
                        rs.getTimestamp("Create_Date"),
                        rs.getString("Created_By"),
                        rs.getTimestamp("Last_Update"),
                        rs.getString("Last_Updated_By"),
                        rs.getInt("Customer_ID"),
                        rs.getInt("User_ID"),
                        rs.getInt("Contact_ID"));
                appointmentList.add(appointment);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        return appointmentList;
    }

    /**
     * Retrieves appointments from the current week from the database and returns them as an ObservableList of
     * Appointment objects.
     *
     * @return an ObservableList of Appointment objects for the current week.
     */
    public static ObservableList<Appointment> getWeekAppointmentList() {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

        try(Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT * FROM client_schedule.appointments\n" +
                                                        "WHERE Start >= NOW() AND Start <= (NOW() + INTERVAL 6 DAY);");
            // Loop through the ResultSet and add each row to the list
            while (rs.next()) {
                Appointment appointment = new Appointment(
                        rs.getInt("Appointment_ID"),
                        rs.getString("Title"),
                        rs.getString("Description"),
                        rs.getString("Location"),
                        rs.getString("Type"),
                        rs.getTimestamp("Start"),
                        rs.getTimestamp("End"),
                        rs.getTimestamp("Create_Date"),
                        rs.getString("Created_By"),
                        rs.getTimestamp("Last_Update"),
                        rs.getString("Last_Updated_By"),
                        rs.getInt("Customer_ID"),
                        rs.getInt("User_ID"),
                        rs.getInt("Contact_ID"));
                appointmentList.add(appointment);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        return appointmentList;
    }

    /**
     * Retrieves appointments from the current month from the database and returns them as an ObservableList of
     * Appointment objects.
     *
     * @return an ObservableList of Appointment objects for the current month.
     */
    public static ObservableList<Appointment> getMonthAppointmentList() {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

        try(Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT * FROM appointments\n" +
                                                        "WHERE MONTH(Start) = MONTH(NOW());");
            // Loop through the ResultSet and add each row to the list
            while (rs.next()) {
                Appointment appointment = new Appointment(
                        rs.getInt("Appointment_ID"),
                        rs.getString("Title"),
                        rs.getString("Description"),
                        rs.getString("Location"),
                        rs.getString("Type"),
                        rs.getTimestamp("Start"),
                        rs.getTimestamp("End"),
                        rs.getTimestamp("Create_Date"),
                        rs.getString("Created_By"),
                        rs.getTimestamp("Last_Update"),
                        rs.getString("Last_Updated_By"),
                        rs.getInt("Customer_ID"),
                        rs.getInt("User_ID"),
                        rs.getInt("Contact_ID"));
                appointmentList.add(appointment);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        return appointmentList;
    }

    /**
     * Retrieves all contact names from the database and returns them as an ObservableList of String objects.
     *
     * @return an ObservableList of contact names.
     */
    public static ObservableList<String> getContactList() {
        ObservableList<String> contactList = FXCollections.observableArrayList();

        try(Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT Contact_Name FROM contacts;");
            // Loop through the ResultSet and add each row to the list
            while (rs.next()) {
                String contact = rs.getString("Contact_Name");
                contactList.add(contact);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        return contactList;
    }

    /**
     * Retrieves all customer IDs from the database and returns them as an ObservableList of Integer objects.
     *
     * @return an ObservableList of customer IDs.
     */
    public static ObservableList<Integer> getCustomerIdList() {
        ObservableList<Integer> customerIdList = FXCollections.observableArrayList();

        try(Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT Customer_ID FROM customers ORDER BY Customer_ID ASC;");
            // Loop through the ResultSet and add each row to the list
            while (rs.next()) {
                int customerId = rs.getInt("Customer_ID");
                customerIdList.add(customerId);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        return customerIdList;
    }

    /**
     * Retrieves the contact ID associated with the given contact name.
     *
     * @param contactName the name of the contact for which to retrieve the ID.
     * @return the contact ID associated with the given contact name, or -1 if not found.
     */
    public static int getContactId(String contactName) {
        String query = "SELECT Contact_ID FROM contacts WHERE Contact_Name = ?;";
        try(PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, contactName);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                return rs.getInt("Contact_ID");
            }
        } catch(SQLException e) {
            System.out.println(e);
        }
        return -1;
    }

    /**
     * Retrieves the contact name associated with the given contact ID.
     *
     * @param contactId the ID of the contact for which to retrieve the name.
     * @return the contact name associated with the given contact ID, or an empty string if not found.
     */
    public static String getContactName(int contactId) {
        String query = "SELECT Contact_Name FROM contacts WHERE Contact_ID = ?;";
        try(PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, contactId);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                return rs.getString("Contact_Name");
            }
        } catch(SQLException e) {
            System.out.println(e);
        }
        return "";
    }

    /**
     * Retrieves the user ID associated with the given user name.
     *
     * @param userName the name of the user for which to retrieve the ID.
     * @return the user ID associated with the given user name, or -1 if not found.
     */
    public static int getUserId(String userName) {
        String query = "SELECT User_ID FROM users WHERE User_Name = ?;";
        try(PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, userName);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                return rs.getInt("User_ID");
            }
        } catch(SQLException e) {
            System.out.println(e);
        }
        return -1;
    }

    /**
     * Retrieves the username associated with the given user ID.
     *
     * @param userId the ID of the user for which to retrieve the name.
     * @return the username associated with the given user ID, or an empty string if not found.
     */
    public static String getUserName(int userId) {
        String query = "SELECT User_Name FROM users WHERE User_ID = ?;";
        try(PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                return rs.getString("User_Name");
            }
        } catch(SQLException e) {
            System.out.println(e);
        }
        return "";
    }

    /**
     * Adds a new appointment to the database.
     *
     * @param appointment the Appointment object to be added to the database.
     */
    public static void addAppointment(Appointment appointment) {
        String query = "INSERT INTO appointments" +
                        "(Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, " +
                        "Last_Updated_By, Customer_ID, User_ID, Contact_ID)\n" +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try(PreparedStatement stmt = connection.prepareStatement(query);) {
            stmt.setString(1, appointment.getTitle());
            stmt.setString(2, appointment.getDescription());
            stmt.setString(3, appointment.getLocation());
            stmt.setString(4, appointment.getType());
            stmt.setTimestamp(5, appointment.getStartTimestamp());
            stmt.setTimestamp(6, appointment.getEndTimestamp());
            stmt.setTimestamp(7, appointment.getCreatedTimestamp());
            stmt.setString(8, appointment.getCreatedBy());
            stmt.setTimestamp(9, appointment.getUpdatedTimestamp());
            stmt.setString(10, appointment.getUpdatedBy());
            stmt.setInt(11, appointment.getCustomerId());
            stmt.setInt(12, appointment.getUserId());
            stmt.setInt(13, appointment.getContactId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    /**
     * Updates an existing appointment in the database.
     *
     * @param appointment the Appointment object containing the updated information.
     */
    public static void updateAppointment(Appointment appointment) {
        String query = "UPDATE appointments\n" +
                        "SET Title = ?," +
                        "Description = ?," +
                        "Location = ?," +
                        "Type = ?," +
                        "Start = ?," +
                        "End = ?," +
                        "Last_Update = ?," +
                        "Last_Updated_By = ?," +
                        "Customer_ID = ?," +
                        "User_ID = ?," +
                        "Contact_ID = ?\n" +
                        "WHERE Appointment_ID = ?;";

        try(PreparedStatement stmt = connection.prepareStatement(query);) {
            stmt.setString(1, appointment.getTitle());
            stmt.setString(2, appointment.getDescription());
            stmt.setString(3, appointment.getLocation());
            stmt.setString(4, appointment.getType());
            stmt.setTimestamp(5, appointment.getStartTimestamp());
            stmt.setTimestamp(6, appointment.getEndTimestamp());
            stmt.setTimestamp(7, appointment.getUpdatedTimestamp());
            stmt.setString(8, appointment.getUpdatedBy());
            stmt.setInt(9, appointment.getCustomerId());
            stmt.setInt(10, appointment.getUserId());
            stmt.setInt(11, appointment.getContactId());
            stmt.setInt(12, appointment.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    /**
     * Deletes an appointment from the database.
     *
     * @param appointment the Appointment object to be deleted from the database.
     */
    public static void deleteAppointment(Appointment appointment) {
        String query = "DELETE FROM appointments " +
                "WHERE Appointment_ID = ?";

        try(PreparedStatement stmt = connection.prepareStatement(query);) {
            stmt.setInt(1, appointment.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }


    /**
     * Retrieves all customers from the database and returns them as an ObservableList of Customer objects.
     *
     * @return an ObservableList of all Customer objects.
     */
    public static ObservableList<Customer> getAllCustomerList() {
        ObservableList<Customer> customerList = FXCollections.observableArrayList();

        try(Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT customers.*, first_level_divisions.Division, countries.Country FROM customers\n" +
                    "JOIN first_level_divisions ON customers.Division_ID = first_level_divisions.Division_ID\n" +
                    "JOIN countries ON first_level_divisions.Country_ID = countries.Country_ID;");
            // Loop through the ResultSet and add each row to the list
            while (rs.next()) {
                Customer customer = new Customer(
                        rs.getInt("Customer_ID"),
                        rs.getString("Customer_Name"),
                        rs.getString("Address"),
                        rs.getString("Postal_Code"),
                        rs.getString("Phone"),
                        rs.getTimestamp("Create_Date"),
                        rs.getString("Created_By"),
                        rs.getTimestamp("Last_Update"),
                        rs.getString("Last_Updated_By"),
                        rs.getInt("Division_ID"),
                        rs.getString("Division"),
                        rs.getString("Country"));
                customerList.add(customer);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        return customerList;
    }

    /**
     * Retrieves all country names from the database and returns them as an ObservableList of String objects.
     *
     * @return an ObservableList of country names.
     */
    public static ObservableList<String> getCountryList() {
        ObservableList<String> countryList = FXCollections.observableArrayList();

        try(Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT Country FROM countries;");
            // Loop through the ResultSet and add each row to the list
            while (rs.next()) {
                String country = rs.getString("country");
                countryList.add(country);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        return countryList;
    }

    /**
     * Retrieves the country ID associated with the given country name.
     *
     * @param countryName the name of the country for which to retrieve the ID.
     * @return the country ID associated with the given country name, or -1 if not found.
     */
    public static int getCountryId(String countryName) {
        String query = "SELECT Country_ID FROM countries WHERE Country = ?;";
        try(PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, countryName);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                return rs.getInt("Country_ID");
            }
        } catch(SQLException e) {
            System.out.println(e);
        }
        return -1;
    }

    /**
     * Retrieves a list of division names based on the provided country ID.
     *
     * @param countryId The ID of the country for which the divisions are to be retrieved.
     * @return An ObservableList of division names.
     */
    public static ObservableList<String> getDivisionList(int countryId) {
        ObservableList<String> divisionList = FXCollections.observableArrayList();

        String query = "SELECT Division FROM first_level_divisions WHERE Country_ID = ?;";
        try(PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, countryId);
            ResultSet rs = stmt.executeQuery();
            // Loop through the ResultSet and add each row to the list
            while (rs.next()) {
                String division = rs.getString("division");
                divisionList.add(division);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        return divisionList;
    }

    /**
     * Retrieves the ID of a division based on its name.
     *
     * @param divisionName The name of the division.
     * @return The ID of the division or -1 if not found.
     */
    public static int getDivisionId(String divisionName) {
        String query = "SELECT Division_ID FROM first_level_divisions WHERE Division = ?;";
        try(PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, divisionName);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                return rs.getInt("Division_ID");
            }
        } catch(SQLException e) {
            System.out.println(e);
        }
        return -1;
    }

    /**
     * Adds a new customer to the database.
     *
     * @param customer The Customer object to be added.
     */
    public static void addCustomer(Customer customer) {
        String query = "INSERT INTO customers " +
                "(Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By," +
                    "Division_ID)\n" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try(PreparedStatement stmt = connection.prepareStatement(query);) {
            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getAddress());
            stmt.setString(3, customer.getPostalCode());
            stmt.setString(4, customer.getPhone());
            stmt.setTimestamp(5, customer.getCreatedTimestamp());
            stmt.setString(6, customer.getCreatedBy());
            stmt.setTimestamp(7, customer.getCreatedTimestamp());
            stmt.setString(8, customer.getCreatedBy());
            stmt.setInt(9, customer.getDivisionId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    /**
     * Updates an existing customer in the database.
     *
     * @param customer The Customer object with updated information.
     */
    public static void updateCustomer(Customer customer) {
        String query = "UPDATE customers\n" +
                        "SET Customer_Name = ?," +
                        "Address = ?," +
                        "Postal_Code = ?," +
                        "Phone = ?," +
                        "Last_Update = ?," +
                        "Last_Updated_By = ?," +
                        "Division_ID = ?\n" +
                        "WHERE Customer_ID = ?;";

        try(PreparedStatement stmt = connection.prepareStatement(query);) {
            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getAddress());
            stmt.setString(3, customer.getPostalCode());
            stmt.setString(4, customer.getPhone());
            stmt.setTimestamp(5, customer.getUpdatedTimestamp());
            stmt.setString(6, customer.getUpdatedBy());
            stmt.setInt(7, customer.getDivisionId());
            stmt.setInt(8, customer.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    /**
     * Deletes a customer and their related appointments from the database.
     *
     * @param customerId The ID of the customer to be deleted.
     */
    public static void deleteCustomer(int customerId) {
        String appointmentsQuery = "DELETE FROM appointments WHERE Customer_ID = ?;";
        String customersQuery = "DELETE FROM customers WHERE Customer_ID = ?;";

        try(PreparedStatement stmt = connection.prepareStatement(appointmentsQuery);) {
            stmt.setInt(1, customerId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }

        try(PreparedStatement stmt = connection.prepareStatement(customersQuery);) {
            stmt.setInt(1, customerId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    /**
     * Retrieves a list of unique appointment types.
     *
     * @return An ObservableList of appointment types.
     */
    public static ObservableList<String> getTypeList() {
        ObservableList<String> typeList = FXCollections.observableArrayList();

        try(Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT DISTINCT Type FROM appointments;");
            // Loop through the ResultSet and add each row to the list
            while (rs.next()) {
                String type = rs.getString("Type");
                typeList.add(type);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        return typeList;
    }

    /**
     * Retrieves a list of appointments filtered by type and month.
     *
     * @param typeString The type of appointments to filter by.
     * @param monthString The name of the month to filter by.
     * @return An ObservableList of appointments matching the filters.
     */
    public static ObservableList<Appointment> getAppointmentListByTypeAndMonth(String typeString, String monthString) {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        Locale defaultLocale = Locale.getDefault();
        DateFormat format = new SimpleDateFormat("MMMM", defaultLocale);
        String query = "SELECT * FROM appointments WHERE Type = ? AND MONTH(Start) = ?;";

        try(PreparedStatement stmt = connection.prepareStatement(query);) {
            Date monthDate = format.parse(monthString);
            Calendar cal = Calendar.getInstance();
            cal.setTime(monthDate);
            int month = cal.get(Calendar.MONTH) + 1; // Add 1 to match SQL's 1-based month numbering
            stmt.setString(1, typeString);
            stmt.setInt(2, month);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Appointment appointment = new Appointment(
                        rs.getInt("Appointment_ID"),
                        rs.getString("Title"),
                        rs.getString("Description"),
                        rs.getString("Location"),
                        rs.getString("Type"),
                        rs.getTimestamp("Start"),
                        rs.getTimestamp("End"),
                        rs.getTimestamp("Create_Date"),
                        rs.getString("Created_By"),
                        rs.getTimestamp("Last_Update"),
                        rs.getString("Last_Updated_By"),
                        rs.getInt("Customer_ID"),
                        rs.getInt("User_ID"),
                        rs.getInt("Contact_ID"));
                appointmentList.add(appointment);
            }
        } catch (SQLException | ParseException e) {
            System.out.println(e);
        }

        return appointmentList;
    }

    /**
     * Retrieves a list of appointments associated with a specific contact ID.
     *
     * @param contactId The ID of the contact.
     * @return An ObservableList of appointments for the specified contact.
     */
    public static ObservableList<Appointment> getAppointmentListByContactId(int contactId) {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        String query = "SELECT * FROM appointments WHERE Contact_ID = ?;";

        try(PreparedStatement stmt = connection.prepareStatement(query);) {
            stmt.setInt(1, contactId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Appointment appointment = new Appointment(
                        rs.getInt("Appointment_ID"),
                        rs.getString("Title"),
                        rs.getString("Description"),
                        rs.getString("Location"),
                        rs.getString("Type"),
                        rs.getTimestamp("Start"),
                        rs.getTimestamp("End"),
                        rs.getTimestamp("Create_Date"),
                        rs.getString("Created_By"),
                        rs.getTimestamp("Last_Update"),
                        rs.getString("Last_Updated_By"),
                        rs.getInt("Customer_ID"),
                        rs.getInt("User_ID"),
                        rs.getInt("Contact_ID"));
                appointmentList.add(appointment);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        return appointmentList;
    }

    /**
     * Retrieves a list of appointments associated with a specific customer ID.
     *
     * @param customerId The ID of the customer.
     * @return An ObservableList of appointments for the specified customer.
     */
    public static ObservableList<Appointment> getAppointmentListByCustomerId(int customerId) {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        String query = "SELECT * FROM appointments WHERE Customer_ID = ?;";

        try(PreparedStatement stmt = connection.prepareStatement(query);) {
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Appointment appointment = new Appointment(
                        rs.getInt("Appointment_ID"),
                        rs.getString("Title"),
                        rs.getString("Description"),
                        rs.getString("Location"),
                        rs.getString("Type"),
                        rs.getTimestamp("Start"),
                        rs.getTimestamp("End"),
                        rs.getTimestamp("Create_Date"),
                        rs.getString("Created_By"),
                        rs.getTimestamp("Last_Update"),
                        rs.getString("Last_Updated_By"),
                        rs.getInt("Customer_ID"),
                        rs.getInt("User_ID"),
                        rs.getInt("Contact_ID"));
                appointmentList.add(appointment);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        return appointmentList;
    }

    /**
     * Retrieves a list of appointments associated with a specific country name.
     *
     * @param countryName The name of the country.
     * @return An ObservableList of appointments for the specified country.
     */
    public static ObservableList<Appointment> getAppointmentListByCountry(String countryName) {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        String query = "SELECT appointments.*, countries.Country FROM appointments\n" +
                        "JOIN customers ON appointments.Customer_ID = customers.Customer_ID\n" +
                        "JOIN first_level_divisions ON customers.Division_ID = first_level_divisions.Division_ID\n" +
                        "JOIN countries ON first_level_divisions.Country_ID = countries.Country_ID\n" +
                        "WHERE countries.Country = ?;";

        try(PreparedStatement stmt = connection.prepareStatement(query);) {
            stmt.setString(1, countryName);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Appointment appointment = new Appointment(
                        rs.getInt("Appointment_ID"),
                        rs.getString("Title"),
                        rs.getString("Description"),
                        rs.getString("Location"),
                        rs.getString("Type"),
                        rs.getTimestamp("Start"),
                        rs.getTimestamp("End"),
                        rs.getTimestamp("Create_Date"),
                        rs.getString("Created_By"),
                        rs.getTimestamp("Last_Update"),
                        rs.getString("Last_Updated_By"),
                        rs.getInt("Customer_ID"),
                        rs.getInt("User_ID"),
                        rs.getInt("Contact_ID"));
                appointmentList.add(appointment);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        return appointmentList;
    }

    /**
     * Retrieves a list of overlapping appointments based on the provided start and end timestamps
     * and excluding a specific appointment ID.
     *
     * @param startTimestamp The start timestamp to compare.
     * @param endTimestamp The end timestamp to compare.
     * @param appointmentId The ID of the appointment to exclude from the search.
     * @return An ObservableList of overlapping appointments.
     */
    public static ObservableList<Appointment> getOverlappingAppointmentList(Timestamp startTimestamp, Timestamp endTimestamp,
                                                                            int appointmentId) {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        String query = "SELECT * FROM appointments\n" +
                        "WHERE Start < ?\n" +
                        "AND End > ?\n" +
                        "AND Appointment_ID != ?;";

        try(PreparedStatement stmt = connection.prepareStatement(query);) {
            stmt.setTimestamp(1, endTimestamp);
            stmt.setTimestamp(2, startTimestamp);
            stmt.setInt(3, appointmentId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Appointment appointment = new Appointment(
                        rs.getInt("Appointment_ID"),
                        rs.getString("Title"),
                        rs.getString("Description"),
                        rs.getString("Location"),
                        rs.getString("Type"),
                        rs.getTimestamp("Start"),
                        rs.getTimestamp("End"),
                        rs.getTimestamp("Create_Date"),
                        rs.getString("Created_By"),
                        rs.getTimestamp("Last_Update"),
                        rs.getString("Last_Updated_By"),
                        rs.getInt("Customer_ID"),
                        rs.getInt("User_ID"),
                        rs.getInt("Contact_ID"));
                appointmentList.add(appointment);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        return appointmentList;
    }
}
