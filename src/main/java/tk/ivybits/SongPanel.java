package tk.ivybits;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

class SongPanel extends JPanel {
    public boolean selected;
    Color borderColor;
    private static Image PREVIEW_ICON;

    static {
        try {
            PREVIEW_ICON = ImageIO.read(new File("moonlight.jpg"));
            PREVIEW_ICON = PREVIEW_ICON.getScaledInstance(-1, Game.TAB_HEIGHT, BufferedImage.SCALE_SMOOTH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void paintComponent(Graphics _g) {
        Graphics2D g = (Graphics2D) _g;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setClip(new RoundRectangle2D.Float(0, 0, getWidth(), getPreferredSize().height, 10, 10));

        super.paintComponent(g);
    }

    public SongPanel(String name) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(0, Game.TAB_HEIGHT));

        borderColor = Game.TAB_NORMAL_BORDER;

        Border innerBorder = new LeftRoundedBorder();

        setBackground(Game.TAB_NORMAL_FILL);
        setOpaque(false);

        add(new JLabel(new ImageIcon(PREVIEW_ICON)) {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                innerBorder.paintBorder(SongPanel.this, g, 0, 0, SongPanel.this.getWidth(), SongPanel.this.getHeight());
            }
        }, BorderLayout.WEST);
        add(new JLabel("<html>&nbsp;&nbsp;&nbsp;<b>" + name + "</b></html>") {
            {
                setFont(Game.WFONT.deriveFont(15f));
                setForeground(Color.WHITE);
            }

            @Override
            public void paintComponent(Graphics _g) {
                Graphics2D g = (Graphics2D) _g;
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.setColor(SongPanel.this.getBackground());
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        }, BorderLayout.CENTER);
    }

    private class LeftRoundedBorder extends AbstractBorder {
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Color _color = g.getColor();

            g.setClip(0, 0, width, height);
            g.setColor(getBackground());
            if (selected) g.fillRoundRect(x, y, PREVIEW_ICON.getWidth(null), height, 10, 10);
            g.setColor(borderColor);
            g.setClip(0, 0, Game.TAB_HEIGHT / 2, Game.TAB_HEIGHT / 2);
            g.drawRoundRect(x, y, width - 1, height - 1, 10, 10);
            g.setClip(0, height - Game.TAB_HEIGHT / 2, Game.TAB_HEIGHT / 2, Game.TAB_HEIGHT / 2);
            g.drawRoundRect(x, y, width - 1, height - 1, 10, 10);
            g.setClip(Game.TAB_HEIGHT / 2, 0, width - Game.TAB_HEIGHT / 2, height);
            g.drawRect(x, y, width + 5, height - 1);
            g.setClip(0, 0, width, height);

            g.setColor(_color);
        }
    }
}
