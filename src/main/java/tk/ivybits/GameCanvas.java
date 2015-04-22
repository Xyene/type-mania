package tk.ivybits;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import static tk.ivybits.Game.*;

class GameCanvas extends JPanel {
    VideoPanel videoPanel;
    private final WordCloudManager man;
    private String currentWord = "";
    private KeyAdapter keys;
    private final HealthBarWidget healthbar = new HealthBarWidget();
    private double health = 100;
    private final LinkedList<IRenderable> renders = new LinkedList<>();
    private final Set<WordCloudManager.WordFloat> missed = new HashSet<WordCloudManager.WordFloat>() {

        @Override
        public boolean add(WordCloudManager.WordFloat wf) {
            boolean x = super.add(wf);
            if (x) {
                renders.add(new ShatteredWord(wf, true));
                man.words.remove(wf);
            }
            return x;
        }
    };

    {
        setOpaque(false);
    }

    private Timer updater;

    public GameCanvas(VideoPanel videoPanel, SongManager.Song s) throws IOException {
        this.videoPanel = videoPanel;
        man = new WordCloudManager(s);
        videoPanel.parent.addKeyListener(keys = new KeyAdapter() {
            String filter(String x) {
                return x.replaceAll("[^a-zA-Z]", "");
            }

            @Override
            public void keyTyped(KeyEvent e) {
                char c = Character.toLowerCase(e.getKeyChar());
                if (c == '\b' && currentWord.length() > 0)
                    currentWord = currentWord.substring(0, currentWord.length() - 1);
                else if (c == '\u001b') currentWord = "";
                else if ('a' <= c && c <= 'z' ||
                        (c == ' ') ||
                        c == ',' || c == '\'') currentWord += c;
                else if (currentWord.length() > 0 && c == '\n') currentWord += " ";
                WordCloudManager.WordFloat wf = getFirstNonMissed();
                String a = wf == null ? "\0" : filter(wf.word).toLowerCase();
                String b = filter(currentWord).toLowerCase();
                if (currentWord.endsWith(" ") || a.equals(b)) {
                    currentWord = "";
                    if (a.equals(b)) {
                        man.words.remove(wf);
                        renders.add(new ShatteredWord(wf, false));
                        health = Math.max(0, Math.min(100, health + 5));
                    } else if (wf != null) {
                        wf.render(Color.RED);
                        health -= 7;
                        missed.add(wf);
                    }
                    healthbar.setHealth(health);
                }
            }
        });
    }

    @Override
    public void removeNotify() {
        if (updater.isRunning()) updater.stop();
        updater = null;
        super.removeNotify();
        videoPanel.parent.removeKeyListener(keys);
    }

    private WordCloudManager.WordFloat getFirstNonMissed() {
        for (WordCloudManager.WordFloat wf : man.words) {
            if (!missed.contains(wf)) {
                return wf;
            }
        }
        return null;
    }

    @Override
    public void addNotify() {
        super.addNotify();
        updater = new Timer(1000 / GAME_UPS, new ActionListener() {
            private long start = System.currentTimeMillis();

            @Override
            public void actionPerformed(ActionEvent x) {
                long diff = System.currentTimeMillis() - start;
                start = System.currentTimeMillis();
                double v = diff / (1000.0 / GAME_UPS) * 0.8;
                man.updateCloud(videoPanel.videoPanel.position(), v);
                for (IRenderable i : renders) i.update(v);
                try {
                    GameCanvas.this.paintImmediately(0, 0, getWidth(), getHeight());
                } catch (NullPointerException ignored) {
                    // Java bug, not my fault:
                    // Exception in thread "AWT-EventQueue-0" java.lang.NullPointerException
                    //     at javax.swing.JComponent.paintChildren(Unknown Source)
                }
                WordCloudManager.WordFloat wf = GameCanvas.this.getFirstNonMissed();
                if (wf != null) wf.render(Color.CYAN);
                man.endlineCrossed.stream().filter(missed::add).forEach(w -> {
                    health -= 7;
                    currentWord = "";
                });
                health -= 0.025;
                healthbar.setHealth(health);
                if (health <= 0) {
                    updater.stop();
                    JPanel empty = new JPanel();
                    empty.setVisible(true);
                    empty.setOpaque(false);
                    videoPanel.parent.setGlassPane(empty);
                    videoPanel.videoPanel.setPlaying(false);
                    videoPanel.parent.remove(videoPanel);
                    videoPanel.parent.add(new GameEndPanel(GameCanvas.this), BorderLayout.CENTER);
                    videoPanel.parent.revalidate();
                    videoPanel.parent.repaint();
                }
            }
        });
        updater.start();
    }

    public void paintComponent(Graphics _g) {
        super.paintComponent(_g);
        Graphics2D g = (Graphics2D) _g;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g.setColor(GAME_DIM);
        g.fillRect(0, 0, getWidth(), getHeight());

        for (IRenderable x : renders) x.draw(g);

        g.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.setColor(GAME_LINE_COLORS[0]);
        for (int i = 1; i < man.words.size(); i++) {
            WordCloudManager.WordFloat _b = man.words.get(i);
            WordCloudManager.WordFloat _a = man.words.get(i - 1);
            if (_a.group != _b.group) {
                continue;
            }
            g.setColor(GAME_LINE_COLORS[_b.group % GAME_LINE_COLORS.length]);
            Point a = _a.getCenter();
            Point b = _b.getCenter();
            g.drawLine(a.x, a.y, b.x, b.y);
        }
        g.setStroke(new BasicStroke(1));

        for (WordCloudManager.WordFloat wf : man.words) wf.draw(g);

//        g.setColor(GAME_ENDLINE_COLOR[man.endlineCrossed.size() > 0 ? 1 : 0]);
//        g.drawLine(GAME_ENDLINE_X, 0, GAME_ENDLINE_X, getHeight());
//        g.setColor(Color.darkGray);
//        g.drawLine(0, 50, getWidth(), 50);

        g.setColor(Game.TAB_HIGHLIGHT_FILL);
        String hud = "[ " + currentWord + " ]";
        g.setFont(WFONT);
        int w = g.getFontMetrics().stringWidth(hud);
        g.drawString(hud, W / 2 - w / 2, (int) (WFONT.getSize() * 1.25));

        healthbar.draw(g);
    }
}
