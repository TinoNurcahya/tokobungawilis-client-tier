package view;

import javax.swing.*;

import dao.TransaksiDAO;

import java.awt.*;

public class LoginForm extends JFrame {
    private JTextField txtUsername = new JTextField(20);
    private JPasswordField txtPassword = new JPasswordField(20);
    private JButton btnLogin = new JButton("Login");

    public LoginForm() {
        setTitle("Login - Toko Bunga wilis");
        setSize(450, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // === Panel utama dengan background hijau tua ===
        JPanel panel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(27, 94, 32);   // hijau tua
                Color color2 = new Color(46, 125, 50);  // hijau sedang
                GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("Toko Bunga wilis", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(new Color(200, 230, 201)); // hijau muda

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(lblTitle, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        JLabel lblUser = new JLabel("Username:");
        lblUser.setForeground(new Color(200, 230, 201)); // hijau muda
        panel.add(lblUser, gbc);
        gbc.gridx = 1;
        panel.add(txtUsername, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel lblPass = new JLabel("Password:");
        lblPass.setForeground(new Color(200, 230, 201)); // hijau muda
        panel.add(lblPass, gbc);
        gbc.gridx = 1;
        panel.add(txtPassword, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        // === Styling tombol login ===
        btnLogin.setBackground(new Color(200, 230, 201)); // hijau pastel terang
        btnLogin.setForeground(new Color(27, 94, 32));    // teks hijau tua
        btnLogin.setFocusPainted(false);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnLogin.setBorder(BorderFactory.createLineBorder(new Color(27, 94, 32), 2));
        panel.add(btnLogin, gbc);

        add(panel, BorderLayout.CENTER);

        // === Aksi tombol login ===
        btnLogin.addActionListener(e -> {
            String username = txtUsername.getText().trim();
            String password = new String(txtPassword.getPassword()).trim();

            // Validasi login
            if (username.equals("admin") && password.equals("admin123")) {
                dispose();
                new Dashboard("admin").setVisible(true);
            } else if (username.equals("kasir") && password.equals("kasir123")) {
                dispose();
                new TransaksiForm(null, new TransaksiDAO(), null);

            } else {
                JOptionPane.showMessageDialog(this, "Username atau Password salah!");
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }
}
