package view.component;

import javax.swing.*;
import java.awt.*;

public class ToastNotification {
  public static void show(JFrame parent, String message) {
    show(parent, message, 3000); // Default 3 seconds
  }

  public static void show(JFrame parent, String message, int duration) {
    JWindow toast = new JWindow(parent);
    toast.setLayout(new BorderLayout());

    JPanel panel = new JPanel(new BorderLayout());
    panel.setBackground(new Color(27, 94, 32, 220));
    panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

    JLabel label = new JLabel(
        "<html><div style='text-align: center; color: white;'>" +
            message + "</div></html>",
        SwingConstants.CENTER);
    label.setFont(new Font("Segoe UI", Font.PLAIN, 12));

    panel.add(label, BorderLayout.CENTER);
    toast.add(panel);
    toast.setSize(250, 50);

    // Position at top-right corner
    Point loc = parent.getLocation();
    toast.setLocation(loc.x + parent.getWidth() - 270, loc.y + 20);
    toast.setOpacity(0.9f);
    toast.setVisible(true);

    // Auto close timer
    Timer timer = new Timer(duration, e -> {
      toast.dispose();
      ((Timer) e.getSource()).stop();
    });
    timer.setRepeats(false);
    timer.start();
  }

  public static void showSuccess(JFrame parent, String message) {
    show(parent, "✅ " + message, 3000);
  }

  public static void showError(JFrame parent, String message) {
    show(parent, "❌ " + message, 4000);
  }

  public static void showInfo(JFrame parent, String message) {
    show(parent, "ℹ️ " + message, 3000);
  }
}