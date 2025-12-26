package dao;

import java.sql.*;
import model.User;
import util.DBConnection;

public class UserDAO {
    public User login(String username, String password) throws SQLException {
        String sql = "SELECT * FROM user WHERE username=? AND password=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("role")
                );
            }
        }
        return null; // kalau tidak ada user
    }
}
