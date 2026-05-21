package com.pt.presentation;

import javax.swing.*;
import java.awt.*;

/**
 * Presentation Layer class representing the primary dashboard window of the application.
 * It serves as the main entry point for the graphical user interface, providing navigation buttons
 * to access client management, product management, order processing, and log viewing modules.
 */

public class MainFrame extends JFrame {

    /**
     * Constructs a new MainFrame dashboard window.
     * Initializes the frame layout, configures standard close behaviors, and maps navigation event listeners.
     */
    public MainFrame() {
        setTitle("Orders Management");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 1, 10, 10));
        JButton clientsButton = new JButton("Client Operations");
        JButton productsButton = new JButton("Product Operations");
        JButton ordersButton = new JButton("Order Operations");
        JButton billsButton = new JButton("Bill Log");
        JButton exitButton = new JButton("Exit");
        clientsButton.addActionListener(e -> new ClientFrame().setVisible(true));
        productsButton.addActionListener(e -> new ProductFrame().setVisible(true));
        ordersButton.addActionListener(e -> new OrderFrame().setVisible(true));
        billsButton.addActionListener(e -> new BillFrame().setVisible(true));
        exitButton.addActionListener(e -> System.exit(0));
        add(clientsButton);
        add(productsButton);
        add(ordersButton);
        add(billsButton);
        add(exitButton);
    }
}