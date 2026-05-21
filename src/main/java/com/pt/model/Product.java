package com.pt.model;

/**
 * Model class representing a Product stock entity within the warehouse.
 * Maps directly to rows within the Product table inside the application database.
 */

public class Product {
    private int id;
    private String name;
    private double price;
    private int stock;

    /**
     * Default no-argument constructor necessary for instance compilation through Java Reflection mechanisms.
     */
    public Product() {
    }

    /**
     * Constructs a new Product context model without assigning an identifier value.
     * Used mostly when registering brand-new catalog entries before primary key initialization.
     *
     * @param name  the descriptive inventory catalog name of the item
     * @param price individual unit wholesale monetary cost evaluation
     * @param stock total quantities currently available inside the storage facility
     */
    public Product(String name, double price, int stock) {
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    /**
     * Constructs a new Product context model with a persistent data source identifier.
     * Used upon structural dataset mapping actions from persistent layers or during updates.
     *
     * @param id    the persistent singular database primary key index value
     * @param name  the descriptive inventory catalog name of the item
     * @param price individual unit wholesale monetary cost evaluation
     * @param stock total quantities currently available inside the storage facility
     */
    public Product(int id, String name, double price, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    /**
     * Gets the database primary key storage entity identifier assigned to the item.
     *
     * @return unique database index integer
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the database primary key storage entity identifier assigned to the item.
     *
     * @param id unique database index integer to assign
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the description title label specifying the item context.
     *
     * @return catalog string title name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the description title label specifying the item context.
     *
     * @param name catalog string title name to assign
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the active price value configured per individual item unit.
     *
     * @return double unit value pricing
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the active price value configured per individual item unit.
     *
     * @param price double unit value pricing to assign
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Gets the volume metric remaining inside warehouse deposits.
     *
     * @return units quantity integer count
     */
    public int getStock() {
        return stock;
    }

    /**
     * Sets the volume metric remaining inside warehouse deposits.
     *
     * @param stock units quantity integer count to assign
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Generates a structural string format of the object instance utilized inside UI interface combo selectors.
     *
     * @return a text label pattern under the template "ID - Name | stock: Value"
     */
    @Override
    public String toString() {
        return id + " - " + name + " | stock: " + stock;
    }
}