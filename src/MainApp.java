import javax.swing.*;
import view.LoginForm;

public class MainApp {
    public static void main(String[] args) {
        // Set Look & Feel ke sistem agar tampilan lebih modern
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            System.err.println("Gagal set Look & Feel: " + ex.getMessage());
        }

        // Jalankan GUI di Event Dispatch Thread (best practice untuk Swing)
        SwingUtilities.invokeLater(() -> {
            LoginForm login = new LoginForm();
            login.setLocationRelativeTo(null); // tampil di tengah layar
            login.setVisible(true);
        });
    }
}
