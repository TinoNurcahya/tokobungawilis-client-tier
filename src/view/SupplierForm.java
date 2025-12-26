package view;

import javax.swing.*;
import java.awt.*;
import dao.SupplierDAO;
import model.Supplier;

public class SupplierForm extends JFrame {
    private JTextField txtNama = new JTextField(20);
    private JTextField txtAlamat = new JTextField(20);
    private JTextField txtKontak = new JTextField(20);
    private JButton btnSimpan = new JButton("Simpan");
    private SupplierDAO supplierDAO = new SupplierDAO();
    private Supplier supplierEdit = null;

    public SupplierForm() {
        this(null); // mode tambah
    }

    public SupplierForm(Supplier supplier) {
        this.supplierEdit = supplier;
        setTitle(supplier == null ? "Tambah Supplier" : "Edit Supplier");
        setSize(400, 250);
        setLayout(new GridLayout(4, 2, 10, 10));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        add(new JLabel("Nama Supplier:"));
        add(txtNama);
        add(new JLabel("Alamat:"));
        add(txtAlamat);
        add(new JLabel("Kontak:"));
        add(txtKontak);
        add(new JLabel(""));
        add(btnSimpan);

        if (supplier != null) {
            txtNama.setText(supplier.getNama());
            txtAlamat.setText(supplier.getAlamat());
            txtKontak.setText(supplier.getKontak());
        }

        btnSimpan.addActionListener(e -> simpanData());

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void simpanData() {
        String nama = txtNama.getText().trim();
        String alamat = txtAlamat.getText().trim();
        String kontak = txtKontak.getText().trim();

        if (nama.isEmpty() || alamat.isEmpty() || kontak.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field wajib diisi!");
            return;
        }

        try {
            if (supplierEdit == null) {
                Supplier baru = new Supplier(nama, alamat, kontak);
                supplierDAO.insert(baru);
                JOptionPane.showMessageDialog(this, "Supplier berhasil ditambahkan!");
            } else {
                supplierEdit.setNama(nama);
                supplierEdit.setAlamat(alamat);
                supplierEdit.setKontak(kontak);
                supplierDAO.update(supplierEdit);
                JOptionPane.showMessageDialog(this, "Supplier berhasil diperbarui!");
            }
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + ex.getMessage());
        }
    }
}
