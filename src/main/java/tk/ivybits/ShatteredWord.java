package tk.ivybits;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class ShatteredWord implements IRenderable {
    public Image[] renders;
    public FragVector[] frags;

    public class FragVector {
        public double x, y;
        public double _x, _y;
        public double dx, dy;
        public double t;
        public double rot;
        public double rotDir;
        public boolean missed;
    }

    public ShatteredWord(WordCloudManager.WordFloat original, boolean missed) {
        renders = new Image[original.word.length()];
        frags = new FragVector[original.word.length()];
        double __x = original.getBounds().getX();
        double __y = original.getBounds().getY();
        for (int i = 0; i < original.word.length(); i++) {
            renders[i] = Images.renderImage(original.word.charAt(i) + "",
                    missed ? new Color(255, 0, 0, 240) : new Color(0, 255, 0, 240));
            FragVector v = new FragVector();
            v._x = __x;
            v._y = __y;
            __x += renders[i].getWidth(null);
            v.missed = missed;
            double angle = Math.random() * 180;
            double magnitude = 20;
            v.dx = magnitude * Math.cos(Math.toRadians(angle));
            v.dy = magnitude * -Math.sin(Math.toRadians(angle));
            v.rotDir = Math.random() >= 0.5 ? -1 : 1;
            frags[i] = v;
        }
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void update(double v) {
        for (FragVector frag : frags) {
            // x = vt
            // y = vt + 0.5at**2
            frag.t += v / 8;
            // (9.8 / (frag.missed ? 1 : 2)) * (frag.missed ? 1 : -1)
            frag.y = frag._y + frag.dy * frag.t + 0.5 * 9.8 * frag.t * frag.t;
            frag.x = frag._x + frag.dx * frag.t;
            frag.rot += v;
        }
    }

    @Override
    public void draw(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        for (int i = 0; i < frags.length; i++) {
            FragVector frag = frags[i];
            AffineTransform at = new AffineTransform();
            at.rotate(Math.toRadians(frag.rot) * frag.rotDir, frag.x, frag.y);
            at.translate(frag.x, frag.y);
            g.drawImage(renders[i], at, null);
        }
    }
}
