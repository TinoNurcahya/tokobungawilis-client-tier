package view;

import javax.swing.*;
import java.awt.*;
import dao.TransaksiDAO;
import model.Transaksi;

public class TransaksiForm extends JFrame {
    private JTextField txtProdukId = new JTextField(20);
    private JTextField txtJumlah = new JTextField(20);
    private JTextField txtTotal = new JTextField(20);
    private JTextField txtPembeli = new JTextField(20);
    private JButton btnSimpan = new JButton("Simpan");
    private TransaksiDAO transaksiDAO = new TransaksiDAO();
    private Transaksi transaksiEdit = null;

    public TransaksiForm() {
        this(null); // mode tambah
    }

    public TransaksiForm(Transaksi transaksi) {
        this.transaksiEdit = transaksi;
        setTitle(transaksi == null ? "Tambah Transaksi" : "Edit Transaksi");
        setSize(400, 300);
        setLayout(new GridLayout(5, 2, 10, 10));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        add(new JLabel("Produk ID:"));
        add(txtProdukId);
        add(new JLabel("Jumlah:"));
        add(txtJumlah);
        add(new JLabel("Total:"));
        add(txtTotal);
        add(new JLabel("Pembeli:"));
        add(txtPembeli);
        add(new JLabel(""));
        add(btnSimpan);

        if (transaksi != null) {
            txtProdukId.setText(String.valueOf(transaksi.getProdukId()));
            txtJumlah.setText(String.valueOf(transaksi.getJumlah()));
            txtTotal.setText(String.valueOf(transaksi.getTotal()));
            txtPembeli.setText(transaksi.getPembeli());
        }

        btnSimpan.addActionListener(e -> simpanData());

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void simpanData() {
        int produkId, jumlah;
        double total;
        String pembeli = txtPembeli.getText().trim();

        try {
            produkId = Integer.parseInt(txtProdukId.getText().trim());
            jumlah = Integer.parseInt(txtJumlah.getText().trim());
            total = Double.parseDouble(txtTotal.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Produk ID, Jumlah, dan Total harus angka!");
            return;
        }

        if (pembeli.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pembeli wajib diisi!");
            return;
        }

        try {
            if (transaksiEdit == null) {
                Transaksi baru = new Transaksi(produkId, jumlah, total, pembeli);
                transaksiDAO.insert(baru);
                JOptionPane.showMessageDialog(this, "Transaksi berhasil ditambahkan!");
            } else {
                transaksiEdit.setProdukId(produkId);
                transaksiEdit.setJumlah(jumlah);
                transaksiEdit.setTotal(total);
                transaksiEdit.setPembeli(pembeli);
                transaksiDAO.update(transaksiEdit);
                JOptionPane.showMessageDialog(this, "Transaksi berhasil diperbarui!");
            }
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + ex.getMessage());
        }
    }
}
