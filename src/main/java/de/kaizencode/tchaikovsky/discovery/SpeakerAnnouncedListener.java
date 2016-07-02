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
package de.kaizencode.tchaikovsky.discovery;

import de.kaizencode.tchaikovsky.speaker.Speaker;

/**
 * Listener for newly announced {@link Speaker}s.
 * 
 * @author Dominic Lerbs
 */
public interface SpeakerAnnouncedListener {

    /**
     * Called when a new speaker has been discovered.
     * 
     * @param speaker
     *            The {@link Speaker} which has been discovered
     */
    void onSpeakerAnnounced(Speaker speaker);

}
