package view.component;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class SidebarPanel extends JPanel {
    private Consumer<String> menuClickListener;

    public SidebarPanel(Consumer<String> menuClickListener) {
        this.menuClickListener = menuClickListener;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(27, 94, 32));
        setPreferredSize(new Dimension(220, 600));
        initializeUI();
    }

    private void initializeUI() {
        JLabel lblTitle = new JLabel("Toko Bunga Wilis", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(new Color(200, 230, 201));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(lblTitle);

        add(Box.createVerticalGlue());
        
        add(createMenuButton("Produk", "produk"));
        add(Box.createVerticalStrut(15));
        add(createMenuButton("Supplier", "supplier"));
        add(Box.createVerticalStrut(15));
        add(createMenuButton("Transaksi", "transaksi"));
        add(Box.createVerticalStrut(15));
        add(createMenuButton("Logout", "logout"));
        
        add(Box.createVerticalGlue());
    }

    private JButton createMenuButton(String text, String menuId) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(190, 50));
        btn.setBackground(new Color(200, 230, 201));
        btn.setForeground(new Color(27, 94, 32));
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 30));
        
        btn.addActionListener(e -> {
            if (menuClickListener != null) {
                menuClickListener.accept(menuId);
            }
        });
        
        return btn;
    }
}