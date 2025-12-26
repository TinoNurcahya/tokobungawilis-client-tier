package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import dao.ProdukDAO;
import dao.SupplierDAO;
import dao.TransaksiDAO;
import model.Produk;
import model.Supplier;
import model.Transaksi;

public class Dashboard extends JFrame {
    private JButton btnProduk = new JButton("Produk");
    private JButton btnSupplier = new JButton("Supplier");
    private JButton btnTransaksi = new JButton("Transaksi");
    private JButton btnLogout = new JButton("Logout");

    private JPanel contentPanel;
    private ProdukDAO produkDAO = new ProdukDAO();
    private SupplierDAO supplierDAO = new SupplierDAO();
    private TransaksiDAO transaksiDAO = new TransaksiDAO();

    public Dashboard(String role) {
        setTitle("Dashboard - " + role);
        setSize(1000, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // === Sidebar dengan warna hijau tua ===
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(27, 94, 32)); // hijau tua
        sidebar.setPreferredSize(new Dimension(220, getHeight()));

        // Judul/logo di sidebar
        JLabel lblSidebarTitle = new JLabel("Infinity8", SwingConstants.CENTER);
        lblSidebarTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblSidebarTitle.setForeground(new Color(200, 230, 201)); // hijau muda
        lblSidebarTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblSidebarTitle.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        sidebar.add(lblSidebarTitle);

        // Styling tombol sidebar
        styleSidebarButton(btnProduk);
        styleSidebarButton(btnSupplier);
        styleSidebarButton(btnTransaksi);
        styleSidebarButton(btnLogout);

        // Dorong tombol ke tengah sidebar
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(btnProduk);
        sidebar.add(Box.createVerticalStrut(15));
        sidebar.add(btnSupplier);
        sidebar.add(Box.createVerticalStrut(15));
        sidebar.add(btnTransaksi);
        sidebar.add(Box.createVerticalStrut(15));
        sidebar.add(btnLogout);
        sidebar.add(Box.createVerticalGlue());

        add(sidebar, BorderLayout.WEST);

        // === Konten utama ===
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(232, 245, 233)); // hijau pastel terang

        JLabel lblWelcome = new JLabel("Selamat datang di Dashboard", SwingConstants.CENTER);
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblWelcome.setForeground(new Color(27, 94, 32)); // hijau tua

        contentPanel.add(lblWelcome, BorderLayout.CENTER);
        add(contentPanel, BorderLayout.CENTER);

        // === Aksi tombol sidebar ===
        btnProduk.addActionListener(e -> tampilkanDataProduk());
        btnSupplier.addActionListener(e -> tampilkanDataSupplier());
        btnTransaksi.addActionListener(e -> tampilkanDataTransaksi());
        btnLogout.addActionListener(e -> {
            dispose();
            new LoginForm().setVisible(true);
        });
    }

    // Styling tombol sidebar
    private void styleSidebarButton(JButton btn) {
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(190, 50)); // lebih besar
        btn.setBackground(new Color(200, 230, 201)); // hijau pastel terang
        btn.setForeground(new Color(27, 94, 32));    // font hijau tua
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 30)); // lebih ke kanan
    }

    // === PRODUK ===
    private void tampilkanDataProduk() {
        contentPanel.removeAll();
        addHeader("Data Produk");

        String[] kolom = {"ID", "Nama", "Jenis", "Harga", "Stok"};
        DefaultTableModel model = new DefaultTableModel(kolom, 0);
        JTable table = new JTable(model);

        table.setForeground(new Color(27, 94, 32));
        table.getTableHeader().setForeground(new Color(27, 94, 32));

        try {
            List<Produk> daftarProduk = produkDAO.findAll();
            for (Produk p : daftarProduk) {
                model.addRow(new Object[]{p.getId(), p.getNama(), p.getJenis(), p.getHarga(), p.getStok()});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal ambil data produk: " + ex.getMessage());
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel panelAksi = createCrudPanel();
        contentPanel.add(panelAksi, BorderLayout.SOUTH);
        refreshContent();
    }

    // === SUPPLIER ===
    private void tampilkanDataSupplier() {
        contentPanel.removeAll();
        addHeader("Data Supplier");

        String[] kolom = {"ID", "Nama", "Alamat", "Kontak"};
        DefaultTableModel model = new DefaultTableModel(kolom, 0);
        JTable table = new JTable(model);

        table.setForeground(new Color(27, 94, 32));
        table.getTableHeader().setForeground(new Color(27, 94, 32));

        try {
            List<Supplier> daftarSupplier = supplierDAO.findAll();
            for (Supplier s : daftarSupplier) {
                model.addRow(new Object[]{s.getId(), s.getNama(), s.getAlamat(), s.getKontak()});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal ambil data supplier: " + ex.getMessage());
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel panelAksi = createCrudPanel();
        contentPanel.add(panelAksi, BorderLayout.SOUTH);
        refreshContent();
    }

    // === TRANSAKSI ===
    private void tampilkanDataTransaksi() {
        contentPanel.removeAll();
        addHeader("Data Transaksi");

        String[] kolom = {"ID", "Produk ID", "Jumlah", "Total", "Pembeli"};
        DefaultTableModel model = new DefaultTableModel(kolom, 0);
        JTable table = new JTable(model);

        table.setForeground(new Color(27, 94, 32));
        table.getTableHeader().setForeground(new Color(27, 94, 32));

        try {
            List<Transaksi> daftarTransaksi = transaksiDAO.findAll();
            for (Transaksi t : daftarTransaksi) {
                model.addRow(new Object[]{t.getId(), t.getProdukId(), t.getJumlah(), t.getTotal(), t.getPembeli()});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal ambil data transaksi: " + ex.getMessage());
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel panelAksi = createCrudPanel();
        contentPanel.add(panelAksi, BorderLayout.SOUTH);
        refreshContent();
    }

    // Tambah header untuk setiap halaman
    private void addHeader(String title) {
        JLabel lblJudul = new JLabel(title, SwingConstants.CENTER);
        lblJudul.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblJudul.setForeground(new Color(27, 94, 32)); // hijau tua
        lblJudul.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        contentPanel.add(lblJudul, BorderLayout.NORTH);
    }

    // Panel CRUD dengan styling hijau
    private JPanel createCrudPanel() {
        JPanel panelAksi = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelAksi.setBackground(new Color(232, 245, 233));
        panelAksi.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JButton btnTambah = new JButton("Tambah");
        JButton btnEdit = new JButton("Edit");
        JButton btnHapus = new JButton("Hapus");

        styleCrudButton(btnTambah);
        styleCrudButton(btnEdit);
        styleCrudButton(btnHapus);

        panelAksi.add(btnTambah);
        panelAksi.add(btnEdit);
        panelAksi.add(btnHapus);

        return panelAksi;
    }

        private void styleCrudButton(JButton btn) {
        btn.setBackground(new Color(200, 230, 201)); // hijau pastel terang
        btn.setForeground(new Color(27, 94, 32));    // font hijau tua
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBorder(BorderFactory.createEmptyBorder(6, 16, 6, 16));
    }

    private void refreshContent() {
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}
