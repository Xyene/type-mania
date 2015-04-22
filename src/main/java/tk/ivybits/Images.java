package tk.ivybits;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import static tk.ivybits.Game.WFONT;

public class Images {
    // A cache with a bad policy is another term for a memory leak
    private static final HashMap<Object, BufferedImage> CACHE_FOREVER_IMAGE_CACHE = new HashMap<>();
    private static final GraphicsConfiguration GFX_CONF
            = GraphicsEnvironment
            .getLocalGraphicsEnvironment()
            .getDefaultScreenDevice()
            .getDefaultConfiguration();

    public static BufferedImage renderImage(String word, Color color) {
        class _Key {
            String word;
            Color color;

            public _Key(String word, Color color) {
                this.word = word;
                this.color = color;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;

                _Key key = (_Key) o;

                if (!color.equals(key.color)) return false;
                if (!word.equals(key.word)) return false;

                return true;
            }

            @Override
            public int hashCode() {
                int result = word.hashCode();
                result = 31 * result + color.hashCode();
                return result;
            }
        }
        _Key key = new _Key(word, color);
        BufferedImage k = CACHE_FOREVER_IMAGE_CACHE.get(key);
        if (k != null) return k;
        // Render the image in a headless context
        FontRenderContext frc = new FontRenderContext(new AffineTransform(), true, true);
        int w = (int) Math.ceil(WFONT.getStringBounds(word, frc).getWidth());
        int h = (int) Math.ceil(WFONT.getStringBounds(word, frc).getHeight());

        k = GFX_CONF.createCompatibleImage(w, h, Transparency.TRANSLUCENT);

        Graphics2D g = k.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setColor(new Color(0, 0, 0, 0));
        g.fillRect(0, 0, w, h);
        g.setFont(WFONT);
        g.setColor(color);
        g.drawString(word, 0, WFONT.getSize());
        CACHE_FOREVER_IMAGE_CACHE.put(key, k);
        return k;
    }
}
