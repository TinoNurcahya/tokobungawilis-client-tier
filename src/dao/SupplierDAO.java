package dao;

import java.sql.*;
import java.util.*;
import model.Supplier;
import util.DBConnection;

public class SupplierDAO {

    public void insert(Supplier s) throws Exception {
        String sql = "INSERT INTO supplier (nama, alamat, kontak) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, s.getNama());
            stmt.setString(2, s.getAlamat());
            stmt.setString(3, s.getKontak());
            stmt.executeUpdate();
        }
    }

    public void update(Supplier s) throws Exception {
        String sql = "UPDATE supplier SET nama=?, alamat=?, kontak=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, s.getNama());
            stmt.setString(2, s.getAlamat());
            stmt.setString(3, s.getKontak());
            stmt.setInt(4, s.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws Exception {
        String sql = "DELETE FROM supplier WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Supplier> findAll() throws Exception {
        String sql = "SELECT * FROM supplier";
        List<Supplier> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Supplier s = new Supplier(
                    rs.getInt("id"),
                    rs.getString("nama"),
                    rs.getString("alamat"),
                    rs.getString("kontak")
                );
                list.add(s);
            }
        }
        return list;
    }

    public Supplier findById(int id) throws Exception {
        String sql = "SELECT * FROM supplier WHERE id=?";
        Supplier s = null;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    s = new Supplier(
                        rs.getInt("id"),
                        rs.getString("nama"),
                        rs.getString("alamat"),
                        rs.getString("kontak")
                    );
                }
            }
        }
        return s;
    }
}
