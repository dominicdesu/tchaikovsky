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
package de.kaizencode.tchaikovsky.speaker.remote;

import java.util.Arrays;
import java.util.List;

import org.alljoyn.bus.annotation.Position;
import org.alljoyn.bus.annotation.Signature;

import de.kaizencode.tchaikovsky.speaker.PlayState;
import de.kaizencode.tchaikovsky.speaker.PlaylistItem;

public class RemotePlayState implements PlayState {

    @Position(0)
    @Signature("s")
    public String playState;

    @Position(1)
    @Signature("x")
    public long position;

    @Position(2)
    @Signature("u")
    public int currentSampleRate;

    @Position(3)
    @Signature("u")
    public int audioChannels;

    @Position(4)
    @Signature("u")
    public int bitsPerSample;

    @Position(5)
    @Signature("i")
    public int indexCurrentItem;

    @Position(6)
    @Signature("i")
    public int indexNextItem;

    @Position(7)
    @Signature("ar")
    public RemotePlaylistItem[] playlistItems;

    @Override
    public State getState() {
        return State.parse(playState);
    }

    @Override
    public long getPositionInMs() {
        return position;
    }

    @Override
    public int getCurrentSampleRate() {
        return currentSampleRate;
    }

    @Override
    public int getAudioChannels() {
        return audioChannels;
    }

    @Override
    public int getBitsPerSample() {
        return bitsPerSample;
    }

    @Override
    public int getIndexCurrentItem() {
        return indexCurrentItem;
    }

    @Override
    public int getIndexNextItem() {
        return indexNextItem;
    }

    @Override
    public List<PlaylistItem> getPlaylistItems() {
        return Arrays.asList(playlistItems);
    }

    @Override
    public String toString() {
        return playState + " at position " + position + " and index " + indexCurrentItem;
    }

}
