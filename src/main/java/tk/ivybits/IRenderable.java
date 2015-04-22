package tk.ivybits;

import java.awt.*;

public interface IRenderable {
    boolean isActive();

    void update(double v);

    void draw(Graphics2D g);
}
