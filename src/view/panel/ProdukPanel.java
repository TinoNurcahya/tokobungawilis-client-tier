package view.panel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import dao.ProdukDAO;
import model.Produk;
import view.websocket.RealtimeClient;

public class ProdukPanel extends JPanel {
  private ProdukDAO produkDAO;
  private DefaultTableModel model;
  private JTable table;
  private RealtimeClient realtimeClient;
  private String[] kolom = { "ID", "Nama", "Jenis", "Harga", "Stok" };

  public ProdukPanel(ProdukDAO produkDAO, RealtimeClient realtimeClient) {
    this.produkDAO = produkDAO;
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
      JOptionPane.showMessageDialog(this, "Gagal ambil data produk: " + ex.getMessage());
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
    btn.setBorder(BorderFactory.createEmptyBorder(6, 16, 6, 16));
  }

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
            Integer.parseInt(stokField.getText()));

        produkDAO.insert(p);
        loadData();

        if (realtimeClient != null && realtimeClient.isConnected()) {
          realtimeClient.sendMessage("PRODUK:INSERT:" + p.getId() + ":" + p.getNama());
        }

      } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Gagal tambah produk: " + ex.getMessage());
      }
    }
  }

  private void editProduk() {
    int row = table.getSelectedRow();
    if (row >= 0) {
      int id = (int) model.getValueAt(row, 0);

      JTextField namaField = new JTextField(String.valueOf(model.getValueAt(row, 1)));
      JTextField jenisField = new JTextField(String.valueOf(model.getValueAt(row, 2)));
      JTextField hargaField = new JTextField(String.valueOf(model.getValueAt(row, 3)));
      JTextField stokField = new JTextField(String.valueOf(model.getValueAt(row, 4)));

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
              Integer.parseInt(stokField.getText()));

          produkDAO.update(p);
          loadData();

          if (realtimeClient != null && realtimeClient.isConnected()) {
            realtimeClient.sendMessage("PRODUK:UPDATE:" + p.getId());
          }

        } catch (Exception ex) {
          JOptionPane.showMessageDialog(this, "Gagal edit produk: " + ex.getMessage());
        }
      }
    } else {
      JOptionPane.showMessageDialog(this, "Pilih baris dulu untuk edit.");
    }
  }

  private void hapusProduk() {
    int row = table.getSelectedRow();
    if (row >= 0) {
      int id = (int) model.getValueAt(row, 0);
      int confirm = JOptionPane.showConfirmDialog(
          this, "Hapus produk ID " + id + "?", "Konfirmasi", JOptionPane.YES_NO_OPTION);

      if (confirm == JOptionPane.YES_OPTION) {
        try {
          produkDAO.delete(id);
          loadData();

          if (realtimeClient != null && realtimeClient.isConnected()) {
            realtimeClient.sendMessage("PRODUK:DELETE:" + id);
          }

        } catch (Exception ex) {
          JOptionPane.showMessageDialog(this, "Gagal hapus produk: " + ex.getMessage());
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