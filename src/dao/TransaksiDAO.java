package dao;

import java.sql.*;
import java.util.*;
import model.Transaksi;
import util.DBConnection;

public class TransaksiDAO {

    public void insert(Transaksi t) throws Exception {
        String sql = "INSERT INTO transaksi (produk_id, jumlah, total, pembeli) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, t.getProdukId());
            stmt.setInt(2, t.getJumlah());
            stmt.setDouble(3, t.getTotal());
            stmt.setString(4, t.getPembeli());
            stmt.executeUpdate();
        }
    }

    public void update(Transaksi t) throws Exception {
        String sql = "UPDATE transaksi SET produk_id=?, jumlah=?, total=?, pembeli=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, t.getProdukId());
            stmt.setInt(2, t.getJumlah());
            stmt.setDouble(3, t.getTotal());
            stmt.setString(4, t.getPembeli());
            stmt.setInt(5, t.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws Exception {
        String sql = "DELETE FROM transaksi WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Transaksi> findAll() throws Exception {
        String sql = "SELECT * FROM transaksi";
        List<Transaksi> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Transaksi t = new Transaksi(
                    rs.getInt("id"),
                    rs.getInt("produk_id"),
                    rs.getInt("jumlah"),
                    rs.getDouble("total"),
                    rs.getString("pembeli")
                );
                list.add(t);
            }
        }
        return list;
    }

    public Transaksi findById(int id) throws Exception {
        String sql = "SELECT * FROM transaksi WHERE id=?";
        Transaksi t = null;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    t = new Transaksi(
                        rs.getInt("id"),
                        rs.getInt("produk_id"),
                        rs.getInt("jumlah"),
                        rs.getDouble("total"),
                        rs.getString("pembeli")
                    );
                }
            }
        }
        return t;
    }
}
