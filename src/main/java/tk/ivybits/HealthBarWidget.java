package tk.ivybits;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

class HealthBarWidget {
    private BufferedImage background;
    private BufferedImage colour;
    private BufferedImage healthSub;
    private Dimension dim;
    private final int HEALTH_OFFSET = 155 / 2;

    {
        try {
            background = ImageIO.read(new File("healthbar-bg.png"));
            colour = ImageIO.read(new File("healthbar-colour.png"));
            healthSub = colour;
            dim = new Dimension(background.getWidth(), background.getHeight());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics g) {
        g.drawImage(background, 10, 10, dim.width, dim.height, null, null);
        if (healthSub != null) g.drawImage(healthSub, 10, 10, healthSub.getWidth(), dim.height, null, null);
    }

    public void setHealth(double health) {
        int scaledHealth = (int) ((dim.width - HEALTH_OFFSET) * (health / 100.0));
        healthSub = health > 0 ? colour.getSubimage(0, 0, HEALTH_OFFSET + scaledHealth, colour.getHeight()) : null;
    }
}
