package com.pt.model;

import java.sql.Timestamp;

/**
 * Model class representing an Order entity within the warehouse management system.
 * This class maps directly to the relational structures within the database Orders table.
 */

public class Order {
    private int id;
    private int clientId;
    private int productId;
    private int quantity;
    private double totalPrice;
    private Timestamp orderDate;

    /**
     * Default no-argument constructor required for automatic instance generation via Java Reflection.
     */
    public Order() {
    }

    /**
     * Constructs a new Order instance with specific application parameter fields.
     * This constructor is used when preparing a new commercial transaction before persistent insertion.
     *
     * @param clientId   the persistent data identifier referencing the ordering Client
     * @param productId  the persistent data identifier referencing the requested Product warehouse item
     * @param quantity   the number volume count of products ordered
     * @param totalPrice the total accumulated monetary gross cost evaluated for the order
     */
    public Order(int clientId, int productId, int quantity, double totalPrice) {
        this.clientId = clientId;
        this.productId = productId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    /**
     * Gets the unique database primary key identity identifier of the order.
     *
     * @return the unique integer database ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique database primary key identity identifier of the order.
     *
     * @param id the unique integer database ID to assign
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the relational foreign key reference identifying the client associated with the order.
     *
     * @return the associated client's identifier index integer
     */
    public int getClientId() {
        return clientId;
    }

    /**
     * Sets the relational foreign key reference identifying the client associated with the order.
     *
     * @param clientId the associated client's identifier index integer to assign
     */
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    /**
     * Gets the relational foreign key reference identifying the item product associated with the order.
     *
     * @return the associated product's identifier index integer
     */
    public int getProductId() {
        return productId;
    }

    /**
     * Sets the relational foreign key reference identifying the item product associated with the order.
     *
     * @param productId the associated product's identifier index integer to assign
     */
    public void setProductId(int productId) {
        this.productId = productId;
    }

    /**
     * Gets the structural item volume quantity defined within this specific order context.
     *
     * @return the quantity of pieces ordered
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the structural item volume quantity defined within this specific order context.
     *
     * @param quantity the quantity of pieces ordered to assign
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Gets the calculated comprehensive financial pricing total cost evaluated for this order.
     *
     * @return the double financial total price calculation value
     */
    public double getTotalPrice() {
        return totalPrice;
    }

    /**
     * Sets the calculated comprehensive financial pricing total cost evaluated for this order.
     *
     * @param totalPrice the double financial total price calculation value to assign
     */
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    /**
     * Gets the precise database timestamp representing the generation time context of this order row.
     *
     * @return the specific sql Timestamp data structure value
     */
    public Timestamp getOrderDate() {
        return orderDate;
    }

    /**
     * Sets the precise database timestamp representing the generation time context of this order row.
     *
     * @param orderDate the specific sql Timestamp data structure value to assign
     */
    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }
}