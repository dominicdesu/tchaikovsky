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

import java.util.Map;

import de.kaizencode.tchaikovsky.exception.SpeakerException;

/**
 * A single item in the current playlist
 * 
 * @author Dominic Lerbs
 */
public interface PlaylistItem {

    /**
     * @return URL of the item.
     */
    String getUrl();

    /**
     * @return Title of the item
     */
    String getTitle();

    /**
     * @return Artist of the item
     */
    String getArtist();

    /**
     * @return URL containing the cover art/thumbnail for this item
     */
    String getThumbnailUrl();

    /**
     * @return Duration of the item in milliseconds
     */
    long getDurationInMs();

    /**
     * @return The media type of the item, e.g. audio
     */
    String getMediaType();

    /**
     * @return Album name of the item
     */
    String getAlbum();

    /**
     * @return Genre of the item
     */
    String getGenre();

    /**
     * @return Map of other data, such as country or channel information
     */
    Map<String, String> getOtherData();

    /**
     * Returns medium description, such as codec, container or protocol. Note that in AllJoyn a <code>Variant</code> is
     * returned, which could be any kind of object. The library tries to convert the returned Variant to a Java object.
     * If an unsupported object type is encountered, a {@link SpeakerException} is thrown.
     * 
     * @return Map containing medium description values
     * @throws SpeakerException
     *             Exception if the AllJoyn <code>Variant</code> object cannot be processed
     */
    Map<String, Object> getMediumDescription() throws SpeakerException;

    /**
     * Returns custom user data. Note that in AllJoyn a <code>Variant</code> is returned, which could be any kind of
     * object. The library tries to convert the returned Variant to a Java object. If an unsupported object type is
     * encountered, a {@link SpeakerException} is thrown.
     * 
     * @return The user data converted to Object
     * @throws SpeakerException
     *             Exception if the AllJoyn <code>Variant</code> object cannot be processed
     */
    Object getUserData() throws SpeakerException;

}