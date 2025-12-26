package dao;

import model.Transaksi;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransaksiDAO {

    // INSERT (ID AUTO_INCREMENT)
    public void insert(Transaksi t) throws Exception {
        String sql = "INSERT INTO transaksi (produk_id, jumlah, total, pembeli) VALUES (?, ?, ?, ?)";
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setInt(1, t.getProdukId());
        ps.setInt(2, t.getJumlah());
        ps.setDouble(3, t.getTotal());
        ps.setString(4, t.getPembeli());

        ps.executeUpdate();
        ps.close();
        conn.close();
    }

    // UPDATE
    public void update(Transaksi t) throws Exception {
        String sql = "UPDATE transaksi SET produk_id=?, jumlah=?, total=?, pembeli=? WHERE id=?";
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setInt(1, t.getProdukId());
        ps.setInt(2, t.getJumlah());
        ps.setDouble(3, t.getTotal());
        ps.setString(4, t.getPembeli());
        ps.setInt(5, t.getId());

        ps.executeUpdate();
        ps.close();
        conn.close();
    }

    // DELETE
    public void delete(int id) throws Exception {
        String sql = "DELETE FROM transaksi WHERE id=?";
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);

        ps.executeUpdate();
        ps.close();
        conn.close();
    }

    // FIND BY ID
    public Transaksi findById(int id) throws Exception {
        String sql = "SELECT * FROM transaksi WHERE id=?";
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();
        Transaksi t = null;

        if (rs.next()) {
            t = new Transaksi(
                rs.getInt("id"),
                rs.getInt("produk_id"),
                rs.getInt("jumlah"),
                rs.getDouble("total"),
                rs.getString("pembeli")
            );
        }

        rs.close();
        ps.close();
        conn.close();
        return t;
    }

    // FIND ALL (UNTUK JTable)
    public List<Transaksi> findAll() throws Exception {
        List<Transaksi> list = new ArrayList<>();
        String sql = "SELECT * FROM transaksi ORDER BY id ASC";
        Connection conn = DBConnection.getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);

        while (rs.next()) {
            list.add(new Transaksi(
                rs.getInt("id"),
                rs.getInt("produk_id"),
                rs.getInt("jumlah"),
                rs.getDouble("total"),
                rs.getString("pembeli")
            ));
        }

        rs.close();
        st.close();
        conn.close();
        return list;
    }

    // AMBIL HARGA PRODUK
    public double getHargaProduk(int produkId) throws Exception {
        String sql = "SELECT harga FROM produk WHERE id=?";
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, produkId);

        ResultSet rs = ps.executeQuery();
        double harga = 0;

        if (rs.next()) {
            harga = rs.getDouble("harga");
        }

        rs.close();
        ps.close();
        conn.close();
        return harga;
    }
}
