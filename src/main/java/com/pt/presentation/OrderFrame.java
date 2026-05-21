package com.pt.presentation;

import com.pt.businessLayer.ClientBLL;
import com.pt.businessLayer.OrderBLL;
import com.pt.businessLayer.ProductBLL;
import com.pt.model.Client;
import com.pt.model.Product;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Presentation Layer window designed to process commercial warehouse order operations.
 * It allows the user to select clients and available products using choice combo boxes,
 * validates stock levels, and generates real-time data table synchronization.
 */

public class OrderFrame extends JFrame {
    private final ClientBLL clientBLL = new ClientBLL();
    private final ProductBLL productBLL = new ProductBLL();
    private final OrderBLL orderBLL = new OrderBLL();
    private final JComboBox<Client> clientComboBox = new JComboBox<>();
    private final JComboBox<Product> productComboBox = new JComboBox<>();
    private final JTextField quantityField = new JTextField();
    private final JTable orderTable = new JTable();

    /**
     * Constructs and initializes the warehouse purchase orders interface workflow frame.
     */
    public OrderFrame() {
        setTitle("Order Operations");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        add(createOrderPanel(), BorderLayout.NORTH);
        add(new JScrollPane(orderTable), BorderLayout.CENTER);
        refreshData();
    }

    /**
     * Compiles the structural visual selectors and control panels for order configuration.
     *
     * @return a configured operational control layout {@link JPanel}
     */
    private JPanel createOrderPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 4, 10, 10));
        JButton createButton = new JButton("Create Order");
        JButton refreshButton = new JButton("Refresh");
        createButton.addActionListener(e -> createOrder());
        refreshButton.addActionListener(e -> refreshData());
        panel.add(new JLabel("Client"));
        panel.add(new JLabel("Product"));
        panel.add(new JLabel("Quantity"));
        panel.add(new JLabel("Actions"));
        panel.add(clientComboBox);
        panel.add(productComboBox);
        panel.add(quantityField);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(createButton);
        buttonPanel.add(refreshButton);
        panel.add(buttonPanel);
        return panel;
    }

    private void clearFields() {
        quantityField.setText("");
        if (clientComboBox.getItemCount() > 0) {
            clientComboBox.setSelectedIndex(0);
        }
        if (productComboBox.getItemCount() > 0) {
            productComboBox.setSelectedIndex(0);
        }
        orderTable.clearSelection();
    }

    /**
     * Flushes and updates data models across all active client selectors, product stocks, and orders tables.
     */
    private void refreshData() {
        refreshClients();
        refreshProducts();
        refreshOrders();
        clearFields();
    }

    private void refreshClients() {
        clientComboBox.removeAllItems();
        List<Client> clients = clientBLL.findAllClients();
        for (Client client : clients) {
            clientComboBox.addItem(client);
        }
    }

    private void refreshProducts() {
        productComboBox.removeAllItems();
        List<Product> products = productBLL.findAvailableProducts();
        for (Product product : products) {
            productComboBox.addItem(product);
        }
    }

    private void refreshOrders() {
        orderTable.setModel(ReflectionTableModel.createTableModel(orderBLL.findAllOrders()));
    }

    /**
     * Processes input selection choices to forward a placement order transaction request.
     * Triggers informational alerts upon validation issues or success completions.
     */
    private void createOrder() {
        try {
            Client selectedClient = (Client) clientComboBox.getSelectedItem();
            Product selectedProduct = (Product) productComboBox.getSelectedItem();
            if (selectedClient == null || selectedProduct == null) {
                throw new IllegalArgumentException("Please select a client and a product.");
            }
            int quantity = Integer.parseInt(quantityField.getText());
            orderBLL.createOrder(
                    selectedClient.getId(),
                    selectedProduct.getId(),
                    quantity
            );
            quantityField.setText("");
            refreshData();
            JOptionPane.showMessageDialog(this, "Order created successfully.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}