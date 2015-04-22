package tk.ivybits;

import org.pushingpixels.substance.api.SubstanceLookAndFeel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Game extends JFrame {
    public static final int W = 960;
    public static final int H = 580;
    public static final Color TAB_HIGHLIGHT_FILL = new Color(237, 187, 70, 192);
    public static final Color TAB_HIGHLIGHT_BORDER = new Color(237, 140, 0, 240);
    public static final Color TAB_NORMAL_FILL = new Color(0, 115, 255, 75);
    public static final Color TAB_NORMAL_BORDER = new Color(0, 115, 255, 204);
    public static final Color BACKGROUND_COLOR = new Color(0x2E435D);
    public static final Color BACKGROUND_SHADE = new Color(0xd5d5d5);
    public static final Color GAME_DIM = new Color(0, 0, 0, 119);
    public static final int SPACER_WIDTH = 70;
    public static final int TAB_HEIGHT = 80;
    public static final int RESIZE_ANIMATION_DELAY = 175;
    public static final int GAME_UPS = 80;
    public static final int GAME_ENDLINE_X = 0;
    public static final Color[] GAME_LINE_COLORS = {
            new Color(255, 204, 17, 160),
            new Color(72, 255, 85, 160),
            new Color(153, 72, 255, 160),
            new Color(255, 29, 46, 160 )};
    public static final Color[] GAME_ENDLINE_COLOR = {Color.GREEN, Color.RED};

    public static BufferedImage HONEYCOMB;

    public static final Font WFONT = new Font("Verdana", Font.BOLD, 22);

    static {
        try {
            HONEYCOMB = ImageIO.read(new File("honey.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Game() {
        super("type!mania");
        setBackground(Color.BLACK);
        setLayout(new BorderLayout());
        add(new SplashPanel(this), BorderLayout.CENTER);
        setSize(W, H);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    public static void main(String[] argv) throws UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        //System.setProperty("sun.java2d.opengl", "True");
        System.setProperty("insubstantial.checkEDT", "false");
        System.setProperty("insubstantial.logEDT", "false");
        UIManager.put(SubstanceLookAndFeel.COLORIZATION_FACTOR, 1.0);

        //JFrame.setDefaultLookAndFeelDecorated(true);

        try {
            UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceModerateLookAndFeel");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Game nu = new Game();
        nu.setVisible(true);
        // nu.revalidate();
    }
}
