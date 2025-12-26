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

        // Sidebar
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(27, 94, 32));
        sidebar.setPreferredSize(new Dimension(220, getHeight()));

        JLabel lblSidebarTitle = new JLabel("Infinity8", SwingConstants.CENTER);
        lblSidebarTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblSidebarTitle.setForeground(new Color(200, 230, 201));
        lblSidebarTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblSidebarTitle.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        sidebar.add(lblSidebarTitle);

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

        add(sidebar, BorderLayout.WEST);

        // Content
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(232, 245, 233));

        JLabel lblWelcome = new JLabel("Selamat datang di Dashboard", SwingConstants.CENTER);
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblWelcome.setForeground(new Color(27, 94, 32));
        contentPanel.add(lblWelcome, BorderLayout.CENTER);
        add(contentPanel, BorderLayout.CENTER);

        // Actions
        btnProduk.addActionListener(e -> tampilkanDataProduk());
        btnSupplier.addActionListener(e -> tampilkanDataSupplier());
        btnTransaksi.addActionListener(e -> tampilkanDataTransaksi());
        btnLogout.addActionListener(e -> {
            dispose();
            new LoginForm().setVisible(true);
        });
    }

    private void styleSidebarButton(JButton btn) {
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(190, 50));
        btn.setBackground(new Color(200, 230, 201));
        btn.setForeground(new Color(27, 94, 32));
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 30));
    }

    // PRODUK
    private void tampilkanDataProduk() {
        contentPanel.removeAll();
        addHeader("Data Produk");

        String[] kolom = {"ID", "Nama", "Jenis", "Harga", "Stok"};
        DefaultTableModel model = new DefaultTableModel(kolom, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable table = new JTable(model);

        try {
            List<Produk> daftarProduk = produkDAO.findAll();
            for (Produk p : daftarProduk) {
                model.addRow(new Object[]{p.getId(), p.getNama(), p.getJenis(), p.getHarga(), p.getStok()});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal ambil data produk: " + ex.getMessage());
        }

        JScrollPane scrollPane = new JScrollPane(table);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel panelAksi = createCrudPanelProduk(model, table);
        contentPanel.add(panelAksi, BorderLayout.SOUTH);
        refreshContent();
    }

    // SUPPLIER
    private void tampilkanDataSupplier() {
        contentPanel.removeAll();
        addHeader("Data Supplier");

        String[] kolom = {"ID", "Nama", "Alamat", "Kontak"};
        DefaultTableModel model = new DefaultTableModel(kolom, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable table = new JTable(model);

        try {
            List<Supplier> daftarSupplier = supplierDAO.findAll();
            for (Supplier s : daftarSupplier) {
                model.addRow(new Object[]{s.getId(), s.getNama(), s.getAlamat(), s.getKontak()});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal ambil data supplier: " + ex.getMessage());
        }

        JScrollPane scrollPane = new JScrollPane(table);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel panelAksi = createCrudPanelSupplier(model, table);
        contentPanel.add(panelAksi, BorderLayout.SOUTH);
        refreshContent();
    }

    // TRANSAKSI
    private void tampilkanDataTransaksi() {
        contentPanel.removeAll();
        addHeader("Data Transaksi");

        String[] kolom = {"ID", "Produk ID", "Jumlah", "Total", "Pembeli"};
        DefaultTableModel model = new DefaultTableModel(kolom, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable table = new JTable(model);

        try {
            List<Transaksi> daftarTransaksi = transaksiDAO.findAll();
            for (Transaksi t : daftarTransaksi) {
                model.addRow(new Object[]{t.getId(), t.getProdukId(), t.getJumlah(), t.getTotal(), t.getPembeli()});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal ambil data transaksi: " + ex.getMessage());
        }

        JScrollPane scrollPane = new JScrollPane(table);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel panelAksi = createCrudPanelTransaksi(model, table);
        contentPanel.add(panelAksi, BorderLayout.SOUTH);
        refreshContent();
    }

    private void addHeader(String title) {
        JLabel lblJudul = new JLabel(title, SwingConstants.CENTER);
        lblJudul.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblJudul.setForeground(new Color(27, 94, 32));
        lblJudul.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        contentPanel.add(lblJudul, BorderLayout.NORTH);
    }

    // CRUD Panel Produk
    private JPanel createCrudPanelProduk(DefaultTableModel model, JTable table) {
        JPanel panelAksi = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnTambah = new JButton("Tambah");
        JButton btnEdit = new JButton("Edit");
        JButton btnHapus = new JButton("Hapus");
        styleCrudButton(btnTambah); styleCrudButton(btnEdit); styleCrudButton(btnHapus);

        btnTambah.addActionListener(e -> {
            JTextField namaField = new JTextField();
            JTextField jenisField = new JTextField();
            JTextField hargaField = new JTextField();
            JTextField stokField = new JTextField();

            Object[] message = {"Nama:", namaField, "Jenis:", jenisField, "Harga:", hargaField, "Stok:", stokField};
            int option = JOptionPane.showConfirmDialog(this, message, "Tambah Produk", JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                try {
                    Produk p = new Produk(
                        0,
                        namaField.getText(),
                        jenisField.getText(),
                        Double.parseDouble(hargaField.getText()),
                        Integer.parseInt(stokField.getText())
                    );
                    produkDAO.insert(p);
                    // Refresh data untuk memastikan id auto-increment terambil
                    reloadProdukTable(model);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Gagal tambah produk: " + ex.getMessage());
                }
            }
        });

        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int id = (int) model.getValueAt(row, 0);

                JTextField namaField = new JTextField(String.valueOf(model.getValueAt(row, 1)));
                JTextField jenisField = new JTextField(String.valueOf(model.getValueAt(row, 2)));
                JTextField hargaField = new JTextField(String.valueOf(model.getValueAt(row, 3)));
                JTextField stokField = new JTextField(String.valueOf(model.getValueAt(row, 4)));

                Object[] message = {"Nama:", namaField, "Jenis:", jenisField, "Harga:", hargaField, "Stok:", stokField};
                int option = JOptionPane.showConfirmDialog(this, message, "Edit Produk", JOptionPane.OK_CANCEL_OPTION);

                if (option == JOptionPane.OK_OPTION) {
                    try {
                        Produk p = new Produk(
                            id,
                            namaField.getText(),
                            jenisField.getText(),
                            Double.parseDouble(hargaField.getText()),
                            Integer.parseInt(stokField.getText())
                        );
                        produkDAO.update(p);
                        reloadProdukTable(model);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Gagal edit produk: " + ex.getMessage());
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Pilih baris dulu untuk edit.");
            }
        });

        btnHapus.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int id = (int) model.getValueAt(row, 0);
                int confirm = JOptionPane.showConfirmDialog(this, "Hapus produk ID " + id + "?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        produkDAO.delete(id);
                        reloadProdukTable(model);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Gagal hapus produk: " + ex.getMessage());
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Pilih baris dulu untuk hapus.");
            }
        });

        panelAksi.add(btnTambah); panelAksi.add(btnEdit); panelAksi.add(btnHapus);
        return panelAksi;
    }

    // CRUD Panel Supplier
    private JPanel createCrudPanelSupplier(DefaultTableModel model, JTable table) {
        JPanel panelAksi = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnTambah = new JButton("Tambah");
        JButton btnEdit = new JButton("Edit");
        JButton btnHapus = new JButton("Hapus");
        styleCrudButton(btnTambah); styleCrudButton(btnEdit); styleCrudButton(btnHapus);

        btnTambah.addActionListener(e -> {
            JTextField namaField = new JTextField();
            JTextField alamatField = new JTextField();
            JTextField kontakField = new JTextField();

            Object[] message = {"Nama:", namaField, "Alamat:", alamatField, "Kontak:", kontakField};
            int option = JOptionPane.showConfirmDialog(this, message, "Tambah Supplier", JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                try {
                    Supplier s = new Supplier(0, namaField.getText(), alamatField.getText(), kontakField.getText());
                    supplierDAO.insert(s);
                    reloadSupplierTable(model);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Gagal tambah supplier: " + ex.getMessage());
                }
            }
        });

        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int id = (int) model.getValueAt(row, 0);

                JTextField namaField = new JTextField(String.valueOf(model.getValueAt(row, 1)));
                JTextField alamatField = new JTextField(String.valueOf(model.getValueAt(row, 2)));
                JTextField kontakField = new JTextField(String.valueOf(model.getValueAt(row, 3)));

                Object[] message = {"Nama:", namaField, "Alamat:", alamatField, "Kontak:", kontakField};
                int option = JOptionPane.showConfirmDialog(this, message, "Edit Supplier", JOptionPane.OK_CANCEL_OPTION);

                if (option == JOptionPane.OK_OPTION) {
                    try {
                        Supplier s = new Supplier(id, namaField.getText(), alamatField.getText(), kontakField.getText());
                        supplierDAO.update(s);
                        reloadSupplierTable(model);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Gagal edit supplier: " + ex.getMessage());
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Pilih baris dulu untuk edit.");
            }
        });

        btnHapus.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int id = (int) model.getValueAt(row, 0);
                int confirm = JOptionPane.showConfirmDialog(this, "Hapus supplier ID " + id + "?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        supplierDAO.delete(id);
                        reloadSupplierTable(model);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Gagal hapus supplier: " + ex.getMessage());
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Pilih baris dulu untuk hapus.");
            }
        });

        panelAksi.add(btnTambah); panelAksi.add(btnEdit); panelAksi.add(btnHapus);
        return panelAksi;
    }

    // CRUD Panel Transaksi
    private JPanel createCrudPanelTransaksi(DefaultTableModel model, JTable table) {
        JPanel panelAksi = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnTambah = new JButton("Tambah");
        JButton btnEdit = new JButton("Edit");
        JButton btnHapus = new JButton("Hapus");
        styleCrudButton(btnTambah); styleCrudButton(btnEdit); styleCrudButton(btnHapus);

        btnTambah.addActionListener(e -> {
            JTextField produkIdField = new JTextField();
            JTextField jumlahField = new JTextField();
            JTextField totalField = new JTextField();
            JTextField pembeliField = new JTextField();

            Object[] message = {"Produk ID:", produkIdField, "Jumlah:", jumlahField, "Total:", totalField, "Pembeli:", pembeliField};
            int option = JOptionPane.showConfirmDialog(this, message, "Tambah Transaksi", JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                try {
                    Transaksi t = new Transaksi(
                        0,
                        Integer.parseInt(produkIdField.getText()),
                        Integer.parseInt(jumlahField.getText()),
                        Double.parseDouble(totalField.getText()),
                        pembeliField.getText()
                    );
                    transaksiDAO.insert(t);
                    reloadTransaksiTable(model);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Gagal tambah transaksi: " + ex.getMessage());
                }
            }
        });

        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int id = (int) model.getValueAt(row, 0);

                JTextField produkIdField = new JTextField(String.valueOf(model.getValueAt(row, 1)));
                JTextField jumlahField = new JTextField(String.valueOf(model.getValueAt(row, 2)));
                JTextField totalField = new JTextField(String.valueOf(model.getValueAt(row, 3)));
                JTextField pembeliField = new JTextField(String.valueOf(model.getValueAt(row, 4)));

                Object[] message = {"Produk ID:", produkIdField, "Jumlah:", jumlahField, "Total:", totalField, "Pembeli:", pembeliField};
                int option = JOptionPane.showConfirmDialog(this, message, "Edit Transaksi", JOptionPane.OK_CANCEL_OPTION);

                if (option == JOptionPane.OK_OPTION) {
                    try {
                        Transaksi t = new Transaksi(
                            id,
                            Integer.parseInt(produkIdField.getText()),
                            Integer.parseInt(jumlahField.getText()),
                            Double.parseDouble(totalField.getText()),
                            pembeliField.getText()
                        );
                        transaksiDAO.update(t);
                        reloadTransaksiTable(model);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Gagal edit transaksi: " + ex.getMessage());
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Pilih baris dulu untuk edit.");
            }
        });

        btnHapus.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int id = (int) model.getValueAt(row, 0);
                int confirm = JOptionPane.showConfirmDialog(this, "Hapus transaksi ID " + id + "?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        transaksiDAO.delete(id);
                        reloadTransaksiTable(model);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Gagal hapus transaksi: " + ex.getMessage());
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Pilih baris dulu untuk hapus.");
            }
        });

        panelAksi.add(btnTambah); panelAksi.add(btnEdit); panelAksi.add(btnHapus);
        return panelAksi;
    }

    // Helpers: Reload tables to ensure data stays in sync with DB and auto-increment IDs are reflected
    private void reloadProdukTable(DefaultTableModel model) throws Exception {
        List<Produk> daftarProduk = produkDAO.findAll();
        model.setRowCount(0);
        for (Produk p : daftarProduk) {
            model.addRow(new Object[]{p.getId(), p.getNama(), p.getJenis(), p.getHarga(), p.getStok()});
        }
    }

    private void reloadSupplierTable(DefaultTableModel model) throws Exception {
        List<Supplier> daftarSupplier = supplierDAO.findAll();
        model.setRowCount(0);
        for (Supplier s : daftarSupplier) {
            model.addRow(new Object[]{s.getId(), s.getNama(), s.getAlamat(), s.getKontak()});
        }
    }

    private void reloadTransaksiTable(DefaultTableModel model) throws Exception {
        List<Transaksi> daftarTransaksi = transaksiDAO.findAll();
        model.setRowCount(0);
        for (Transaksi t : daftarTransaksi) {
            model.addRow(new Object[]{t.getId(), t.getProdukId(), t.getJumlah(), t.getTotal(), t.getPembeli()});
        }
    }

    // Styling
    private void styleCrudButton(JButton btn) {
        btn.setBackground(new Color(200, 230, 201));
        btn.setForeground(new Color(27, 94, 32));
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBorder(BorderFactory.createEmptyBorder(6, 16, 6, 16));
    }

    private void refreshContent() {
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}