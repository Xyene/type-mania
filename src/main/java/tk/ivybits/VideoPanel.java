package tk.ivybits;

import tk.ivybits.javi.media.Media;
import tk.ivybits.javi.media.MediaFactory;
import tk.ivybits.javi.swing.StreamListener;
import tk.ivybits.javi.swing.SwingMediaPanel;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

class VideoPanel extends JPanel {
    SwingMediaPanel videoPanel;
    final Game parent;
    final SongManager.Song song;

    public VideoPanel(Game m, SongManager.Song s) throws IOException {
        parent = m;
        song = s;

        setLayout(new BorderLayout());
        Media media = MediaFactory.open(s.video);

        GameCanvas gameCanvas = new GameCanvas(this, s);
        m.setGlassPane(gameCanvas);
        gameCanvas.setVisible(true);

        videoPanel = new SwingMediaPanel(media) {
            @Override
            protected void doRepaint() {
            }
        };
        long length = media.length();
        System.err.printf("Video is %s milliseconds (%s seconds) long.\n", length, (double) length / 1000.0D);
        System.err.println("Streams");

        videoPanel.setVideoStream(media.videoStreams().get(0));

        videoPanel.setAudioStream(media.audioStreams().get(0));

        videoPanel.setBackground(Color.BLACK);
        setBackground(Color.BLACK);

        add(videoPanel, BorderLayout.CENTER);

        videoPanel.addStreamListener(new StreamListener() {
            public void onStart() {
                System.err.println("Playback started.");
            }

            public void onEnd() {
                System.err.println("Playback finished.");
            }

            public void onSeek(long to) {
                System.out.println(to);
            }
        });
        videoPanel.start();
    }

}
