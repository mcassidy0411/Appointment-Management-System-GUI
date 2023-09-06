package com.mc.model;

import java.sql.Timestamp;

/**
 * Represents a customer with their associated information.
 * @author Michael Cassidy
 */
public class Customer {
    int id;
    String name;
    String address;
    String postalCode;
    String phone;
    Timestamp createdTimestamp;
    String createdBy;
    Timestamp updatedTimestamp;
    String updatedBy;
    int divisionId;
    String division;
    String country;

    /**
     * Constructs a new Customer instance for displaying customers.
     *
     * @param id The customer ID.
     * @param name The customer name.
     * @param address The customer address.
     * @param postalCode The customer postal code.
     * @param phone The customer phone number.
     * @param createdTimestamp The customer record creation timestamp.
     * @param createdBy The user who created the customer record.
     * @param updatedTimestamp The customer record update timestamp.
     * @param updatedBy The user who updated the customer record.
     * @param divisionId The division ID for the customer.
     * @param division The division name for the customer.
     * @param country The country of the customer.
     */
    public Customer(int id, String name, String address, String postalCode, String phone, Timestamp createdTimestamp,
                    String createdBy, Timestamp updatedTimestamp, String updatedBy, int divisionId, String division,
                    String country) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.createdTimestamp = createdTimestamp;
        this.createdBy = createdBy;
        this.updatedTimestamp = updatedTimestamp;
        this.updatedBy = updatedBy;
        this.divisionId = divisionId;
        this.division = division;
        this.country = country;
    }

    /**
     * Constructs a new Customer instance for adding customers.  ID field is omitted as the database auto-generates one
     *
     * @param name The customer name.
     * @param address The customer address.
     * @param postalCode The customer postal code.
     * @param phone The customer phone number.
     * @param createdTimestamp The customer record creation timestamp.
     * @param createdBy The user who created the customer record.
     * @param updatedTimestamp The customer record update timestamp.
     * @param updatedBy The user who updated the customer record.
     * @param divisionId The division ID for the customer.
     */
    public Customer(String name, String address, String postalCode, String phone, Timestamp createdTimestamp,
                    String createdBy, Timestamp updatedTimestamp, String updatedBy, int divisionId) {
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.createdTimestamp = createdTimestamp;
        this.createdBy = createdBy;
        this.updatedTimestamp = updatedTimestamp;
        this.updatedBy = updatedBy;
        this.divisionId = divisionId;
    }

    /**
     * Constructs a new Customer instance for modifying customers.
     *
     * @param id The customer ID.
     * @param name The customer name.
     * @param address The customer address.
     * @param postalCode The customer postal code.
     * @param phone The customer phone number.
     * @param createdTimestamp The customer record creation timestamp.
     * @param createdBy The user who created the customer record.
     * @param updatedTimestamp The customer record update timestamp.
     * @param updatedBy The user who updated the customer record.
     * @param divisionId The division ID for the customer.
     */
    public Customer(int id, String name, String address, String postalCode, String phone, Timestamp createdTimestamp,
                    String createdBy, Timestamp updatedTimestamp, String updatedBy, int divisionId) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.createdTimestamp = createdTimestamp;
        this.createdBy = createdBy;
        this.updatedTimestamp = updatedTimestamp;
        this.updatedBy = updatedBy;
        this.divisionId = divisionId;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }

    public void setAddress(String address) { this.address = address; }

    public String getPostalCode() { return postalCode; }

    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }

    public String getPhone() { return phone; }

    public void setPhone(String phone) { this.phone = phone; }

    public Timestamp getCreatedTimestamp() { return createdTimestamp; }

    public void setCreatedTimestamp(Timestamp createdTimestamp) { this.createdTimestamp = createdTimestamp; }

    public String getCreatedBy() { return createdBy; }

    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public Timestamp getUpdatedTimestamp() { return updatedTimestamp; }

    public void setUpdatedTimestamp(Timestamp updatedTimestamp) { this.updatedTimestamp = updatedTimestamp; }

    public String getUpdatedBy() { return updatedBy; }

    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }

    public int getDivisionId() { return divisionId; }

    public void setDivisionId(int divisionId) { this.divisionId = divisionId; }

    public String getDivision() { return division; }

    public void setDivision(String division) { this.division = division; }

    public String getCountry() { return country; }

    public void setCountry(String country) { this.country = country; }
}
