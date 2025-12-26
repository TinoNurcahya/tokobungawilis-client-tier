package view;

import dao.TransaksiDAO;
import model.Transaksi;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class TransaksiForm extends JFrame {

    private JTextField txtProdukId = new JTextField(20);
    private JTextField txtJumlah = new JTextField(20);
    private JTextField txtTotal = new JTextField(20);
    private JTextField txtPembeli = new JTextField(20);
    private JButton btnSimpan = new JButton("Simpan");

    private TransaksiDAO transaksiDAO;
    private Transaksi transaksiEdit;
    private Runnable onSuccess;

    private double hargaProduk = 0;

    // ================= CONSTRUCTOR FINAL =================
    public TransaksiForm(Transaksi transaksi, TransaksiDAO dao, Runnable onSuccess) {
        this.transaksiEdit = transaksi;
        this.transaksiDAO = dao;
        this.onSuccess = onSuccess;

        setTitle(transaksi == null ? "Tambah Transaksi" : "Edit Transaksi");
        setSize(420, 320);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        txtTotal.setEditable(false);

        // ===== UI =====
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addRow(panel, gbc, 0, "Produk ID", txtProdukId);
        addRow(panel, gbc, 1, "Jumlah", txtJumlah);
        addRow(panel, gbc, 2, "Total", txtTotal);
        addRow(panel, gbc, 3, "Pembeli", txtPembeli);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(btnSimpan, gbc);

        add(panel);

        // ===== ISI DATA SAAT EDIT =====
        if (transaksiEdit != null) {
            txtProdukId.setText(String.valueOf(transaksiEdit.getProdukId()));
            txtJumlah.setText(String.valueOf(transaksiEdit.getJumlah()));
            txtTotal.setText(String.valueOf(transaksiEdit.getTotal()));
            txtPembeli.setText(transaksiEdit.getPembeli());
            hargaProduk = transaksiEdit.getTotal() / transaksiEdit.getJumlah();
        }

        // ===== LISTENER OTOMATIS (KUNCI) =====
        txtProdukId.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { ambilHargaDanHitung(); }
            public void removeUpdate(DocumentEvent e) { ambilHargaDanHitung(); }
            public void changedUpdate(DocumentEvent e) { ambilHargaDanHitung(); }
        });

        txtJumlah.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { hitungTotal(); }
            public void removeUpdate(DocumentEvent e) { hitungTotal(); }
            public void changedUpdate(DocumentEvent e) { hitungTotal(); }
        });

        btnSimpan.addActionListener(e -> simpan());

        setVisible(true);
    }

    // ================= HELPER UI =================
    private void addRow(JPanel panel, GridBagConstraints gbc, int y, String label, JTextField field) {
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    // ================= LOGIC OTOMATIS =================
    private void ambilHargaDanHitung() {
        try {
            int produkId = Integer.parseInt(txtProdukId.getText().trim());
            hargaProduk = transaksiDAO.getHargaProduk(produkId);

            if (hargaProduk <= 0) {
                txtTotal.setText("");
                return;
            }
            hitungTotal();

        } catch (Exception e) {
            hargaProduk = 0;
            txtTotal.setText("");
        }
    }

    private void hitungTotal() {
        try {
            int jumlah = Integer.parseInt(txtJumlah.getText().trim());
            if (hargaProduk > 0) {
                txtTotal.setText(String.valueOf(jumlah * hargaProduk));
            } else {
                txtTotal.setText("");
            }
        } catch (Exception e) {
            txtTotal.setText("");
        }
    }

    // ================= SIMPAN =================
    private void simpan() {
        try {
            int produkId = Integer.parseInt(txtProdukId.getText().trim());
            int jumlah = Integer.parseInt(txtJumlah.getText().trim());
            double total = Double.parseDouble(txtTotal.getText().trim());
            String pembeli = txtPembeli.getText().trim();

            if (pembeli.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Pembeli wajib diisi!");
                return;
            }

            if (transaksiEdit == null) {
                transaksiDAO.insert(new Transaksi(produkId, jumlah, total, pembeli));
            } else {
                transaksiEdit.setProdukId(produkId);
                transaksiEdit.setJumlah(jumlah);
                transaksiEdit.setTotal(total);
                transaksiEdit.setPembeli(pembeli);
                transaksiDAO.update(transaksiEdit);
            }

            if (onSuccess != null) onSuccess.run();
            dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}
