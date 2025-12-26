package view.panel;

import dao.TransaksiDAO;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import model.Transaksi;
import view.websocket.RealtimeClient;

public class TransaksiPanel extends JPanel {

    private TransaksiDAO transaksiDAO;
    private DefaultTableModel model;
    private JTable table;
    private RealtimeClient realtimeClient;

    private final String[] kolom = { "ID", "Produk ID", "Jumlah", "Total", "Pembeli" };

    public TransaksiPanel(TransaksiDAO transaksiDAO, RealtimeClient realtimeClient) {
        this.transaksiDAO = transaksiDAO;
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
        col.getColumn(0).setPreferredWidth(60);    // ID
        col.getColumn(1).setPreferredWidth(120);   // Produk ID
        col.getColumn(2).setPreferredWidth(100);   // Jumlah
        col.getColumn(3).setPreferredWidth(140);   // Total
        col.getColumn(4).setPreferredWidth(260);   // Pembeli
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

        btnTambah.addActionListener(e -> tambahTransaksi());
        btnEdit.addActionListener(e -> editTransaksi());
        btnHapus.addActionListener(e -> hapusTransaksi());

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
            List<Transaksi> daftarTransaksi = transaksiDAO.findAll();
            model.setRowCount(0);

            for (Transaksi t : daftarTransaksi) {
                model.addRow(new Object[] {
                        t.getId(),
                        t.getProdukId(),
                        t.getJumlah(),
                        t.getTotal(),
                        t.getPembeli()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Gagal ambil data transaksi:\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public void refreshData() {
        loadData();
    }

    /* ======================= CRUD ACTION ======================= */

    private void tambahTransaksi() {
        JTextField produkIdField = new JTextField();
        JTextField jumlahField = new JTextField();
        JTextField totalField = new JTextField();
        JTextField pembeliField = new JTextField();

        Object[] message = {
                "Produk ID:", produkIdField,
                "Jumlah:", jumlahField,
                "Total:", totalField,
                "Pembeli:", pembeliField
        };

        int option = JOptionPane.showConfirmDialog(
                this, message, "Tambah Transaksi", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            try {
                Transaksi t = new Transaksi(
                        0,
                        Integer.parseInt(produkIdField.getText()),
                        Integer.parseInt(jumlahField.getText()),
                        Double.parseDouble(totalField.getText()),
                        pembeliField.getText().trim()
                );

                transaksiDAO.insert(t);
                loadData();

                if (realtimeClient != null && realtimeClient.isConnected()) {
                    realtimeClient.sendMessage("TRANSAKSI:INSERT:Total=" + t.getTotal());
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Gagal tambah transaksi:\n" + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private void editTransaksi() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Pilih data terlebih dahulu.");
            return;
        }

        int id = (int) model.getValueAt(row, 0);

        try {
            Transaksi transaksiLama = transaksiDAO.findById(id);
            if (transaksiLama == null) {
                JOptionPane.showMessageDialog(this, "Transaksi tidak ditemukan!");
                return;
            }

            JTextField produkIdField = new JTextField(String.valueOf(transaksiLama.getProdukId()));
            JTextField jumlahField = new JTextField(String.valueOf(transaksiLama.getJumlah()));
            JTextField totalField = new JTextField(String.valueOf(transaksiLama.getTotal()));
            JTextField pembeliField = new JTextField(transaksiLama.getPembeli());

            Object[] message = {
                    "Produk ID:", produkIdField,
                    "Jumlah:", jumlahField,
                    "Total:", totalField,
                    "Pembeli:", pembeliField
            };

            int option = JOptionPane.showConfirmDialog(
                    this, message, "Edit Transaksi", JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                Transaksi t = new Transaksi(
                        id,
                        Integer.parseInt(produkIdField.getText()),
                        Integer.parseInt(jumlahField.getText()),
                        Double.parseDouble(totalField.getText()),
                        pembeliField.getText().trim()
                );

                transaksiDAO.update(t);
                loadData();

                if (realtimeClient != null && realtimeClient.isConnected()) {
                    realtimeClient.sendMessage("TRANSAKSI:UPDATE:" + id);
                }
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Gagal edit transaksi:\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void hapusTransaksi() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Pilih data terlebih dahulu.");
            return;
        }

        int id = (int) model.getValueAt(row, 0);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Hapus transaksi ID " + id + "?",
                "Konfirmasi",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                transaksiDAO.delete(id);
                loadData();

                if (realtimeClient != null && realtimeClient.isConnected()) {
                    realtimeClient.sendMessage("TRANSAKSI:DELETE:" + id);
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Gagal hapus transaksi:\n" + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
}
