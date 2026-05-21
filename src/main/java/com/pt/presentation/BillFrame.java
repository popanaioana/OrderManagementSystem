package com.pt.presentation;

import com.pt.dataAccessLayer.BillDAO;

import javax.swing.*;
import java.awt.*;

/**
 * Presentation Layer window designed to display historical commercial invoices (Bills).
 * Uses reflection mapping to populate logged bills read from the log table inside a read-only viewer.
 */

public class BillFrame extends JFrame {
    private final BillDAO billDAO = new BillDAO();
    private final JTable table = new JTable();

    /**
     * Constructs and initializes the system financial bill logs interface viewer.
     */
    public BillFrame() {
        setTitle("Bill Log");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refresh());
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(refreshButton, BorderLayout.SOUTH);
        refresh();
    }

    /**
     * Synchronizes and reads the immutable records data logs to rebuild the viewer data matrix.
     */
    private void refresh() {
        table.setModel(ReflectionTableModel.createTableModel(billDAO.findAll()));
        table.clearSelection();
        table.revalidate();
        table.repaint();
    }
}