package model;

/**
 * Entity Transaksi merepresentasikan tabel 'transaksi' di database.
 * Digunakan untuk operasi CRUD.
 */
public class Transaksi {
    private int id;
    private int produkId;
    private int jumlah;
    private double total;
    private String pembeli;

    // Constructor kosong (wajib untuk JavaBean & ORM)
    public Transaksi() {}

    // Constructor lengkap (untuk data dari DB)
    public Transaksi(int id, int produkId, int jumlah, double total, String pembeli) {
        this.id = id;
        this.produkId = produkId;
        this.jumlah = jumlah;
        this.total = total;
        this.pembeli = pembeli;
    }

    // Constructor tanpa ID (untuk insert data baru)
    public Transaksi(int produkId, int jumlah, double total, String pembeli) {
        this.produkId = produkId;
        this.jumlah = jumlah;
        this.total = total;
        this.pembeli = pembeli;
    }

    // Getter
    public int getId() { return id; }
    public int getProdukId() { return produkId; }
    public int getJumlah() { return jumlah; }
    public double getTotal() { return total; }
    public String getPembeli() { return pembeli; }

    // Setter
    public void setId(int id) { this.id = id; }
    public void setProdukId(int produkId) { this.produkId = produkId; }
    public void setJumlah(int jumlah) { this.jumlah = jumlah; }
    public void setTotal(double total) { this.total = total; }
    public void setPembeli(String pembeli) { this.pembeli = pembeli; }

    @Override
    public String toString() {
        return "Transaksi{id=" + id +
               ", produkId=" + produkId +
               ", jumlah=" + jumlah +
               ", total=" + total +
               ", pembeli='" + pembeli + '\'' +
               '}';
    }
}
