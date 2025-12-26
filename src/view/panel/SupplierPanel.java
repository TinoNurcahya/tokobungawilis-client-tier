package view.panel;

import dao.SupplierDAO;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import model.Supplier;
import view.websocket.RealtimeClient;

public class SupplierPanel extends JPanel {

    private SupplierDAO supplierDAO;
    private DefaultTableModel model;
    private JTable table;
    private RealtimeClient realtimeClient;

    private final String[] kolom = { "ID", "Nama", "Alamat", "Kontak" };

    public SupplierPanel(SupplierDAO supplierDAO, RealtimeClient realtimeClient) {
        this.supplierDAO = supplierDAO;
        this.realtimeClient = realtimeClient;

        setLayout(new BorderLayout(10, 10)); // spacing biar lega
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
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS); // RESPONSIVE
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        aturLebarKolom();
    }

    private JScrollPane createScrollPane() {
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 230, 201)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        return scrollPane;
    }

    private void aturLebarKolom() {
        TableColumnModel colModel = table.getColumnModel();
        colModel.getColumn(0).setPreferredWidth(60);   // ID
        colModel.getColumn(1).setPreferredWidth(200);  // Nama
        colModel.getColumn(2).setPreferredWidth(350);  // Alamat
        colModel.getColumn(3).setPreferredWidth(150);  // Kontak
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

        btnTambah.addActionListener(e -> tambahSupplier());
        btnEdit.addActionListener(e -> editSupplier());
        btnHapus.addActionListener(e -> hapusSupplier());

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
            List<Supplier> daftarSupplier = supplierDAO.findAll();
            model.setRowCount(0);

            for (Supplier s : daftarSupplier) {
                model.addRow(new Object[] {
                        s.getId(),
                        s.getNama(),
                        s.getAlamat(),
                        s.getKontak()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Gagal ambil data supplier:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void refreshData() {
        loadData();
    }

    /* ======================= CRUD ACTION ======================= */

    private void tambahSupplier() {
        JTextField namaField = new JTextField();
        JTextField alamatField = new JTextField();
        JTextField kontakField = new JTextField();

        Object[] message = {
                "Nama:", namaField,
                "Alamat:", alamatField,
                "Kontak:", kontakField
        };

        int option = JOptionPane.showConfirmDialog(
                this, message, "Tambah Supplier", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            try {
                Supplier s = new Supplier(
                        0,
                        namaField.getText().trim(),
                        alamatField.getText().trim(),
                        kontakField.getText().trim()
                );

                supplierDAO.insert(s);
                loadData();

                if (realtimeClient != null && realtimeClient.isConnected()) {
                    realtimeClient.sendMessage("SUPPLIER:INSERT:" + s.getNama());
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Gagal tambah supplier:\n" + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editSupplier() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Pilih data terlebih dahulu.");
            return;
        }

        int id = (int) model.getValueAt(row, 0);

        JTextField namaField = new JTextField(model.getValueAt(row, 1).toString());
        JTextField alamatField = new JTextField(model.getValueAt(row, 2).toString());
        JTextField kontakField = new JTextField(model.getValueAt(row, 3).toString());

        Object[] message = {
                "Nama:", namaField,
                "Alamat:", alamatField,
                "Kontak:", kontakField
        };

        int option = JOptionPane.showConfirmDialog(
                this, message, "Edit Supplier", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            try {
                Supplier s = new Supplier(
                        id,
                        namaField.getText().trim(),
                        alamatField.getText().trim(),
                        kontakField.getText().trim()
                );

                supplierDAO.update(s);
                loadData();

                if (realtimeClient != null && realtimeClient.isConnected()) {
                    realtimeClient.sendMessage("SUPPLIER:UPDATE:" + id);
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Gagal edit supplier:\n" + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void hapusSupplier() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Pilih data terlebih dahulu.");
            return;
        }

        int id = (int) model.getValueAt(row, 0);

        int confirm = JOptionPane.showConfirmDialog(
                this, "Hapus supplier ID " + id + "?",
                "Konfirmasi", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                supplierDAO.delete(id);
                loadData();

                if (realtimeClient != null && realtimeClient.isConnected()) {
                    realtimeClient.sendMessage("SUPPLIER:DELETE:" + id);
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Gagal hapus supplier:\n" + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
