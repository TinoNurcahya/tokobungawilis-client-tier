package view.panel;

import dao.ProdukDAO;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import model.Produk;
import view.websocket.RealtimeClient;

public class ProdukPanel extends JPanel {

    private ProdukDAO produkDAO;
    private DefaultTableModel model;
    private JTable table;
    private RealtimeClient realtimeClient;

    private final String[] kolom = { "ID", "Nama", "Jenis", "Harga", "Stok" };

    public ProdukPanel(ProdukDAO produkDAO, RealtimeClient realtimeClient) {
        this.produkDAO = produkDAO;
        this.realtimeClient = realtimeClient;

        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(232, 245, 233));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        initTable();
        add(createScrollPane(), BorderLayout.CENTER);
        add(createCrudPanel(), BorderLayout.SOUTH);

        loadData();
    }

    /* ======================= TABLE ======================= */

    private void initTable() {
        model = new DefaultTableModel(kolom, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        aturLebarKolom();
    }

    private JScrollPane createScrollPane() {
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 230, 201)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        return scrollPane;
    }

    private void aturLebarKolom() {
        TableColumnModel col = table.getColumnModel();
        col.getColumn(0).setPreferredWidth(60);   // ID
        col.getColumn(1).setPreferredWidth(250);  // Nama
        col.getColumn(2).setPreferredWidth(180);  // Jenis
        col.getColumn(3).setPreferredWidth(120);  // Harga
        col.getColumn(4).setPreferredWidth(80);   // Stok
    }

    /* ======================= CRUD PANEL ======================= */

    private JPanel createCrudPanel() {
        JPanel panelAksi = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        panelAksi.setBackground(new Color(232, 245, 233));

        JButton btnTambah = new JButton("Tambah");
        JButton btnEdit = new JButton("Edit");
        JButton btnHapus = new JButton("Hapus");

        styleCrudButton(btnTambah);
        styleCrudButton(btnEdit);
        styleCrudButton(btnHapus);

        btnTambah.addActionListener(e -> tambahProduk());
        btnEdit.addActionListener(e -> editProduk());
        btnHapus.addActionListener(e -> hapusProduk());

        panelAksi.add(btnTambah);
        panelAksi.add(btnEdit);
        panelAksi.add(btnHapus);

        return panelAksi;
    }

    private void styleCrudButton(JButton btn) {
        btn.setBackground(new Color(200, 230, 201));
        btn.setForeground(new Color(27, 94, 32));
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
    }

    /* ======================= DATA ======================= */

    private void loadData() {
        try {
            List<Produk> daftarProduk = produkDAO.findAll();
            model.setRowCount(0);

            for (Produk p : daftarProduk) {
                model.addRow(new Object[] {
                        p.getId(),
                        p.getNama(),
                        p.getJenis(),
                        p.getHarga(),
                        p.getStok()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Gagal ambil data produk:\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public void refreshData() {
        loadData();
    }

    /* ======================= CRUD ACTION ======================= */

    private void tambahProduk() {
        JTextField namaField = new JTextField();
        JTextField jenisField = new JTextField();
        JTextField hargaField = new JTextField();
        JTextField stokField = new JTextField();

        Object[] message = {
                "Nama:", namaField,
                "Jenis:", jenisField,
                "Harga:", hargaField,
                "Stok:", stokField
        };

        int option = JOptionPane.showConfirmDialog(
                this, message, "Tambah Produk", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            try {
                Produk p = new Produk(
                        0,
                        namaField.getText().trim(),
                        jenisField.getText().trim(),
                        Double.parseDouble(hargaField.getText()),
                        Integer.parseInt(stokField.getText())
                );

                produkDAO.insert(p);
                loadData();

                if (realtimeClient != null && realtimeClient.isConnected()) {
                    realtimeClient.sendMessage("PRODUK:INSERT:" + p.getNama());
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Gagal tambah produk:\n" + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private void editProduk() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Pilih data terlebih dahulu.");
            return;
        }

        int id = (int) model.getValueAt(row, 0);

        JTextField namaField = new JTextField(model.getValueAt(row, 1).toString());
        JTextField jenisField = new JTextField(model.getValueAt(row, 2).toString());
        JTextField hargaField = new JTextField(model.getValueAt(row, 3).toString());
        JTextField stokField = new JTextField(model.getValueAt(row, 4).toString());

        Object[] message = {
                "Nama:", namaField,
                "Jenis:", jenisField,
                "Harga:", hargaField,
                "Stok:", stokField
        };

        int option = JOptionPane.showConfirmDialog(
                this, message, "Edit Produk", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            try {
                Produk p = new Produk(
                        id,
                        namaField.getText().trim(),
                        jenisField.getText().trim(),
                        Double.parseDouble(hargaField.getText()),
                        Integer.parseInt(stokField.getText())
                );

                produkDAO.update(p);
                loadData();

                if (realtimeClient != null && realtimeClient.isConnected()) {
                    realtimeClient.sendMessage("PRODUK:UPDATE:" + id);
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Gagal edit produk:\n" + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private void hapusProduk() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Pilih data terlebih dahulu.");
            return;
        }

        int id = (int) model.getValueAt(row, 0);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Hapus produk ID " + id + "?",
                "Konfirmasi",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                produkDAO.delete(id);
                loadData();

                if (realtimeClient != null && realtimeClient.isConnected()) {
                    realtimeClient.sendMessage("PRODUK:DELETE:" + id);
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Gagal hapus produk:\n" + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
}
