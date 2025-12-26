package util;

import java.sql.*;

public class DBConnection {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/tokobunga";
        String user = "root";   // sesuaikan dengan konfigurasi MySQL kamu
        String pass = "";       // isi password MySQL kalau ada
        return DriverManager.getConnection(url, user, pass);
    }
}
