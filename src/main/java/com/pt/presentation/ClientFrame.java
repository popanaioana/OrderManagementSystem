
package com.pt.presentation;

import com.pt.businessLayer.ClientBLL;
import com.pt.model.Client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

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

    private static final Color BACKGROUND = new Color(23, 27, 36);
    private static final Color PANEL = new Color(31, 36, 48);
    private static final Color TABLE_BACKGROUND = new Color(38, 44, 58);
    private static final Color ACCENT = new Color(99, 102, 241);
    private static final Color TEXT = new Color(240, 242, 248);
    private static final Color MUTED = new Color(155, 163, 180);
    private static final Color BORDER = new Color(57, 64, 80);

    /**
     * Constructs and initializes the Client operations user interface window.
     */
    public ClientFrame() {
        setTitle("OrderManagementSystem | Client Management");
        setSize(1100, 750);
        setMinimumSize(new Dimension(850, 600));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel mainPanel = new JPanel(new BorderLayout(0, 25)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2.setPaint(new GradientPaint(0, 0, BACKGROUND, getWidth(), getHeight(), new Color(35, 34, 57)));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        mainPanel.setBorder(new EmptyBorder(35, 40, 30, 40));
        setContentPane(mainPanel);
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        JLabel titleLabel = new JLabel("Client Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        titleLabel.setForeground(TEXT);
        JLabel subtitleLabel = new JLabel("Manage your customers and their information");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(MUTED);
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(8));
        titlePanel.add(subtitleLabel);
        headerPanel.add(titlePanel, BorderLayout.WEST);
        JPanel topPanel = new JPanel(new BorderLayout(0, 22));
        topPanel.setOpaque(false);
        topPanel.add(headerPanel, BorderLayout.NORTH);
        topPanel.add(createFormPanel(), BorderLayout.CENTER);
        topPanel.add(createButtonPanel(), BorderLayout.SOUTH);
        mainPanel.add(topPanel, BorderLayout.NORTH);
        table.setBackground(TABLE_BACKGROUND);
        table.setForeground(TEXT);
        table.setSelectionBackground(new Color(73, 77, 150));
        table.setSelectionForeground(Color.WHITE);
        table.setShowGrid(false);
        table.setRowHeight(42);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);
        table.setIntercellSpacing(new Dimension(0, 0));

        DefaultTableCellRenderer cellRenderer =
                new DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                        super.getTableCellRendererComponent(table, value, isSelected, false, row, column);
                        setBorder(new EmptyBorder(0, 14, 0, 14));
                        setForeground(TEXT);
                        setFont(new Font("Segoe UI", Font.PLAIN, 13));
                        if (isSelected) {
                            setBackground(new Color(73, 77, 150));
                        } else {
                            setBackground(row % 2 == 0 ? TABLE_BACKGROUND : new Color(43, 49, 64));
                        }
                        return this;
                    }
                };
        table.setDefaultRenderer(Object.class, cellRenderer);
        table.setDefaultRenderer(Number.class, cellRenderer);
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setDefaultRenderer(
                new DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                        JLabel label = new JLabel(value == null ? "" : value.toString());
                        label.setForeground(TEXT);
                        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
                        label.setBorder(new EmptyBorder(0, 14, 0, 14));
                        label.setOpaque(false);
                        return label;
                    }
                }
        );
        tableHeader.setBackground(PANEL);
        tableHeader.setForeground(TEXT);
        tableHeader.setPreferredSize(new Dimension(0, 48));
        tableHeader.setReorderingAllowed(false);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setBackground(TABLE_BACKGROUND);
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                thumbColor = new Color(90, 93, 165);
                trackColor = PANEL;
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return invisibleButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return invisibleButton();
            }

            private JButton invisibleButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                return button;
            }

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle bounds) {
                if (bounds.isEmpty()) return;
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(99, 102, 180));
                g2.fillRoundRect(bounds.x + 3, bounds.y + 3, Math.max(0, bounds.width - 6), Math.max(0, bounds.height - 6), 12, 12);
                g2.dispose();
            }
        });
        verticalScrollBar.setPreferredSize(new Dimension(12, 0));
        verticalScrollBar.setUnitIncrement(16);
        JPanel tableContainer = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(PANEL);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 22, 22);
                g2.dispose();
            }
        };
        tableContainer.setOpaque(false);
        tableContainer.setBorder(new EmptyBorder(1, 1, 1, 1));
        JPanel roundedContent = new JPanel(new BorderLayout()) {
            @Override
            protected void paintChildren(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setClip(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 22, 22));
                super.paintChildren(g2);
                g2.dispose();
            }
        };
        roundedContent.setOpaque(false);
        roundedContent.add(scrollPane, BorderLayout.CENTER);
        tableContainer.add(roundedContent, BorderLayout.CENTER);
        mainPanel.add(tableContainer, BorderLayout.CENTER);
        JLabel footerLabel = new JLabel("ORDER MANAGEMENT SYSTEM  |  CLIENT RECORDS");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footerLabel.setForeground(MUTED);
        mainPanel.add(footerLabel, BorderLayout.SOUTH);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.convertRowIndexToModel(
                        table.getSelectedRow()
                );
                idField.setText(table.getModel().getValueAt(row, 0).toString());
                nameField.setText(table.getModel().getValueAt(row, 1).toString());
                addressField.setText(table.getModel().getValueAt(row, 2).toString());
                emailField.setText(table.getModel().getValueAt(row, 3).toString());
            }
        });
        refresh();
    }

    /**
     * Assembles the structural input form grid panel containing labels and text data fields.
     *
     * @return a configured {@link JPanel} object layout for data input
     */
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 4, 16, 10));
        panel.setOpaque(false);
        JLabel[] labels = {new JLabel("CLIENT ID"), new JLabel("FULL NAME"), new JLabel("ADDRESS"), new JLabel("EMAIL")};
        for (JLabel label : labels) {
            label.setForeground(MUTED);
            label.setFont(new Font("Segoe UI", Font.BOLD, 11));
            panel.add(label);
        }
        JTextField[] fields = {idField, nameField, addressField, emailField};
        for (JTextField field : fields) {
            field.setBackground(PANEL);
            field.setForeground(TEXT);
            field.setCaretColor(TEXT);
            field.setSelectionColor(ACCENT);
            field.setSelectedTextColor(Color.WHITE);
            field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            field.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER), new EmptyBorder(10, 12, 10, 12)));
            field.setPreferredSize(new Dimension(100, 42));
            panel.add(field);
        }
        return panel;
    }

    /**
     * Assembles the management control button panel linking actions to business operations.
     *
     * @return a configured {@link JPanel} object layout for application triggers
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        panel.setOpaque(false);
        JButton addButton = createStyledButton("Add Client", ACCENT);
        JButton updateButton = createStyledButton("Update", new Color(55, 125, 180));
        JButton deleteButton = createStyledButton("Delete", new Color(190, 65, 85));
        JButton refreshButton = createStyledButton("Refresh", new Color(65, 72, 90));
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

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color start = getModel().isRollover() ? color.brighter() : color;
                Color end = getModel().isPressed() ? color.darker().darker() : color.darker();
                g2.setPaint(new GradientPaint(0, 0, start,0, getHeight(), end));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setRolloverEnabled(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(135, 42));
        return button;
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
