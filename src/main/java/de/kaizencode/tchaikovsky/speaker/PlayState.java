/**
 * Tchaikovsky - A Java library for controlling AllPlay-compatible devices.
 * Copyright (c) 2016 Dominic Lerbs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.kaizencode.tchaikovsky.speaker;

import java.util.List;

/**
 * Various information about the current state of the speaker.
 * 
 * @author Dominic Lerbs
 */
public interface PlayState {

    /**
     * The actual playing state of the speaker. As there is no complete list of states available, state might be
     * UNKNOWN.
     */
    public enum State {
        PAUSED, STOPPED, PLAYING, TRANSITIONING, BUFFERING, UNKNOWN;

        public static State parse(String rawString) {
            try {
                return State.valueOf(rawString);
            } catch (Exception e) {
                return State.UNKNOWN;
            }
        }
    }

    /**
     * @return Current Play{@link State} of the speaker
     */
    State getState();

    /**
     * @return Current position in milliseconds
     */
    long getPositionInMs();

    /**
     * @return Current sample rate
     */
    int getCurrentSampleRate();

    /**
     * @return Number of audio channels
     */
    int getAudioChannels();

    /**
     * @return Bits per Sample (bps)
     */
    int getBitsPerSample();

    /**
     * @return Index of the current item in the playlist
     */
    int getIndexCurrentItem();

    /**
     * @return Item of the next item in the playlist
     */
    int getIndexNextItem();

    /**
     * @return A list of all items in the playlist
     */
    List<PlaylistItem> getPlaylistItems();

}
