package com.pt.businessLayer;

import com.pt.dataAccessLayer.BillDAO;
import com.pt.dataAccessLayer.ClientDAO;
import com.pt.dataAccessLayer.OrderDAO;
import com.pt.dataAccessLayer.ProductDAO;
import com.pt.model.Bill;
import com.pt.model.Client;
import com.pt.model.Order;
import com.pt.model.Product;
import com.pt.validator.OrderValidator;
import com.pt.validator.Validator;

import java.util.List;

/**
 * Business Logic Layer class for handling operations related to Orders.
 * It coordinates validators, data access objects, and ensures the integrity of order placements.
 */

public class OrderBLL {

    private final OrderDAO orderDAO = new OrderDAO();
    private final ClientDAO clientDAO = new ClientDAO();
    private final ProductDAO productDAO = new ProductDAO();
    private final BillDAO billDAO = new BillDAO();

    private final Validator<Order> orderValidator = new OrderValidator();

    /**
     * Retrieves all orders currently stored in the database.
     *
     * @return a List containing all Order objects, or an empty list if none exist
     */
    public List<Order> findAllOrders() {
        return orderDAO.findAll();
    }

    /**
     * Processes and creates a new product order for a client.
     * Validates the existence of the client and product, verifies stock levels,
     * decrements the stock, records the order, and logs an immutable bill record.
     *
     * @param clientId  the unique identifier of the purchasing client
     * @param productId the unique identifier of the requested product
     * @param quantity  the number of items desired in the order
     * @return the generated unique identifier (ID) of the successfully inserted order
     * @throws IllegalArgumentException if the client or product does not exist, or if stock is insufficient
     * @throws RuntimeException         if an unexpected error occurs during database insertion
     */
    public int createOrder(int clientId, int productId, int quantity) {
        Client client = clientDAO.findById(clientId);
        Product product = productDAO.findById(productId);
        if (client == null) {
            throw new IllegalArgumentException("Client does not exist.");
        }
        if (product == null) {
            throw new IllegalArgumentException("Product does not exist.");
        }
        if (product.getStock() < quantity) {
            throw new IllegalArgumentException("Under-stock: not enough products available.");
        }
        double totalPrice = product.getPrice() * quantity;
        Order order = new Order(clientId, productId, quantity, totalPrice);
        orderValidator.validate(order);
        int orderId = orderDAO.insert(order);
        if (orderId == -1) {
            throw new RuntimeException("Eroare critică: Comanda nu a putut fi salvată în baza de date.");
        }
        product.setStock(product.getStock() - quantity);
        productDAO.update(product);
        Bill bill = new Bill(0, orderId, client.getName(), product.getName(), quantity, totalPrice, null);
        billDAO.insert(bill);
        return orderId;
    }
}