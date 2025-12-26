package view;

import dao.ProdukDAO;
import dao.SupplierDAO;
import dao.TransaksiDAO;
import java.awt.*;
import javax.swing.*;
import view.panel.ProdukPanel;
import view.panel.SupplierPanel;
import view.panel.TransaksiPanel;
import view.websocket.RealtimeClient;

public class Dashboard extends JFrame {

    // Tombol sidebar
    private JButton btnProduk = new JButton("Produk");
    private JButton btnSupplier = new JButton("Supplier");
    private JButton btnTransaksi = new JButton("Transaksi");
    private JButton btnLogout = new JButton("Logout");

    private JPanel contentPanel;

    private ProdukDAO produkDAO = new ProdukDAO();
    private SupplierDAO supplierDAO = new SupplierDAO();
    private TransaksiDAO transaksiDAO = new TransaksiDAO();

    private RealtimeClient realtimeClient;
    private String currentView = "welcome";

    private ProdukPanel produkPanel;
    private SupplierPanel supplierPanel;
    private TransaksiPanel transaksiPanel;

    private JPanel welcomePanel;

    public Dashboard(String role) {
        setTitle("Dashboard - " + role);
        setExtendedState(JFrame.MAXIMIZED_BOTH);              // RESPONSIVE
        setMinimumSize(new Dimension(900, 600));             // AMAN DI LAYAR KECIL
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Sidebar
        add(createSidebar(), BorderLayout.WEST);

        // Content panel
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(232, 245, 233));

        welcomePanel = createWelcomePanel();
        contentPanel.add(welcomePanel, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);

        // Action
        btnProduk.addActionListener(e -> tampilkanDataProduk());
        btnSupplier.addActionListener(e -> tampilkanDataSupplier());
        btnTransaksi.addActionListener(e -> tampilkanDataTransaksi());
        btnLogout.addActionListener(e -> logout());

        initializeWebSocket();
    }

    /* ======================= WELCOME PANEL ======================= */

    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(232, 245, 233));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 20, 40, 20));

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(232, 245, 233));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel lblTitle = new JLabel("SELAMAT DATANG");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitle.setForeground(new Color(27, 94, 32));
        centerPanel.add(lblTitle, gbc);

        JLabel lblSubtitle = new JLabel("Sistem Manajemen Toko Bunga Wilis");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblSubtitle.setForeground(new Color(56, 142, 60));
        centerPanel.add(lblSubtitle, gbc);

        JSeparator separator = new JSeparator();
        separator.setPreferredSize(new Dimension(300, 2));
        centerPanel.add(separator, gbc);

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        statusPanel.setBackground(new Color(232, 245, 233));

        statusPanel.add(new JLabel("Status: "));
        statusPanel.add(new JLabel("Sistem Siap Digunakan"));

        centerPanel.add(statusPanel, gbc);

        panel.add(centerPanel, BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setBackground(new Color(200, 230, 201));
        footer.add(new JLabel("© 2025 Toko Bunga Wilis"));
        panel.add(footer, BorderLayout.SOUTH);

        return panel;
    }

    /* ======================= SIDEBAR ======================= */

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(27, 94, 32));
        sidebar.setPreferredSize(new Dimension(220, 0));   // RESPONSIVE FIX

        JLabel title = new JLabel("Toko Bunga Wilis");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(200, 230, 201));
        title.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        sidebar.add(title);

        sidebar.add(Box.createVerticalStrut(20));

        styleSidebarButton(btnProduk);
        styleSidebarButton(btnSupplier);
        styleSidebarButton(btnTransaksi);
        styleSidebarButton(btnLogout);

        sidebar.add(btnProduk);
        sidebar.add(Box.createVerticalStrut(15));
        sidebar.add(btnSupplier);
        sidebar.add(Box.createVerticalStrut(15));
        sidebar.add(btnTransaksi);
        sidebar.add(Box.createVerticalStrut(15));
        sidebar.add(btnLogout);

        sidebar.add(Box.createVerticalGlue());

        JLabel footer = new JLabel("© 2025");
        footer.setForeground(new Color(200, 230, 201));
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(footer);
        sidebar.add(Box.createVerticalStrut(15));

        return sidebar;
    }

    /* ======================= VIEW SWITCH ======================= */

    private void tampilkanDataProduk() {
        currentView = "produk";
        contentPanel.removeAll();

        contentPanel.add(createTitle("Data Produk"), BorderLayout.NORTH);

        if (produkPanel == null) {
            produkPanel = new ProdukPanel(produkDAO, realtimeClient);
        }
        contentPanel.add(produkPanel, BorderLayout.CENTER);

        refreshContent();
    }

    private void tampilkanDataSupplier() {
        currentView = "supplier";
        contentPanel.removeAll();

        contentPanel.add(createTitle("Data Supplier"), BorderLayout.NORTH);

        if (supplierPanel == null) {
            supplierPanel = new SupplierPanel(supplierDAO, realtimeClient);
        }
        contentPanel.add(supplierPanel, BorderLayout.CENTER);

        refreshContent();
    }

    private void tampilkanDataTransaksi() {
        currentView = "transaksi";
        contentPanel.removeAll();

        contentPanel.add(createTitle("Data Transaksi"), BorderLayout.NORTH);

        if (transaksiPanel == null) {
            transaksiPanel = new TransaksiPanel(transaksiDAO, realtimeClient);
        }
        contentPanel.add(transaksiPanel, BorderLayout.CENTER);

        refreshContent();
    }

    private JLabel createTitle(String text) {
        JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lbl.setForeground(new Color(27, 94, 32));
        lbl.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        return lbl;
    }

    /* ======================= WEBSOCKET ======================= */

    private void initializeWebSocket() {
        new Thread(() -> {
            try {
                realtimeClient = new RealtimeClient(this::handleWebSocketMessage);
                realtimeClient.connect();
            } catch (Exception e) {
                System.err.println("WebSocket gagal: " + e.getMessage());
            }
        }).start();
    }

    private void handleWebSocketMessage(String message) {
        if (message.startsWith("SERVER:")) return;

        String[] parts = message.split(":", 3);
        if (parts.length < 2) return;

        String entity = parts[0];

        view.component.ToastNotification.show(this, entity + " update");

        refreshCurrentView();
    }

    private void refreshCurrentView() {
        switch (currentView) {
            case "produk" -> {
                if (produkPanel != null) produkPanel.refreshData();
            }
            case "supplier" -> {
                if (supplierPanel != null) supplierPanel.refreshData();
            }
            case "transaksi" -> {
                if (transaksiPanel != null) transaksiPanel.refreshData();
            }
        }
    }

    /* ======================= UTIL ======================= */

    private void logout() {
        if (realtimeClient != null) realtimeClient.close();
        dispose();
        new LoginForm().setVisible(true);
    }

    private void styleSidebarButton(JButton btn) {
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(190, 45));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(new Color(200, 230, 201));
        btn.setForeground(new Color(27, 94, 32));
        btn.setFocusPainted(false);
    }

    private void refreshContent() {
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    @Override
    public void dispose() {
        if (realtimeClient != null) realtimeClient.close();
        super.dispose();
    }
}
