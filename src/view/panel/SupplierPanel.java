package view.panel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import dao.SupplierDAO;
import model.Supplier;
import view.websocket.RealtimeClient;

public class SupplierPanel extends JPanel {
  private SupplierDAO supplierDAO;
  private DefaultTableModel model;
  private JTable table;
  private RealtimeClient realtimeClient;
  private String[] kolom = { "ID", "Nama", "Alamat", "Kontak" };

  public SupplierPanel(SupplierDAO supplierDAO, RealtimeClient realtimeClient) {
    this.supplierDAO = supplierDAO;
    this.realtimeClient = realtimeClient;
    setLayout(new BorderLayout());
    setBackground(new Color(232, 245, 233));
    initializeUI();
    loadData();
  }

  private void initializeUI() {
    model = new DefaultTableModel(kolom, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };

    table = new JTable(model);

    JScrollPane scrollPane = new JScrollPane(table);
    add(scrollPane, BorderLayout.CENTER);

    JPanel panelAksi = createCrudPanel();
    add(panelAksi, BorderLayout.SOUTH);
  }

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
      JOptionPane.showMessageDialog(this, "Gagal ambil data supplier: " + ex.getMessage());
    }
  }

  private JPanel createCrudPanel() {
    JPanel panelAksi = new JPanel(new FlowLayout(FlowLayout.RIGHT));

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
    btn.setBorder(BorderFactory.createEmptyBorder(6, 16, 6, 16));
  }

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
            kontakField.getText().trim());

        supplierDAO.insert(s);
        loadData();

        if (realtimeClient != null && realtimeClient.isConnected()) {
          realtimeClient.sendMessage("SUPPLIER:INSERT:" + s.getId() + ":" + s.getNama());
        }

      } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Gagal tambah supplier: " + ex.getMessage());
      }
    }
  }

  private void editSupplier() {
    int row = table.getSelectedRow();
    if (row >= 0) {
      int id = (int) model.getValueAt(row, 0);

      JTextField namaField = new JTextField(String.valueOf(model.getValueAt(row, 1)));
      JTextField alamatField = new JTextField(String.valueOf(model.getValueAt(row, 2)));
      JTextField kontakField = new JTextField(String.valueOf(model.getValueAt(row, 3)));

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
              kontakField.getText().trim());

          supplierDAO.update(s);
          loadData();

          if (realtimeClient != null && realtimeClient.isConnected()) {
            realtimeClient.sendMessage("SUPPLIER:UPDATE:" + s.getId());
          }

        } catch (Exception ex) {
          JOptionPane.showMessageDialog(this, "Gagal edit supplier: " + ex.getMessage());
        }
      }
    } else {
      JOptionPane.showMessageDialog(this, "Pilih baris dulu untuk edit.");
    }
  }

  private void hapusSupplier() {
    int row = table.getSelectedRow();
    if (row >= 0) {
      int id = (int) model.getValueAt(row, 0);

      // MESSAGE
      int confirm = JOptionPane.showConfirmDialog(
          this, "Hapus supplier ID " + id + "?", "Konfirmasi", JOptionPane.YES_NO_OPTION);

      if (confirm == JOptionPane.YES_OPTION) {
        try {
          supplierDAO.delete(id);
          loadData();

          if (realtimeClient != null && realtimeClient.isConnected()) {
            realtimeClient.sendMessage("SUPPLIER:DELETE:" + id);
          }

        } catch (Exception ex) {
          JOptionPane.showMessageDialog(this, "Gagal hapus supplier: " + ex.getMessage());
        }
      }
    } else {
      JOptionPane.showMessageDialog(this, "Pilih baris dulu untuk hapus.");
    }
  }

  public void refreshData() {
    loadData();
  }
}