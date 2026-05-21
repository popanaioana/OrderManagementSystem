package com.pt.presentation;

import com.pt.businessLayer.ClientBLL;
import com.pt.model.Client;

import javax.swing.*;
import java.awt.*;

/**
 * Presentation Layer window dedicated to executing CRUD operations on Clients.
 * Displays persistent customer data in a dynamic {@link JTable} generated via reflection techniques.
 */

public class ClientFrame extends JFrame {
    private final ClientBLL clientBLL = new ClientBLL();
    private final JTable table = new JTable();
    private final JTextField idField = new JTextField();
    private final JTextField nameField = new JTextField();
    private final JTextField addressField = new JTextField();
    private final JTextField emailField = new JTextField();

    /**
     * Constructs and initializes the Client operations user interface window.
     */
    public ClientFrame() {
        setTitle("Client Operations");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        add(createFormPanel(), BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
        refresh();
    }

    /**
     * Assembles the structural input form grid panel containing labels and text data fields.
     *
     * @return a configured {@link JPanel} object layout for data input
     */
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 4, 10, 10));
        panel.add(new JLabel("ID"));
        panel.add(new JLabel("Name"));
        panel.add(new JLabel("Address"));
        panel.add(new JLabel("Email"));
        panel.add(idField);
        panel.add(nameField);
        panel.add(addressField);
        panel.add(emailField);
        return panel;
    }

    /**
     * Assembles the management control button panel linking actions to business operations.
     *
     * @return a configured {@link JPanel} object layout for application triggers
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton refreshButton = new JButton("Refresh");
        addButton.addActionListener(e -> addClient());
        updateButton.addActionListener(e -> updateClient());
        deleteButton.addActionListener(e -> deleteClient());
        refreshButton.addActionListener(e -> refresh());
        panel.add(addButton);
        panel.add(updateButton);
        panel.add(deleteButton);
        panel.add(refreshButton);
        return panel;
    }

    /**
     * Extracts input fields text details to forward a request for appending a new Client record.
     */
    private void addClient() {
        try {
            Client client = new Client(nameField.getText(), addressField.getText(), emailField.getText());
            clientBLL.addClient(client);
            refresh();
            clearFields();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    /**
     * Extracts updated fields to execute modifications over an existing customer record.
     */
    private void updateClient() {
        try {
            Client client = new Client(Integer.parseInt(idField.getText()), nameField.getText(), addressField.getText(), emailField.getText());
            clientBLL.updateClient(client);
            refresh();
            clearFields();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    /**
     * Forwards a persistent records removal request based on the ID primary key field.
     */
    private void deleteClient() {
        try {
            int id = Integer.parseInt(idField.getText());
            clientBLL.deleteClient(id);
            refresh();
            clearFields();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    /**
     * Interrogates the Business Layer to pull current entries, dynamically rebuilding the view grid.
     */
    private void refresh() {
        table.setModel(ReflectionTableModel.createTableModel(clientBLL.findClientsSortedByName()));
        clearFields();
        table.clearSelection();
        table.revalidate();
        table.repaint();
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        addressField.setText("");
        emailField.setText("");
    }

    /**
     * Triggers a graphical pop-up modal dialog highlighting warning or verification failures.
     *
     * @param message description message mapping the encountered warning
     */
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}