package tk.ivybits;

import javax.swing.*;
import java.awt.*;

class MenuPanel extends JPanel {
    final Game parent;

    public MenuPanel(Game parent) {
        this.parent = parent;
        setBackground(Game.BACKGROUND_COLOR);
        setLayout(new BorderLayout());
        add(new SongPicker(this), BorderLayout.CENTER);
    }

    public void paintComponent(Graphics _g) {
        super.paintComponent(_g);
        Graphics2D g = (Graphics2D) _g;
        g.setColor(Game.BACKGROUND_SHADE);
        for (int x = 0; x < getWidth(); x += Game.HONEYCOMB.getWidth()) {
            for (int y = 0; y < getHeight(); y += Game.HONEYCOMB.getHeight()) {
                g.drawImage(Game.HONEYCOMB, x, y, null);
            }
        }
    }
}
