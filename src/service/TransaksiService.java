package service;

import dao.TransaksiDAO;
import model.Transaksi;
import java.util.List;

public class TransaksiService {

    private final TransaksiDAO transaksiDAO = new TransaksiDAO();

    // CREATE
    public void tambahTransaksi(int produkId, int jumlah, double harga, String pembeli) throws Exception {

        if (produkId <= 0) {
            throw new Exception("Produk belum dipilih");
        }
        if (jumlah <= 0) {
            throw new Exception("Jumlah harus lebih dari 0");
        }
        if (pembeli == null || pembeli.isEmpty()) {
            throw new Exception("Nama pembeli wajib diisi");
        }

        double total = harga * jumlah;

        Transaksi transaksi = new Transaksi(
            produkId,
            jumlah,
            total,
            pembeli
        );

        transaksiDAO.insert(transaksi);
    }

    // READ
    public List<Transaksi> getAllTransaksi() throws Exception {
        return transaksiDAO.findAll();
    }

    // UPDATE
    public void updateTransaksi(int id, int produkId, int jumlah, double harga, String pembeli) throws Exception {

        if (id <= 0) {
            throw new Exception("ID transaksi tidak valid");
        }

        double total = harga * jumlah;

        Transaksi transaksi = new Transaksi(
            id,
            produkId,
            jumlah,
            total,
            pembeli
        );

        transaksiDAO.update(transaksi);
    }

    // DELETE
    public void hapusTransaksi(int id) throws Exception {
        transaksiDAO.delete(id);
    }

    // FIND BY ID
    public Transaksi getTransaksiById(int id) throws Exception {
        return transaksiDAO.findById(id);
    }
}
