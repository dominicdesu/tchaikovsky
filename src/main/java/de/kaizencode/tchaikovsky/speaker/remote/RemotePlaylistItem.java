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

import java.util.Map;

import org.alljoyn.bus.Variant;
import org.alljoyn.bus.annotation.Position;
import org.alljoyn.bus.annotation.Signature;

import de.kaizencode.tchaikovsky.businterface.VariantConverter;
import de.kaizencode.tchaikovsky.exception.SpeakerException;
import de.kaizencode.tchaikovsky.speaker.PlaylistItem;

public class RemotePlaylistItem implements PlaylistItem {

    @Position(0)
    @Signature("s")
    public String url;

    @Position(1)
    @Signature("s")
    public String title;

    @Position(2)
    @Signature("s")
    public String artist;

    @Position(3)
    @Signature("s")
    public String thumbnailUrl;

    @Position(4)
    @Signature("x")
    public long durationInMs;

    @Position(5)
    @Signature("s")
    public String mediaType;

    @Position(6)
    @Signature("s")
    public String album;

    @Position(7)
    @Signature("s")
    public String genre;

    @Position(8)
    @Signature("a{ss}")
    public Map<String, String> otherData;

    @Position(9)
    @Signature("a{sv}")
    public Map<String, Variant> mediumDescription;

    @Position(10)
    @Signature("v")
    public Variant userData;

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getArtist() {
        return artist;
    }

    @Override
    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    @Override
    public long getDurationInMs() {
        return durationInMs;
    }

    @Override
    public String getMediaType() {
        return mediaType;
    }

    @Override
    public String getAlbum() {
        return album;
    }

    @Override
    public String getGenre() {
        return genre;
    }

    @Override
    public Map<String, String> getOtherData() {
        return otherData;
    }

    @Override
    public Map<String, Object> getMediumDescription() throws SpeakerException {
        return VariantConverter.convertToMap(mediumDescription);
    }

    @Override
    public Object getUserData() throws SpeakerException {
        return VariantConverter.convert(userData);
    }

    @Override
    public String toString() {
        return artist + " - " + title + " (" + url + ")";
    }

}
