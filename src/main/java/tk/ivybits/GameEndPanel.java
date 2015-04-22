package tk.ivybits;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class GameEndPanel extends JPanel {
    public GameEndPanel(GameCanvas cvs) {
        setBackground(Game.BACKGROUND_COLOR);
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(Box.createHorizontalGlue());
        add(new JPanel() {
            {
                setOpaque(false);
                setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
                add(Box.createVerticalGlue());
                Font labelFont = Game.WFONT.deriveFont(25f);

                class _Label extends JLabel {
                    public _Label(String str, int align, Runnable callback) {
                        super(str, align);
                        setBackground(Game.TAB_NORMAL_FILL);
                        MouseAdapter adp = new MouseAdapter() {
                            @Override
                            public void mouseEntered(MouseEvent e) {
                                setBackground(Game.TAB_HIGHLIGHT_BORDER);
                            }

                            @Override
                            public void mouseExited(MouseEvent e) {
                                setBackground(Game.TAB_NORMAL_FILL);
                            }

                            @Override
                            public void mouseClicked(MouseEvent e) {
                                callback.run();
                            }
                        };
                        addMouseListener(adp);
                        addMouseMotionListener(adp);
                    }

                    @Override
                    public void paintComponent(Graphics _g) {
                        Graphics2D g = (Graphics2D) _g;
                        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g.setColor(getBackground());
                        g.fillRect(0, 0, getWidth(), getHeight());
                        super.paintComponent(g);
                    }
                }

                add(new JPanel() {{
                    setOpaque(false);
                    setPreferredSize(new Dimension(75, 0));
                    setLayout(new GridLayout(0, 1, 10, 10));
                    add(new _Label("Retry", JLabel.CENTER, () -> {
                        cvs.videoPanel.parent.remove(GameEndPanel.this);
                        try {
                            cvs.videoPanel.parent.add(new VideoPanel(cvs.videoPanel.parent, cvs.videoPanel.song));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        cvs.videoPanel.parent.revalidate();
                        cvs.videoPanel.parent.repaint();
                    }) {
                        {
                            setFont(labelFont);
                            setForeground(Color.WHITE);
                            setBorder(BorderFactory.createLineBorder(Game.TAB_NORMAL_BORDER));
                        }
                    });
                    add(new _Label("Back to Menu", JLabel.CENTER, () -> {
                        cvs.videoPanel.parent.remove(GameEndPanel.this);
                        cvs.videoPanel.parent.add(new MenuPanel(cvs.videoPanel.parent));
                        cvs.videoPanel.parent.revalidate();
                        cvs.videoPanel.parent.repaint();
                    }) {
                        {
                            setFont(labelFont);
                            setForeground(Color.WHITE);
                            setBorder(BorderFactory.createLineBorder(Game.TAB_NORMAL_BORDER));
                        }
                    });
                }});
                add(Box.createVerticalGlue());
            }
        });
        add(Box.createHorizontalGlue());
    }

    @Override
    public void paintComponent(Graphics _g) {
        super.paintComponent(_g);
        Graphics2D g = (Graphics2D) _g;
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setColor(Game.BACKGROUND_SHADE);
        for (int x = 0; x < getWidth(); x += Game.HONEYCOMB.getWidth()) {
            for (int y = 0; y < getHeight(); y += Game.HONEYCOMB.getHeight()) {
                g.drawImage(Game.HONEYCOMB, x, y, null);
            }
        }
    }
}
