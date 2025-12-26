package view.component;

import javax.swing.*;
import java.awt.*;

public class HeaderPanel extends JPanel {
  private String title;
  private Color backgroundColor = new Color(232, 245, 233);
  private Color textColor = new Color(27, 94, 32);

  public HeaderPanel(String title) {
    this.title = title;
    initializeUI();
  }

  public HeaderPanel(String title, Color backgroundColor, Color textColor) {
    this.title = title;
    this.backgroundColor = backgroundColor;
    this.textColor = textColor;
    initializeUI();
  }

  private void initializeUI() {
    setLayout(new BorderLayout());
    setBackground(backgroundColor);
    setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

    JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
    lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
    lblTitle.setForeground(textColor);

    add(lblTitle, BorderLayout.CENTER);
  }

  public void setTitle(String newTitle) {
    this.title = newTitle;
    removeAll();
    initializeUI();
    revalidate();
    repaint();
  }

  public String getTitle() {
    return title;
  }
}