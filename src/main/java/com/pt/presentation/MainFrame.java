
package com.pt.presentation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.*;

/**
 * Presentation Layer class representing the primary dashboard window of the application.
 * It serves as the main entry point for the graphical user interface, providing navigation buttons
 * to access client management, product management, order processing, and log viewing modules.
 */
public class MainFrame extends JFrame {

    private static final Color BACKGROUND = new Color(23, 27, 36);
    private static final Color PANEL = new Color(31, 36, 48);
    private static final Color CARD = new Color(38, 44, 58);
    private static final Color ACCENT = new Color(99, 102, 241);
    private static final Color TEXT = new Color(240, 242, 248);
    private static final Color MUTED = new Color(155, 163, 180);
    private static final Color BORDER = new Color(57, 64, 80);

    private JPanel sidebar;
    private JPanel root;
    private boolean sidebarOpen = false;

    /**
     * Constructs a new MainFrame dashboard window.
     * Initializes the frame layout, configures standard close behaviors, and maps navigation event listeners.
     */
    public MainFrame() {
        setTitle("OrderManagementSystem | Dashboard");
        setSize(1100, 820);
        setMinimumSize(new Dimension(850, 580));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        root = new JPanel(new BorderLayout());
        root.setBackground(BACKGROUND);
        setContentPane(root);
        sidebar = createSidebar();
        sidebar.setVisible(false);
        root.add(sidebar, BorderLayout.WEST);
        root.add(createDashboard(), BorderLayout.CENTER);
    }

    private void toggleSidebar() {
        sidebarOpen = !sidebarOpen;
        sidebar.setVisible(sidebarOpen);
        root.revalidate();
        root.repaint();
    }

