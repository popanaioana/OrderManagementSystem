
package com.pt.presentation;

import com.pt.dataAccessLayer.BillDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Presentation Layer window designed to display historical commercial invoices (Bills).
 * Uses reflection mapping to populate logged bills read from the log table inside a read-only viewer.
 */
public class BillFrame extends JFrame {

    private final BillDAO billDAO = new BillDAO();
    private final JTable table = new JTable();

    private static final Color BACKGROUND = new Color(23, 27, 36);
    private static final Color PANEL = new Color(31, 36, 48);
    private static final Color TABLE_BACKGROUND = new Color(38, 44, 58);
    private static final Color ACCENT = new Color(99, 102, 241);
    private static final Color TEXT = new Color(240, 242, 248);
    private static final Color MUTED = new Color(155, 163, 180);
    private static final Color BORDER = new Color(57, 64, 80);

    /**
     * Constructs and initializes the system financial bill logs interface viewer.
     */
    public BillFrame() {
        setTitle("OrderManagementSystem | Bill History");
        setSize(1050, 650);
        setMinimumSize(new Dimension(800, 500));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel mainPanel = new JPanel(new BorderLayout(0, 28)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gradient = new GradientPaint(0, 0, new Color(23, 27, 36), getWidth(), getHeight(), new Color(35, 34, 57));
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        mainPanel.setBorder(new EmptyBorder(35, 40, 30, 40));
        setContentPane(mainPanel);
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JLabel titleLabel = new JLabel("Bill History");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        titleLabel.setForeground(TEXT);
        JLabel subtitleLabel = new JLabel("View your historical order invoices");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(MUTED);
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(8));
        titlePanel.add(subtitleLabel);
        headerPanel.add(titlePanel, BorderLayout.WEST);
        JButton refreshButton = new JButton("Refresh Data") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color start = getModel().isRollover() ? new Color(120, 123, 255) : ACCENT;
                Color end = getModel().isPressed() ? new Color(65, 68, 190) : new Color(79, 82, 210);
                g2.setPaint(new GradientPaint(0, 0, start,0, getHeight(), end));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 22, 22);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setContentAreaFilled(false);
        refreshButton.setBorderPainted(false);
        refreshButton.setFocusPainted(false);
        refreshButton.setRolloverEnabled(true);
        refreshButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        refreshButton.setPreferredSize(new Dimension(160, 44));
        refreshButton.addActionListener(e -> refresh());
        headerPanel.add(refreshButton, BorderLayout.EAST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);
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
                        super.getTableCellRendererComponent(table, value, isSelected,false, row, column);
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
                        label.setHorizontalAlignment(SwingConstants.LEFT);
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
                if (bounds.isEmpty()) {
                    return;
                }
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(99, 102, 180));
                g2.fillRoundRect(bounds.x + 3, bounds.y + 3, Math.max(0, bounds.width - 6), Math.max(0, bounds.height - 6),12, 12);
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
                g2.fillRoundRect(0, 0, getWidth(), getHeight(),22, 22);
                g2.dispose();
            }
        };
        tableContainer.setOpaque(false);
        tableContainer.setBorder(new EmptyBorder(1, 1, 1, 1));
        scrollPane.setOpaque(false);
        JPanel roundedContent = new JPanel(new BorderLayout()) {
            @Override
            protected void paintChildren(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setClip(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(),22, 22));
                super.paintChildren(g2);
                g2.dispose();
            }
        };
        roundedContent.setOpaque(false);
        roundedContent.add(scrollPane, BorderLayout.CENTER);
        tableContainer.add(roundedContent, BorderLayout.CENTER);
        mainPanel.add(tableContainer, BorderLayout.CENTER);
        JLabel footerLabel = new JLabel("ORDER MANAGEMENT SYSTEM  |  BILL RECORDS");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footerLabel.setForeground(MUTED);
        mainPanel.add(footerLabel, BorderLayout.SOUTH);
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
