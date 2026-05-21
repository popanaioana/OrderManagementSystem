package com.pt.presentation;

import com.pt.businessLayer.ProductBLL;
import com.pt.model.Product;

import javax.swing.*;
import java.awt.*;

/**
 * Presentation Layer window dedicated to executing CRUD operations on warehouse Products inventory.
 * It displays persistent item data within a dynamic {@link JTable} component completely populated
 * and configured using Java Reflection techniques.
 */

public class ProductFrame extends JFrame {
    private final ProductBLL productBLL = new ProductBLL();
    private final JTable table = new JTable();
    private final JTextField idField = new JTextField();
    private final JTextField nameField = new JTextField();
    private final JTextField priceField = new JTextField();
    private final JTextField stockField = new JTextField();

    /**
     * Constructs and initializes the graphical window container for managing Product configurations.
     * Sets layouts, frames parameters, and triggers the initial relational data synchronization.
     */
    public ProductFrame() {
        setTitle("Product Operations");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        add(createFormPanel(), BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
        refresh();
    }

    /**
     * Assembles the graphical grid-based input panel layer containing labels and interactive text fields.
     *
     * @return a configured data entry {@link JPanel} layout reference
     */
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 4, 10, 10));
        panel.add(new JLabel("ID"));
        panel.add(new JLabel("Name"));
        panel.add(new JLabel("Price"));
        panel.add(new JLabel("Stock"));
        panel.add(idField);
        panel.add(nameField);
        panel.add(priceField);
        panel.add(stockField);
        return panel;
    }

    /**
     * Assembles the tracking control panel button cluster connecting client action triggers to application operations.
     *
     * @return a configured functional layout trigger {@link JPanel} reference
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton refreshButton = new JButton("Refresh");
        addButton.addActionListener(e -> addProduct());
        updateButton.addActionListener(e -> updateProduct());
        deleteButton.addActionListener(e -> deleteProduct());
        refreshButton.addActionListener(e -> refresh());
        panel.add(addButton);
        panel.add(updateButton);
        panel.add(deleteButton);
        panel.add(refreshButton);
        return panel;
    }

    /**
     * Reads numerical string texts from inputs to forward an initialization append operation for a new Product record.
     */
    private void addProduct() {
        try {
            Product product = new Product(nameField.getText(), Double.parseDouble(priceField.getText()), Integer.parseInt(stockField.getText()));
            productBLL.addProduct(product);
            refresh();
            clearFields();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    /**
     * Extracts variables parameters to forward an update operation request over an existing warehouse Product record.
     */
    private void updateProduct() {
        try {
            Product product = new Product(Integer.parseInt(idField.getText()), nameField.getText(), Double.parseDouble(priceField.getText()), Integer.parseInt(stockField.getText()));
            productBLL.updateProduct(product);
            refresh();
            clearFields();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    /**
     * Processes individual identity identifier numbers to forward a deletion request targeting an index record row.
     */
    private void deleteProduct() {
        try {
            int id = Integer.parseInt(idField.getText());
            productBLL.deleteProduct(id);
            refresh();
            clearFields();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    /**
     * Queries the Business Layer to pull current product listings, dynamically refreshing the viewing grid matrix.
     */
    private void refresh() {
        table.setModel(ReflectionTableModel.createTableModel(productBLL.findProductsSortedByName()));
        clearFields();
        table.clearSelection();
        table.revalidate();
        table.repaint();
    }

    /**
     * Resets all visual text input parameters fields across the interface matrix layer.
     */
    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        priceField.setText("");
        stockField.setText("");
    }

    /**
     * Standard message dialog utility helper used for rendering constraint warnings or process errors.
     *
     * @param message target textual explanation tracing the encountered exception validation parameters
     */
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}