    private JPanel createSidebar(){
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PANEL);
        panel.setPreferredSize(new Dimension(225, 0));
        panel.setBorder(new EmptyBorder(30, 18, 25, 18));
        JPanel navigation = new JPanel();
        navigation.setOpaque(false);
        navigation.setLayout(new BoxLayout(navigation, BoxLayout.Y_AXIS));
        JLabel logo = new JLabel("OrderMS");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 23));
        logo.setForeground(TEXT);
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel subtitle = new JLabel("MANAGEMENT SYSTEM");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        subtitle.setForeground(MUTED);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        navigation.add(logo);
        navigation.add(Box.createVerticalStrut(6));
        navigation.add(subtitle);
        navigation.add(Box.createVerticalStrut(40));
        JButton dashboardButton = createSidebarButton("Dashboard", true);
        JButton clientsButton = createSidebarButton("Customers", false);
        JButton productsButton = createSidebarButton("Products", false);
        JButton ordersButton = createSidebarButton("Orders", false);
        JButton billsButton = createSidebarButton("Bills", false);
        dashboardButton.addActionListener(e -> toggleSidebar());
        clientsButton.addActionListener(e -> new ClientFrame().setVisible(true));
        productsButton.addActionListener(e -> new ProductFrame().setVisible(true));
        ordersButton.addActionListener(e -> new OrderFrame().setVisible(true));
        billsButton.addActionListener(e -> new BillFrame().setVisible(true));
        for (JButton button : new JButton[]{
                dashboardButton, clientsButton,
                productsButton, ordersButton, billsButton
        }) {
            button.setAlignmentX(Component.LEFT_ALIGNMENT);
            navigation.add(button);
            navigation.add(Box.createVerticalStrut(10));
        }
        panel.add(navigation, BorderLayout.NORTH);
        JButton exitButton = createSidebarButton("Exit", false);
        exitButton.setForeground(new Color(240, 125, 145));
        exitButton.addActionListener(e -> System.exit(0));
        panel.add(exitButton, BorderLayout.SOUTH);
        return panel;
    }

    private JButton createSidebarButton(String text, boolean active) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (active || getModel().isRollover()) {
                    g2.setColor(active ? new Color(65, 66, 105) : new Color(47, 52, 69));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(active ? Color.WHITE : MUTED);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(new EmptyBorder(0, 18, 0, 10));
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setRolloverEnabled(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        button.setPreferredSize(new Dimension(185, 44));
        return button;
    }

    private JPanel createDashboard() {
        JPanel dashboard = new JPanel(new BorderLayout(0, 20)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setPaint(new GradientPaint(0, 0, BACKGROUND, getWidth(), getHeight(), new Color(35, 34, 57)));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        dashboard.setBorder(new EmptyBorder(28, 38, 25, 38));
        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        JPanel topBar = new JPanel(new BorderLayout(18, 0));
        topBar.setOpaque(false);
        topBar.setAlignmentX(Component.LEFT_ALIGNMENT);
        topBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 54));
        JButton menuButton = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2.setColor(new Color(48, 54, 73));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                }
                g2.setColor(TEXT);
                g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                int x = (getWidth() - 22) / 2;
                int y = (getHeight() - 16) / 2;
                for (int i = 0; i < 3; i++) {
                    g2.drawLine(x, y + i * 8, x + 22, y + i * 8);
                }
                g2.dispose();
            }
        };
        menuButton.setPreferredSize(new Dimension(48, 48));
        menuButton.setContentAreaFilled(false);
        menuButton.setBorderPainted(false);
        menuButton.setFocusPainted(false);
        menuButton.setRolloverEnabled(true);
        menuButton.setToolTipText("Toggle navigation menu");
        menuButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        menuButton.addActionListener(e -> toggleSidebar());
        JLabel appName = new JLabel("OrderMS");
        appName.setFont(new Font("Segoe UI", Font.BOLD, 16));
        appName.setForeground(MUTED);
        topBar.add(menuButton, BorderLayout.WEST);
        topBar.add(appName, BorderLayout.CENTER);
        content.add(topBar);
        content.add(Box.createVerticalStrut(22));
        JLabel title = new JLabel("Dashboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(TEXT);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel subtitle = new JLabel("Welcome to your Order Management System");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(MUTED);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(title);
        content.add(Box.createVerticalStrut(8));
        content.add(subtitle);
        content.add(Box.createVerticalStrut(28));
        JPanel banner = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, new Color(66, 63, 120), getWidth(), getHeight(), new Color(43, 45, 76)));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                g2.dispose();
            }
        };
        banner.setOpaque(false);
        banner.setLayout(new BoxLayout(banner, BoxLayout.Y_AXIS));
        banner.setBorder(new EmptyBorder(24, 28, 24, 28));
        banner.setPreferredSize(new Dimension(0, 115));
        banner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 115));
        banner.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel bannerTitle = new JLabel("Manage your business");
        bannerTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        bannerTitle.setForeground(Color.WHITE);
        bannerTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel bannerText = new JLabel("Customers, inventory, orders and billing in one place.");
        bannerText.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        bannerText.setForeground(new Color(210, 210, 235));
        bannerText.setAlignmentX(Component.LEFT_ALIGNMENT);
        banner.add(bannerTitle);
        banner.add(Box.createVerticalStrut(9));
        banner.add(bannerText);
        content.add(banner);
        content.add(Box.createVerticalStrut(30));
        JLabel sectionTitle = new JLabel("Quick Access");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 19));
        sectionTitle.setForeground(TEXT);
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(sectionTitle);
        content.add(Box.createVerticalStrut(18));
        JPanel cardGrid = new JPanel(new GridLayout(2, 2, 18, 18));
        cardGrid.setOpaque(false);
        cardGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardGrid.setPreferredSize(new Dimension(0, 300));
        cardGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
        cardGrid.add(createNavigationCard("Customers", "Manage customer information","person", () -> new ClientFrame().setVisible(true)));
        cardGrid.add(createNavigationCard("Products", "Manage inventory and stock", "product", () -> new ProductFrame().setVisible(true)));
        cardGrid.add(createNavigationCard("Orders", "Create and view orders", "package", () -> new OrderFrame().setVisible(true)));
        cardGrid.add(createNavigationCard("Bills", "View billing history", "receipt", () -> new BillFrame().setVisible(true)));
        content.add(cardGrid);
        dashboard.add(content, BorderLayout.NORTH);
        JLabel footer = new JLabel("ORDER MANAGEMENT SYSTEM  |  DASHBOARD");
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footer.setForeground(MUTED);
        dashboard.add(footer, BorderLayout.SOUTH);
        return dashboard;
    }

    private JButton createNavigationCard(String title, String description, String iconType, Runnable action) {
        JButton card = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? new Color(48, 54, 73) : CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 22, 22);
                g2.setColor(getModel().isRollover() ? ACCENT : BORDER);
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(0, 0, getWidth() - 1,getHeight() - 1, 22, 22);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setLayout(new BorderLayout(12, 0));
        card.setContentAreaFilled(false);
        card.setFocusPainted(false);
        card.setRolloverEnabled(true);
        card.setBorderPainted(false);
        card.setBorder(new EmptyBorder(18, 20, 18, 20));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        JPanel details = new JPanel();
        details.setOpaque(false);
        details.setLayout(new BoxLayout(details, BoxLayout.Y_AXIS));
        JComponent icon = createIcon(iconType);
        icon.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel cardTitle = new JLabel(title);
        cardTitle.setFont(new Font("Segoe UI", Font.BOLD, 17));
        cardTitle.setForeground(TEXT);
        cardTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel cardDescription = new JLabel(description);
        cardDescription.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cardDescription.setForeground(MUTED);
        cardDescription.setAlignmentX(Component.LEFT_ALIGNMENT);
        details.add(icon);
        details.add(Box.createVerticalStrut(12));
        details.add(cardTitle);
        details.add(Box.createVerticalStrut(5));
        details.add(cardDescription);
        card.add(details, BorderLayout.CENTER);
        JComponent arrow = new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(180, 181, 255));
                g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                int x = getWidth() / 2;
                int y = getHeight() / 2;
                g2.drawLine(x - 7, y + 7, x + 7, y - 7);
                g2.drawLine(x - 1, y - 7, x + 7, y - 7);
                g2.drawLine(x + 7, y - 7, x + 7, y + 1);
                g2.dispose();
            }
        };
        arrow.setPreferredSize(new Dimension(22, 22));
        card.add(arrow, BorderLayout.EAST);
        card.addActionListener(e -> action.run());
        return card;
    }

    private JComponent createIcon(String type) {
        JComponent icon = new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(58, 58, 99));
                g2.fillRoundRect(0, 0, 48, 48, 14, 14);
                g2.setColor(new Color(180, 181, 255));
                g2.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                switch (type) {
                    case "person":
                        g2.drawOval(18, 9, 12, 12);
                        g2.drawArc(10, 24, 28, 20, 0, 180);
                        break;
                    case "product":
                        g2.drawRoundRect(11, 15, 26, 23, 3, 3);
                        g2.drawLine(11, 22, 37, 22);
                        g2.drawLine(20, 15, 20, 22);
                        g2.drawLine(28, 15, 28, 22);
                        break;
                    case "package":
                        Path2D parcel = new Path2D.Double();
                        parcel.moveTo(24, 9);
                        parcel.lineTo(38, 17);
                        parcel.lineTo(38, 33);
                        parcel.lineTo(24, 41);
                        parcel.lineTo(10, 33);
                        parcel.lineTo(10, 17);
                        parcel.closePath();
                        g2.draw(parcel);
                        g2.drawLine(10, 17, 24, 25);
                        g2.drawLine(38, 17, 24, 25);
                        g2.drawLine(24, 25, 24, 41);
                        break;
                    case "receipt":
                        Path2D receipt = new Path2D.Double();
                        receipt.moveTo(14, 8);
                        receipt.lineTo(34, 8);
                        receipt.lineTo(34, 40);
                        receipt.lineTo(29, 37);
                        receipt.lineTo(24, 40);
                        receipt.lineTo(19, 37);
                        receipt.lineTo(14, 40);
                        receipt.closePath();
                        g2.draw(receipt);
                        g2.drawLine(19, 17, 29, 17);
                        g2.drawLine(19, 23, 29, 23);
                        g2.drawLine(19, 29, 26, 29);
                        break;
                }
                g2.dispose();
            }
        };

        icon.setPreferredSize(new Dimension(48, 48));
        icon.setMaximumSize(new Dimension(48, 48));
        icon.setMinimumSize(new Dimension(48, 48));

        return icon;
    }
}
