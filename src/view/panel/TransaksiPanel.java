package view.panel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import dao.TransaksiDAO;
import model.Transaksi;
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
      JOptionPane.showMessageDialog(this, "Gagal ambil data transaksi: " + ex.getMessage());
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

    btnTambah.addActionListener(e -> tambahTransaksi());
    btnEdit.addActionListener(e -> editTransaksi());
    btnHapus.addActionListener(e -> hapusTransaksi());

    // HAPUS tombol Refresh
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
            pembeliField.getText().trim());

        transaksiDAO.insert(t);
        loadData();

        if (realtimeClient != null && realtimeClient.isConnected()) {
          realtimeClient.sendMessage("TRANSAKSI:INSERT:" + t.getId() + ":Total=" + t.getTotal());
        }

      } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Gagal tambah transaksi: " + ex.getMessage());
      }
    }
  }

  private void editTransaksi() {
    int row = table.getSelectedRow();
    if (row >= 0) {
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
          try {
            Transaksi t = new Transaksi(
                id,
                Integer.parseInt(produkIdField.getText()),
                Integer.parseInt(jumlahField.getText()),
                Double.parseDouble(totalField.getText()),
                pembeliField.getText().trim());

            transaksiDAO.update(t);
            loadData();

            if (realtimeClient != null && realtimeClient.isConnected()) {
              realtimeClient.sendMessage("TRANSAKSI:UPDATE:" + t.getId());
            }

          } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal edit transaksi: " + ex.getMessage());
          }
        }
      } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Gagal mengambil data transaksi: " + ex.getMessage());
      }
    } else {
      JOptionPane.showMessageDialog(this, "Pilih baris dulu untuk edit.");
    }
  }

  private void hapusTransaksi() {
    int row = table.getSelectedRow();
    if (row >= 0) {
      int id = (int) model.getValueAt(row, 0);

      int confirm = JOptionPane.showConfirmDialog(
          this, "Hapus transaksi ID " + id + "?", "Konfirmasi", JOptionPane.YES_NO_OPTION);

      if (confirm == JOptionPane.YES_OPTION) {
        try {
          transaksiDAO.delete(id);
          loadData();

          if (realtimeClient != null && realtimeClient.isConnected()) {
            realtimeClient.sendMessage("TRANSAKSI:DELETE:" + id);
          }

        } catch (Exception ex) {
          JOptionPane.showMessageDialog(this, "Gagal hapus transaksi: " + ex.getMessage());
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