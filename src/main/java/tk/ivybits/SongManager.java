package tk.ivybits;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SongManager {
    public static class Song {
        public File subtitle, video;
        public String name;

        public Song(String name, File subtitle, File video) {
            this.subtitle = subtitle;
            this.video = video;
            this.name = name;
        }

        public static class SubtitleFragment {
            public final long start;
            public final long end;
            public final String content;

            public SubtitleFragment(long start, long end, String content) {
                this.start = start;
                this.end = end;
                this.content = content;
            }

            @Override
            public String toString() {
                return "SubtitleFragment{" +
                        "start=" + start +
                        ", end=" + end +
                        ", content='" + content + '\'' +
                        '}';
            }
        }

        private static long formatTime(String time) {
            List<Float> data = Arrays.stream(time.split(":")).map(Float::parseFloat).collect(Collectors.toList());
            int h = (int) (float) data.get(0);
            int m = (int) (float) data.get(1);
            float _s = data.get(2);
            int s = (int) _s;
            int ms = (int) (_s - s * 10);

            return ms + s * 1000 + m * 60 * 1000 + h * 60 * 60 * 1000 - 1000;
        }

        public List<SubtitleFragment> getFrags() throws IOException {
            String[] dialog = Arrays.stream(new String(Files.readAllBytes(subtitle.toPath())).split("\r\n\r\n"))
                    .filter((x) -> x.startsWith("[Events]")).collect(Collectors.toList()).get(0).split("\n");
            List<SubtitleFragment> frags = new ArrayList<>();
            for (int i = 2; i < dialog.length; i++) {
                String[] data = dialog[i].split(",");
                String content = data[data.length - 1].trim();
                frags.add(new SubtitleFragment(formatTime(data[1]), formatTime(data[2]), content));
            }
            return frags;
        }
    }

    public static final List<Song> LIBRARY = new ArrayList<Song>() {{
        File songdir = new File("songs");
        File[] songs = songdir.listFiles();
        Arrays.sort(songs, (x, y) -> {
            return (int) (x.lastModified() - y.lastModified());
        });
        for (File song : songs) {
            add(new Song(song.getName(), new File(song, "subtitle.ass"), new File(song, "video.mp4")));
        }
    }};
}
