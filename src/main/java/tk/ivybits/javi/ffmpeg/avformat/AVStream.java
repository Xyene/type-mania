/*
 * This file is part of JAVI.
 *
 * JAVI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * JAVI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with JAVI.  If not, see
 * <http://www.gnu.org/licenses/>.
 */

package tk.ivybits.javi.ffmpeg.avformat;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import tk.ivybits.javi.ffmpeg.avcodec.AVCodecContext;
import tk.ivybits.javi.ffmpeg.avcodec.AVPacket;
import tk.ivybits.javi.ffmpeg.avutil.AVDictionary;
import tk.ivybits.javi.ffmpeg.avutil.AVRational;

import java.util.Arrays;
import java.util.List;

public class AVStream extends Structure {
    public static class ByReference extends AVStream implements Structure.ByReference {
    }

    public int index;
    public int id;
    public AVCodecContext.ByReference codec;
    public Pointer priv_data;
    public AVFrac pts;
    public AVRational time_base;
    public long start_time;
    public long duration;
    public long nb_frames;
    public int disposition;
    public int discard;
    public AVRational sample_aspect_ratio;
    public AVDictionary metadata;
    public AVRational avg_frame_rate;
    public AVPacket attached_pic;
    public Pointer info;
    public int pts_wrap_bits;
    @Deprecated
    public long do_not_use;
    public long first_dts;
    public long cur_dts;
    public long last_IP_pts;
    public int last_IP_duration;
    public int probe_packets;
    public int codec_info_nb_frames;
    public int /* AVStreamParseType */ need_parsing;
    public Pointer /* AVCodecParserContext */ parser;
    public Pointer /* AVPacketList.ByReference */ last_in_packet_buffer;
    public AVProbeData probe_data;
    public long[] pts_buffer = new long[MAX_REORDER_DELAY + 1];
    public Pointer /* AVIndexEntry */ index_entries;
    public int nb_index_entries;
    public int index_entries_allocated_size;
    public AVRational r_frame_rate;
    public int stream_identifier;
    public long interleaver_chunk_size;
    public long interleaver_chunk_duration;
    public int request_probe;
    public int skip_to_keyframe;
    public int skip_samples;
    public int nb_decoded_frames;
    public long mux_ts_offset;
    public long pts_wrap_reference;
    public int pts_wrap_behavior;

    public static final int MAX_STD_TIMEBASES = 60 * 12 + 6;
    public static final int MAX_PROBE_PACKETS = 2500;
    public static final int MAX_REORDER_DELAY = 16;

    public AVStream() {
        super();
    }

    public AVStream(Pointer address) {
        super(address);
        read();
    }

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("index", "id", "codec", "priv_data", "pts", "time_base", "start_time",
                "duration", "nb_frames", "disposition", "discard",
                "sample_aspect_ratio", "metadata", "avg_frame_rate", "attached_pic",
                "info", "pts_wrap_bits", "do_not_use", "first_dts", "cur_dts",
                "last_IP_pts", "last_IP_duration", "probe_packets", "codec_info_nb_frames",
                "need_parsing", "parser", "last_in_packet_buffer", "probe_data",
                "pts_buffer", "index_entries", "nb_index_entries", "index_entries_allocated_size",
                "r_frame_rate", "stream_identifier", "interleaver_chunk_size", "interleaver_chunk_duration",
                "request_probe", "skip_to_keyframe", "skip_samples", "nb_decoded_frames",
                "mux_ts_offset", "pts_wrap_reference", "pts_wrap_behavior");
    }
}
