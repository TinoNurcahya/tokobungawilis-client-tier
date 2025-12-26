package dao;

import java.sql.*;
import java.util.*;
import model.Produk;
import util.DBConnection;

public class ProdukDAO {

    public void insert(Produk p) throws Exception {
        String sql = "INSERT INTO produk (nama, jenis, harga, stok) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p.getNama());
            stmt.setString(2, p.getJenis());
            stmt.setDouble(3, p.getHarga());
            stmt.setInt(4, p.getStok());
            stmt.executeUpdate();
        }
    }

    public void update(Produk p) throws Exception {
        String sql = "UPDATE produk SET nama=?, jenis=?, harga=?, stok=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p.getNama());
            stmt.setString(2, p.getJenis());
            stmt.setDouble(3, p.getHarga());
            stmt.setInt(4, p.getStok());
            stmt.setInt(5, p.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws Exception {
        String sql = "DELETE FROM produk WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Produk> findAll() throws Exception {
        String sql = "SELECT * FROM produk";
        List<Produk> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Produk p = new Produk(
                    rs.getInt("id"),
                    rs.getString("nama"),
                    rs.getString("jenis"),
                    rs.getDouble("harga"),
                    rs.getInt("stok")
                );
                list.add(p);
            }
        }
        return list;
    }

    public Produk findById(int id) throws Exception {
        String sql = "SELECT * FROM produk WHERE id=?";
        Produk p = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    p = new Produk(
                        rs.getInt("id"),
                        rs.getString("nama"),
                        rs.getString("jenis"),
                        rs.getDouble("harga"),
                        rs.getInt("stok")
                    );
                }
            }
        }
        return p;
    }
}
