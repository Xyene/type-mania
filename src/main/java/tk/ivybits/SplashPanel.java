package tk.ivybits;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static tk.ivybits.Game.GAME_UPS;

class SplashPanel extends JPanel {
    private static final String SPLASH = "type!mania";
    private KeyAdapter keys;
    private Game game;

    public SplashPanel(Game game) {
        this.game = game;
        setBackground(Game.BACKGROUND_COLOR);
        setLayout(new BorderLayout());
        add(new JPanel() {
            {
                setOpaque(false);
                setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
                add(Box.createVerticalGlue());
                add(new JPanel() {
                    {
                        setOpaque(false);
                        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
                        add(Box.createHorizontalGlue());
                        add(new JLabel(SPLASH, JLabel.CENTER) {
                            {
                                setFont(Game.WFONT.deriveFont(50f));
                                setForeground(Color.WHITE);
                            }
                        });
                        add(Box.createHorizontalGlue());
                    }
                });
                add(new JLabel("type to begin", JLabel.CENTER) {
                    {
                        setFont(Game.WFONT.deriveFont(15f).deriveFont(Font.ITALIC));
                        setForeground(Color.WHITE);
                    }
                });
                add(Box.createVerticalGlue());
            }
        }, BorderLayout.CENTER);
        add(new JLabel("Tudor Brindus 2015", JLabel.LEFT) {
            {
                setFont(Game.WFONT.deriveFont(9f));
                setForeground(Color.WHITE);
            }
        }, BorderLayout.SOUTH);

        Runnable start = () -> {
            // Here we highlight the background
            setBackground(Game.TAB_HIGHLIGHT_BORDER);
            // and immediately paint
            paintImmediately(getBounds());
            // This next part will take a long time, and our frame won't be repainted during this time
            // therefore, the highlight appears as a sort of "loading" recognition
            game.remove(SplashPanel.this);
            game.add(new MenuPanel(game), BorderLayout.CENTER);
            game.revalidate();
            game.repaint(0);
        };
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                start.run();
            }
        });
        game.addKeyListener(keys = new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                start.run();
            }
        });
    }

    private Timer updater;

    @Override
    public void addNotify() {
        super.addNotify();
        updater = new Timer(1000 / GAME_UPS, (x) -> {
            try {
                paintImmediately(0, 0, getWidth(), getHeight());
            } catch (NullPointerException ignored) {
                // Java bug, not my fault:
                // Exception in thread "AWT-EventQueue-0" java.lang.NullPointerException
                //     at javax.swing.JComponent.paintChildren(Unknown Source)
            }
        });
        updater.start();
    }

    @Override
    public void removeNotify() {
        if (updater.isRunning()) updater.stop();
        updater = null;
        game.removeKeyListener(keys);
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
