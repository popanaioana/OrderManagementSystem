
package com.pt.presentation;

import com.pt.businessLayer.ClientBLL;
import com.pt.businessLayer.OrderBLL;
import com.pt.businessLayer.ProductBLL;
import com.pt.model.Client;
import com.pt.model.Product;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
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
    private static final Color BACKGROUND = new Color(23, 27, 36);
    private static final Color PANEL = new Color(31, 36, 48);
    private static final Color TABLE_BACKGROUND = new Color(38, 44, 58);
    private static final Color ACCENT = new Color(99, 102, 241);
    private static final Color TEXT = new Color(240, 242, 248);
    private static final Color MUTED = new Color(155, 163, 180);
    private static final Color BORDER = new Color(57, 64, 80);

    /**
     * Constructs and initializes the warehouse purchase orders interface workflow frame.
     */
    public OrderFrame() {
        setTitle("OrderManagementSystem | Order Management");
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
                g2.setPaint(new GradientPaint( 0, 0, BACKGROUND, getWidth(), getHeight(), new Color(35, 34, 57)));
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
        JLabel titleLabel = new JLabel("Order Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        titleLabel.setForeground(TEXT);
        JLabel subtitleLabel = new JLabel("Create orders and manage your order history");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(MUTED);
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(8));
        titlePanel.add(subtitleLabel);
        headerPanel.add(titlePanel, BorderLayout.WEST);
        JPanel topPanel = new JPanel(new BorderLayout(0, 25));
        topPanel.setOpaque(false);
        topPanel.add(headerPanel, BorderLayout.NORTH);
        topPanel.add(createOrderPanel(), BorderLayout.CENTER);
        mainPanel.add(topPanel, BorderLayout.NORTH);
        orderTable.setBackground(TABLE_BACKGROUND);
        orderTable.setForeground(TEXT);
        orderTable.setSelectionBackground(new Color(73, 77, 150));
        orderTable.setSelectionForeground(Color.WHITE);
        orderTable.setShowGrid(false);
        orderTable.setRowHeight(42);
        orderTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        orderTable.setFillsViewportHeight(true);
        orderTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        orderTable.setAutoCreateRowSorter(true);
        orderTable.setIntercellSpacing(new Dimension(0, 0));
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
        orderTable.setDefaultRenderer(Object.class, cellRenderer);
        orderTable.setDefaultRenderer(Number.class, cellRenderer);
        JTableHeader tableHeader = orderTable.getTableHeader();
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
        JScrollPane scrollPane = new JScrollPane(orderTable);
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
                g2.setClip(new RoundRectangle2D.Double( 0, 0, getWidth(), getHeight(), 22, 22));
                super.paintChildren(g2);
                g2.dispose();
            }
        };
        roundedContent.setOpaque(false);
        roundedContent.add(scrollPane, BorderLayout.CENTER);
        tableContainer.add(roundedContent, BorderLayout.CENTER);
        mainPanel.add(tableContainer, BorderLayout.CENTER);
        JLabel footerLabel = new JLabel("ORDER MANAGEMENT SYSTEM  |  ORDER RECORDS");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footerLabel.setForeground(MUTED);
        mainPanel.add(footerLabel, BorderLayout.SOUTH);
        refreshData();
    }

    /**
     * Compiles the structural visual selectors and control panels for order configuration.
     *
     * @return a configured operational control layout {@link JPanel}
     */
    private JPanel createOrderPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 4, 16, 10));
        panel.setOpaque(false);
        JLabel[] labels = {new JLabel("CLIENT"), new JLabel("PRODUCT"), new JLabel("QUANTITY"), new JLabel("ACTIONS")};
        for (JLabel label : labels) {
            label.setForeground(MUTED);
            label.setFont(new Font("Segoe UI", Font.BOLD, 11));
            panel.add(label);
        }
        styleComboBox(clientComboBox);
        styleComboBox(productComboBox);
        quantityField.setBackground(PANEL);
        quantityField.setForeground(TEXT);
        quantityField.setCaretColor(TEXT);
        quantityField.setSelectionColor(ACCENT);
        quantityField.setSelectedTextColor(Color.WHITE);
        quantityField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        quantityField.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER), new EmptyBorder(10, 12, 10, 12)));
        panel.add(clientComboBox);
        panel.add(productComboBox);
        panel.add(quantityField);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonPanel.setOpaque(false);
        JButton createButton = createStyledButton("Create Order", ACCENT);
        JButton refreshButton = createStyledButton("Refresh", new Color(65, 72, 90));
        createButton.addActionListener(e -> createOrder());
        refreshButton.addActionListener(e -> refreshData());
        buttonPanel.add(createButton);
        buttonPanel.add(refreshButton);
        panel.add(buttonPanel);
        return panel;
    }

    private void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setBackground(PANEL);
        comboBox.setForeground(TEXT);
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboBox.setBorder(BorderFactory.createLineBorder(BORDER));
        comboBox.setPreferredSize(new Dimension(100, 42));
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBackground(isSelected ? ACCENT : PANEL);
                setForeground(TEXT);
                setBorder(new EmptyBorder(8, 12, 8, 12));
                return this;
            }
        });
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
        button.setPreferredSize(new Dimension(125, 42));
        return button;
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
            orderBLL.createOrder(selectedClient.getId(), selectedProduct.getId(), quantity);
            quantityField.setText("");
            refreshData();
            JOptionPane.showMessageDialog(this, "Order created successfully.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),"Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
