package com.pt.model;

/**
 * Model class representing a Client entity within the warehouse application.
 * This class maps directly to the structure of the Client table in the relational database.
 */

public class Client {
    private int id;
    private String name;
    private String address;
    private String email;

    /**
     * Default no-argument constructor required for object instantiation via Java Reflection.
     */
    public Client() {
    }

    /**
     * Constructs a new Client instance without an internal database identity identifier.
     * This constructor is primarily utilized when preparing a new client record for data insertion.
     *
     * @param name    the full legal name of the client
     * @param address the delivery and physical address of the client
     * @param email   the official contact email account address of the client
     */
    public Client(String name, String address, String email) {
        this.name = name;
        this.address = address;
        this.email = email;
    }

    /**
     * Constructs a new Client instance containing a known persistent primary key identifier.
     * This constructor is typically utilized when mapping records fetched from the database or for record updates.
     *
     * @param id      the unique database primary key entity identifier
     * @param name    the full legal name of the client
     * @param address the delivery and physical address of the client
     * @param email   the official contact email account address of the client
     */
    public Client(int id, String name, String address, String email) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.email = email;
    }

    /**
     * Gets the unique primary key identifier of the client record.
     *
     * @return the unique integer database ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique primary key identifier of the client record.
     *
     * @param id the unique integer database ID to assign
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the full legal name associated with the client profile.
     *
     * @return a String representing the name of the client
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the full legal name associated with the client profile.
     *
     * @param name a String containing the client's name to assign
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the official contact email account address configured for the client.
     *
     * @return a String specifying the client's email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the official contact email account address configured for the client.
     *
     * @param email a String specifying the client's email address to assign
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the designated delivery or billing physical address of the client.
     *
     * @return a String specifying the location address details
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the designated delivery or billing physical address of the client.
     *
     * @param address a String specifying the location address details to assign
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Returns a formatted string representation of the client object instance,
     * which is used inside UI combo box selectors.
     *
     * @return a textual representation under the format "ID - Name"
     */
    @Override
    public String toString() {
        return id + " - " + name;
    }
}