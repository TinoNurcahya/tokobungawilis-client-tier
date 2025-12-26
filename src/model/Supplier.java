package model;

/**
 * Entity Supplier merepresentasikan tabel 'supplier' di database.
 * Digunakan untuk operasi CRUD.
 */
public class Supplier {
    private int id;
    private String nama;
    private String alamat;
    private String kontak;

    // Constructor kosong (wajib untuk JavaBean & ORM)
    public Supplier() {}

    // Constructor lengkap (untuk data dari DB)
    public Supplier(int id, String nama, String alamat, String kontak) {
        this.id = id;
        this.nama = nama;
        this.alamat = alamat;
        this.kontak = kontak;
    }

    // Constructor tanpa ID (untuk insert data baru)
    public Supplier(String nama, String alamat, String kontak) {
        this.nama = nama;
        this.alamat = alamat;
        this.kontak = kontak;
    }

    // Getter
    public int getId() { return id; }
    public String getNama() { return nama; }
    public String getAlamat() { return alamat; }
    public String getKontak() { return kontak; }

    // Setter
    public void setId(int id) { this.id = id; }
    public void setNama(String nama) { this.nama = nama; }
    public void setAlamat(String alamat) { this.alamat = alamat; }
    public void setKontak(String kontak) { this.kontak = kontak; }

    @Override
    public String toString() {
        return "Supplier{id=" + id +
               ", nama='" + nama + '\'' +
               ", alamat='" + alamat + '\'' +
               ", kontak='" + kontak + '\'' +
               '}';
    }
}
