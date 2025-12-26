package model;

public class Transaksi {
    private int id;
    private int produkId;
    private int jumlah;
    private double total;
    private String pembeli;

    public Transaksi() {}

    public Transaksi(int id, int produkId, int jumlah, double total, String pembeli) {
        this.id = id;
        this.produkId = produkId;
        this.jumlah = jumlah;
        this.total = total;
        this.pembeli = pembeli;
    }

    public Transaksi(int produkId, int jumlah, double total, String pembeli) {
        this.produkId = produkId;
        this.jumlah = jumlah;
        this.total = total;
        this.pembeli = pembeli;
    }

    public int getId() { return id; }
    public int getProdukId() { return produkId; }
    public int getJumlah() { return jumlah; }
    public double getTotal() { return total; }
    public String getPembeli() { return pembeli; }

    public void setId(int id) { this.id = id; }
    public void setProdukId(int produkId) { this.produkId = produkId; }
    public void setJumlah(int jumlah) { this.jumlah = jumlah; }
    public void setTotal(double total) { this.total = total; }
    public void setPembeli(String pembeli) { this.pembeli = pembeli; }
}
