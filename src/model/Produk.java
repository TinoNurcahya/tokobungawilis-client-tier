package model;

/**
 * Entity Produk merepresentasikan tabel 'produk' di database.
 * Digunakan untuk operasi CRUD (Create, Read, Update, Delete).
 */
public class Produk {
    private int id;
    private String nama;
    private String jenis;
    private double harga;
    private int stok;

    // Constructor kosong (wajib untuk JavaBean & ORM)
    public Produk() {}

    // Constructor lengkap (untuk data dari DB)
    public Produk(int id, String nama, String jenis, double harga, int stok) {
        this.id = id;
        this.nama = nama;
        this.jenis = jenis;
        this.harga = harga;
        this.stok = stok;
    }

    // Constructor tanpa ID (untuk insert data baru)
    public Produk(String nama, String jenis, double harga, int stok) {
        this.nama = nama;
        this.jenis = jenis;
        this.harga = harga;
        this.stok = stok;
    }

    // Getter
    public int getId() { return id; }
    public String getNama() { return nama; }
    public String getJenis() { return jenis; }
    public double getHarga() { return harga; }
    public int getStok() { return stok; }

    // Setter
    public void setId(int id) { this.id = id; }
    public void setNama(String nama) { this.nama = nama; }
    public void setJenis(String jenis) { this.jenis = jenis; }
    public void setHarga(double harga) { this.harga = harga; }
    public void setStok(int stok) { this.stok = stok; }

    @Override
    public String toString() {
        return "Produk{id=" + id +
               ", nama='" + nama + '\'' +
               ", jenis='" + jenis + '\'' +
               ", harga=" + harga +
               ", stok=" + stok + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Produk)) return false;
        Produk produk = (Produk) o;
        return id == produk.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
