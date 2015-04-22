package tk.ivybits;

import javax.swing.*;
import java.awt.*;

class SongPicker extends JPanel {
    final MenuPanel parent;

    public SongPicker(MenuPanel parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        add(new JScrollPane(new JPanel() {
            {
                setOpaque(false);
                setLayout(new BorderLayout());
                add(new SubSongPicker(SongPicker.this), BorderLayout.NORTH);
            }
        }) {{
            setPreferredSize(new Dimension(500, 0));
            setOpaque(false);
            getViewport().setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder());
            setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            getVerticalScrollBar().setUnitIncrement(16);

            InputMap im = getVerticalScrollBar().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
            im.put(KeyStroke.getKeyStroke("DOWN"), "positiveUnitIncrement");
            im.put(KeyStroke.getKeyStroke("UP"), "negativeUnitIncrement");
        }}, BorderLayout.EAST);
        setOpaque(false);
    }
}
