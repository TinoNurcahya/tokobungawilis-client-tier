package view.panel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import dao.TransaksiDAO;
import model.Transaksi;
import view.TransaksiForm;
import view.websocket.RealtimeClient;

public class TransaksiPanel extends JPanel {

    private TransaksiDAO transaksiDAO;
    private DefaultTableModel model;
    private JTable table;
    private RealtimeClient realtimeClient;

    private String[] kolom = { "ID", "Produk ID", "Jumlah", "Total", "Pembeli" };

    public TransaksiPanel(TransaksiDAO transaksiDAO, RealtimeClient realtimeClient) {
        this.transaksiDAO = transaksiDAO;
        this.realtimeClient = realtimeClient;

        setLayout(new BorderLayout());
        setBackground(new Color(232, 245, 233));

        initUI();
        loadData();
    }

    private void initUI() {
        model = new DefaultTableModel(kolom, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(24);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(createCrudPanel(), BorderLayout.SOUTH);
    }

    private void loadData() {
        try {
            List<Transaksi> list = transaksiDAO.findAll();
            model.setRowCount(0);

            for (Transaksi t : list) {
                model.addRow(new Object[] {
                        t.getId(),
                        t.getProdukId(),
                        t.getJumlah(),
                        t.getTotal(),
                        t.getPembeli()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Gagal memuat data transaksi: " + e.getMessage());
        }
    }

    private JPanel createCrudPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBackground(new Color(232, 245, 233));

        JButton btnTambah = new JButton("Tambah");
        JButton btnEdit   = new JButton("Edit");
        JButton btnHapus  = new JButton("Hapus");

        styleButton(btnTambah);
        styleButton(btnEdit);
        styleButton(btnHapus);

        btnTambah.addActionListener(e -> tambahTransaksi());
        btnEdit.addActionListener(e -> editTransaksi());
        btnHapus.addActionListener(e -> hapusTransaksi());

        panel.add(btnTambah);
        panel.add(btnEdit);
        panel.add(btnHapus);

        return panel;
    }

    private void styleButton(JButton btn) {
        btn.setBackground(new Color(200, 230, 201));
        btn.setForeground(new Color(27, 94, 32));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(6, 16, 6, 16));
    }

    // ================= CRUD ACTION =================

    private void tambahTransaksi() {
        new TransaksiForm(null, transaksiDAO, () -> {
            loadData();
            kirimRealtime("INSERT");
        });
    }

    private void editTransaksi() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin diedit.");
            return;
        }

        int id = (int) model.getValueAt(row, 0);

        try {
            Transaksi t = transaksiDAO.findById(id);
            if (t == null) {
                JOptionPane.showMessageDialog(this, "Transaksi tidak ditemukan.");
                return;
            }

            new TransaksiForm(t, transaksiDAO, () -> {
                loadData();
                kirimRealtime("UPDATE");
            });

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Gagal membuka data transaksi: " + e.getMessage());
        }
    }

    private void hapusTransaksi() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin dihapus.");
            return;
        }

        int id = (int) model.getValueAt(row, 0);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Yakin hapus transaksi ID " + id + "?",
                "Konfirmasi",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                transaksiDAO.delete(id);
                loadData();
                kirimRealtime("DELETE:" + id);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Gagal hapus transaksi: " + e.getMessage());
            }
        }
    }

    private void kirimRealtime(String aksi) {
        if (realtimeClient != null && realtimeClient.isConnected()) {
            realtimeClient.sendMessage("TRANSAKSI:" + aksi);
        }
    }

    public void refreshData() {
        loadData();
    }
}
