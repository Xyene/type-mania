package tk.ivybits;

import tk.ivybits.SongManager.Song.SubtitleFragment;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static tk.ivybits.Game.*;

class WordCloudManager implements Iterable<WordCloudManager.WordFloat> {
    final ArrayList<WordFloat> words = new ArrayList<>();
    public final ArrayList<WordFloat> endlineCrossed = new ArrayList<>();
    private SongManager.Song song;
    private List<SubtitleFragment> frags;

    private int currentGroup = 0;

    public WordCloudManager(SongManager.Song song) throws IOException {
        this.song = song;
        frags = song.getFrags();
    }

    void spawnWord(String word) {
        // Some subtitle lines may be empty; we can't render an image with w = 0
        if (word.length() == 0) return;
        currentGroup++;
        int depth = 0;
        for (String y : word.split(" ")) {
            WordFloat wf = new WordFloat(y, depth, currentGroup);
            depth += wf.render.getWidth() + 10;
            words.add(wf);
        }
    }

    public void updateCloud(long time, double v) {
        List<SubtitleFragment> queued = new ArrayList<>();
        frags.removeIf((x) -> {
            if (x.start <= time) {
                queued.add(x);
                return true;
            }
            return false;
        });
        queued.forEach(x -> spawnWord(x.content));
        endlineCrossed.clear();
        for (int i = 0; i < words.size(); i++) {
            WordFloat wf = words.get(i);
            wf.update(v);
            if (wf.ptx.x + wf.render.getWidth() <= 0) {
                words.remove(wf);
            } else {
                float l = wf.ptx.x;
                float r = wf.ptx.x + wf.render.getWidth();
                if (l <= GAME_ENDLINE_X && GAME_ENDLINE_X <= r) endlineCrossed.add(wf);
            }
        }
    }

    @Override
    public Iterator<WordFloat> iterator() {
        return words.iterator();
    }

    public class WordFloat {
        public Point2D.Float ptx;
        public final String word;
        public BufferedImage render;
        public final int group;
        private Color col;

        public void render(Color col) {
            if (this.col == col) return;
            this.col = col;
            this.render = Images.renderImage(word, col);
        }

        public WordFloat(String word, int depth, int group) {
            this.word = word;
            this.group = group;
            render(Color.WHITE);
            // this can actually loop forever, but who cares
            boolean intersects;
            do {
                intersects = false;
                ptx = new Point2D.Float(W + depth, (int) (Math.random() * (H - 200) + 100));
                for (WordFloat f : words) {
                    if (f != this && getBounds().intersects(f.getBounds())) {
                        intersects = true;
                    }
                }
            } while (intersects);
        }

        public Point getCenter() {
            return new Point((int) ptx.x + render.getWidth() / 2, (int) ptx.y + render.getHeight() / 2);
        }

        public Rectangle2D getBounds() {
            return new Rectangle2D.Float(ptx.x, ptx.y - 5, render.getWidth(), render.getHeight() + 10);
        }

        public void update(double v) {
            if (ptx.x < Game.GAME_ENDLINE_X) v *= 2.0 / 3;
            ptx.x -= v;
        }

        public void draw(Graphics2D g) {
            g.drawImage(render, (int) ptx.x, (int) ptx.y, null);
        }
    }
}
