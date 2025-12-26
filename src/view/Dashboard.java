package view;

import javax.swing.*;
import java.awt.*;
import dao.ProdukDAO;
import dao.SupplierDAO;
import dao.TransaksiDAO;
import view.panel.ProdukPanel;
import view.panel.SupplierPanel;
import view.panel.TransaksiPanel;
import view.websocket.RealtimeClient;

public class Dashboard extends JFrame {
    // Tombol harus di sini seperti asli
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

    // Panel yang sudah dipisah
    private ProdukPanel produkPanel;
    private SupplierPanel supplierPanel;
    private TransaksiPanel transaksiPanel;

    // Welcome panel dengan desain yang lebih menarik
    private JPanel welcomePanel;

    public Dashboard(String role) {
        setTitle("Dashboard - " + role);
        setSize(1000, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Set icon untuk window
        try {
            // Anda bisa tambahkan icon jika punya
            // setIconImage(new ImageIcon("path/to/icon.png").getImage());
        } catch (Exception e) {
            // Ignore jika icon tidak ada
        }

        // Sidebar - HARUS dibuat di sini seperti asli
        JPanel sidebar = createSidebar();
        add(sidebar, BorderLayout.WEST);

        // Content Panel - seperti asli
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(232, 245, 233));

        // Welcome message - LEBIH MENARIK TAPI TIDAK TERLALU FANCY
        welcomePanel = createWelcomePanel();
        contentPanel.add(welcomePanel, BorderLayout.CENTER);
        add(contentPanel, BorderLayout.CENTER);

        // Action listener langsung seperti asli
        btnProduk.addActionListener(e -> tampilkanDataProduk());
        btnSupplier.addActionListener(e -> tampilkanDataSupplier());
        btnTransaksi.addActionListener(e -> tampilkanDataTransaksi());
        btnLogout.addActionListener(e -> logout());

        initializeWebSocket();
    }

    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(232, 245, 233));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 20, 40, 20));

        // Panel utama dengan GridBagLayout untuk lebih fleksibel
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(232, 245, 233));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Judul utama
        JLabel lblTitle = new JLabel("SELAMAT DATANG");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitle.setForeground(new Color(27, 94, 32));
        centerPanel.add(lblTitle, gbc);

        // Sub judul
        JLabel lblSubtitle = new JLabel("Sistem Manajemen Toko Bunga Wilis");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblSubtitle.setForeground(new Color(56, 142, 60));
        centerPanel.add(lblSubtitle, gbc);

        // Garis pemisah
        JSeparator separator = new JSeparator();
        separator.setPreferredSize(new Dimension(300, 2));
        separator.setBackground(new Color(27, 94, 32));
        separator.setForeground(new Color(27, 94, 32));
        centerPanel.add(separator, gbc);

        // Info status
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        statusPanel.setBackground(new Color(232, 245, 233));
        statusPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JLabel lblStatus = new JLabel("Status: ");
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblStatus.setForeground(new Color(27, 94, 32));
        statusPanel.add(lblStatus);

        JLabel lblStatusValue = new JLabel("Sistem Siap Digunakan");
        lblStatusValue.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblStatusValue.setForeground(new Color(56, 142, 60));
        statusPanel.add(lblStatusValue);

        gbc.gridy = 5;
        centerPanel.add(statusPanel, gbc);

        panel.add(centerPanel, BorderLayout.CENTER);

        // Footer dengan copyright
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(new Color(200, 230, 201));
        footerPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(27, 94, 32)));

        JLabel lblFooter = new JLabel("© 2025 Toko Bunga Wilis - Versi 1.0");
        lblFooter.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblFooter.setForeground(new Color(27, 94, 32));
        footerPanel.add(lblFooter);

        panel.add(footerPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createSidebar() {
        // SAMA PERSIS dengan kode asli
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(27, 94, 32));
        sidebar.setPreferredSize(new Dimension(220, getHeight()));

        JLabel lblSidebarTitle = new JLabel("Toko bunga wilis", SwingConstants.CENTER);
        lblSidebarTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblSidebarTitle.setForeground(new Color(200, 230, 201));
        lblSidebarTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblSidebarTitle.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        sidebar.add(lblSidebarTitle);

        // Tambahkan garis pemisah di sidebar
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(200, 230, 201));
        separator.setMaximumSize(new Dimension(180, 1));
        sidebar.add(separator);
        sidebar.add(Box.createVerticalStrut(10));

        styleSidebarButton(btnProduk);
        styleSidebarButton(btnSupplier);
        styleSidebarButton(btnTransaksi);
        styleSidebarButton(btnLogout);

        sidebar.add(Box.createVerticalGlue());
        sidebar.add(btnProduk);
        sidebar.add(Box.createVerticalStrut(15));
        sidebar.add(btnSupplier);
        sidebar.add(Box.createVerticalStrut(15));
        sidebar.add(btnTransaksi);
        sidebar.add(Box.createVerticalStrut(15));
        sidebar.add(btnLogout);
        sidebar.add(Box.createVerticalGlue());

        // Tambahkan info di bagian bawah sidebar
        JPanel sidebarFooter = new JPanel();
        sidebarFooter.setLayout(new BoxLayout(sidebarFooter, BoxLayout.Y_AXIS));
        sidebarFooter.setBackground(new Color(27, 94, 32));
        sidebarFooter.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        sidebarFooter.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSeparator footerSeparator = new JSeparator();
        footerSeparator.setForeground(new Color(200, 230, 201));
        footerSeparator.setMaximumSize(new Dimension(180, 1));
        sidebarFooter.add(footerSeparator);
        sidebarFooter.add(Box.createVerticalStrut(10));

        JLabel lblFooterInfo = new JLabel("<html><div style='text-align: center; color: #C8E6C9; font-size: 10px;'>" +
                "© 2025 Toko Bunga Wilis</div></html>");
        lblFooterInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebarFooter.add(lblFooterInfo);

        sidebar.add(sidebarFooter);

        return sidebar;
    }

    private void initializeWebSocket() {
        new Thread(() -> {
            try {
                realtimeClient = new RealtimeClient(new RealtimeClient.MessageHandler() {
                    @Override
                    public void onMessage(String message) {
                        handleWebSocketMessage(message);
                    }
                });
                realtimeClient.connect();
            } catch (Exception e) {
                System.err.println("WebSocket gagal: " + e.getMessage());
            }
        }).start();
    }

    private void tampilkanDataProduk() {
        currentView = "produk";
        contentPanel.removeAll();

        // Header - HARUS dibuat langsung di sini
        JLabel lblJudul = new JLabel("Data Produk", SwingConstants.CENTER);
        lblJudul.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblJudul.setForeground(new Color(27, 94, 32));
        lblJudul.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        contentPanel.add(lblJudul, BorderLayout.NORTH);

        // Gunakan ProdukPanel yang sudah dipisah
        if (produkPanel == null) {
            produkPanel = new ProdukPanel(produkDAO, realtimeClient);
        }

        contentPanel.add(produkPanel, BorderLayout.CENTER);
        refreshContent();
    }

    private void tampilkanDataSupplier() {
        currentView = "supplier";
        contentPanel.removeAll();

        JLabel lblJudul = new JLabel("Data Supplier", SwingConstants.CENTER);
        lblJudul.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblJudul.setForeground(new Color(27, 94, 32));
        lblJudul.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        contentPanel.add(lblJudul, BorderLayout.NORTH);

        if (supplierPanel == null) {
            supplierPanel = new SupplierPanel(supplierDAO, realtimeClient);
        }

        contentPanel.add(supplierPanel, BorderLayout.CENTER);
        refreshContent();
    }

    private void tampilkanDataTransaksi() {
        currentView = "transaksi";
        contentPanel.removeAll();

        JLabel lblJudul = new JLabel("Data Transaksi", SwingConstants.CENTER);
        lblJudul.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblJudul.setForeground(new Color(27, 94, 32));
        lblJudul.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        contentPanel.add(lblJudul, BorderLayout.NORTH);

        if (transaksiPanel == null) {
            transaksiPanel = new TransaksiPanel(transaksiDAO, realtimeClient);
        }

        contentPanel.add(transaksiPanel, BorderLayout.CENTER);
        refreshContent();
    }

    private void handleWebSocketMessage(String message) {
        if (message.startsWith("SERVER:")) {
            return;
        }

        String[] parts = message.split(":", 3);
        if (parts.length < 2)
            return;

        String entity = parts[0];
        String action = parts[1];

        // Gunakan ToastNotification yang sudah dipisah
        view.component.ToastNotification.show(this, entity + " " + action.toLowerCase());

        if ((entity.equals("PRODUK") && currentView.equals("produk")) ||
                (entity.equals("SUPPLIER") && currentView.equals("supplier")) ||
                (entity.equals("TRANSAKSI") && currentView.equals("transaksi"))) {
            refreshCurrentView();
        }
    }

    private void refreshCurrentView() {
        switch (currentView) {
            case "produk":
                if (produkPanel != null)
                    produkPanel.refreshData();
                break;
            case "supplier":
                if (supplierPanel != null)
                    supplierPanel.refreshData();
                break;
            case "transaksi":
                if (transaksiPanel != null)
                    transaksiPanel.refreshData();
                break;
        }
    }

    private void logout() {
        if (realtimeClient != null) {
            realtimeClient.close();
        }
        dispose();
        new LoginForm().setVisible(true);
    }

    private void styleSidebarButton(JButton btn) {
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(190, 50));
        btn.setBackground(new Color(200, 230, 201));
        btn.setForeground(new Color(27, 94, 32));
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 30));

        // Tambahkan hover effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(165, 214, 167));
                btn.setForeground(new Color(27, 94, 32));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(200, 230, 201));
                btn.setForeground(new Color(27, 94, 32));
            }
        });
    }

    private void refreshContent() {
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    @Override
    public void dispose() {
        if (realtimeClient != null) {
            realtimeClient.close();
        }
        super.dispose();
    }
}