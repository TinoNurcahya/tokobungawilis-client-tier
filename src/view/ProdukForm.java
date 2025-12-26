package view;

import javax.swing.*;
import java.awt.*;
import dao.ProdukDAO;
import model.Produk;

public class ProdukForm extends JFrame {
    private JTextField txtNama = new JTextField(20);
    private JTextField txtJenis = new JTextField(20);
    private JTextField txtHarga = new JTextField(20);
    private JTextField txtStok = new JTextField(20);
    private JButton btnSimpan = new JButton("Simpan");
    private ProdukDAO produkDAO = new ProdukDAO();
    private Produk produkEdit = null;

    public ProdukForm() {
        this(null); // mode tambah
    }

    public ProdukForm(Produk produk) {
        this.produkEdit = produk;
        setTitle(produk == null ? "Tambah Produk" : "Edit Produk");
        setSize(400, 300);
        setLayout(new GridLayout(5, 2, 10, 10));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Form field
        add(new JLabel("Nama Produk:"));
        add(txtNama);
        add(new JLabel("Jenis:"));
        add(txtJenis);
        add(new JLabel("Harga:"));
        add(txtHarga);
        add(new JLabel("Stok:"));
        add(txtStok);
        add(new JLabel(""));
        add(btnSimpan);

        // Jika mode edit, isi field dengan data produk
        if (produk != null) {
            txtNama.setText(produk.getNama());
            txtJenis.setText(produk.getJenis());
            txtHarga.setText(String.valueOf(produk.getHarga()));
            txtStok.setText(String.valueOf(produk.getStok()));
        }

        // Aksi tombol simpan
        btnSimpan.addActionListener(e -> simpanData());

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void simpanData() {
        String nama = txtNama.getText().trim();
        String jenis = txtJenis.getText().trim();
        double harga;
        int stok;

        try {
            harga = Double.parseDouble(txtHarga.getText().trim());
            stok = Integer.parseInt(txtStok.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Harga dan Stok harus berupa angka!");
            return;
        }

        if (nama.isEmpty() || jenis.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field wajib diisi!");
            return;
        }

        try {
            if (produkEdit == null) {
                // Mode tambah
                Produk baru = new Produk(nama, jenis, harga, stok);
                produkDAO.insert(baru);
                JOptionPane.showMessageDialog(this, "Produk berhasil ditambahkan!");
            } else {
                // Mode edit
                produkEdit.setNama(nama);
                produkEdit.setJenis(jenis);
                produkEdit.setHarga(harga);
                produkEdit.setStok(stok);
                produkDAO.update(produkEdit);
                JOptionPane.showMessageDialog(this, "Produk berhasil diperbarui!");
            }
            dispose(); // Tutup form setelah simpan
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + ex.getMessage());
        }
    }
}
