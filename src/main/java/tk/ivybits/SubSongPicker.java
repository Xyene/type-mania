package tk.ivybits;

import tk.ivybits.javi.JAVI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

class SubSongPicker extends JPanel {

    public SubSongPicker(SongPicker parent) {
        setLayout(new GridLayout(0, 1, 10, 10));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        for (SongManager.Song s : SongManager.LIBRARY) {
            // Defer all this loading
            SongPanel song = new SongPanel(s.name);
            JPanel spacer = new JPanel() {
                {
                    setPreferredSize(new Dimension(Game.SPACER_WIDTH, 80));
                    setOpaque(false);
                }
            };

            JPanel box = new JPanel() {
                {
                    setOpaque(false);
                    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
                    add(spacer);
                    add(song);
                }
            };

            MouseAdapter adp = new MouseAdapter() {
                Timer resizer, resizerDelay;

                @Override
                public void mouseEntered(MouseEvent e) {
                    song.setBackground(Game.TAB_HIGHLIGHT_FILL);
                    song.borderColor = Game.TAB_HIGHLIGHT_BORDER;
                    song.selected = true;
                    if (resizer != null) resizer.stop();
                    if (resizerDelay != null) resizerDelay.stop();

                    resizerDelay = new Timer(Game.RESIZE_ANIMATION_DELAY, (x) -> {
                        if (song.selected) {
                            resizer = new Timer(5, (_x) -> {
                                if (spacer.getWidth() > 0) {
                                    spacer.setPreferredSize(new Dimension(spacer.getWidth() - 2, spacer.getHeight()));
                                    spacer.revalidate();
                                } else {
                                    if (resizer != null) resizer.stop();
                                    resizer = null;
                                }
                            });
                            resizer.start();
                        }
                        resizerDelay.stop();
                        resizerDelay = null;
                    });
                    resizerDelay.start();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    song.setBackground(Game.TAB_NORMAL_FILL);
                    song.borderColor = Game.TAB_NORMAL_BORDER;
                    song.selected = false;

                    if (resizer != null) resizer.stop();
                    resizer = new Timer(5, (_x) -> {
                        if (spacer.getWidth() < Game.SPACER_WIDTH) {
                            spacer.setPreferredSize(new Dimension(spacer.getWidth() + 2, spacer.getHeight()));
                            spacer.revalidate();
                        } else {
                            resizer.stop();
                            resizer = null;
                        }
                    });
                    resizer.start();
                }

                @Override
                public void mouseClicked(MouseEvent evt) {
                    if (evt.getClickCount() >= 2) {

                        // Here we highlight the background
                        parent.parent.setBackground(Color.DARK_GRAY);
                        // and immediately paint
                        parent.parent.paintImmediately(parent.parent.getBounds());
                        parent.paintImmediately(parent.getBounds());
                        paintImmediately(getBounds());

                        Game m = parent.parent.parent;
                        m.remove(parent.parent);

                        JAVI.initialize();

                        try {
                            m.add(new VideoPanel(m, s));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        m.revalidate();
                    }
                }
            };
            song.addMouseListener(adp);
            song.addMouseMotionListener(adp);
            add(box);
        }
    }
}
