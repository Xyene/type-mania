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

package tk.ivybits.javi.media.stream;

import tk.ivybits.javi.media.Media;

import java.io.Closeable;
import java.util.Locale;

/**
 * Represents an arbitrary stream in a container.
 */
public interface Stream extends Closeable {
    /**
     * Fetches the parent container.
     *
     * @return Said parent container.
     */
    Media container();

    /**
     * Fetches the type of this stream.
     *
     * @return A Type. Current possible values are STREAM_VIDEO and STREAM_AUDIO.
     */
    Type type();

    /**
     * Fetches the locale of this stream.
     *
     * @return The locale this stream is designated for, nor null if undefined.
     */
    Locale language();

    /**
     * Fetches the index of this stream in the parent container.
     *
     * @return The index of the stream.
     */
    int index();

    /**
     * Fetches the name of the codec.
     *
     * @return The name of the codec.
     * @since 1.0
     */
    String codecName();

    /**
     * Fetches a descriptive name of the codec.
     *
     * @return The name; format may differ substantially from codec to codec.
     * @since 1.0
     */
    String longCodecName();

    @Override
    void close();

    /**
     * Enum for all possible streams that a container may hold.
     *
     * @version 1.0
     * @since 1.0
     */
    public static enum Type {
        STREAM_VIDEO,
        STREAM_AUDIO,
        STREAM_DATA,
        STREAM_SUBTITLE,
        STREAM_ATTACHMENT
    }
}
