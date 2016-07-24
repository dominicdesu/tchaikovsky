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
package de.kaizencode.tchaikovsky.bus;

import java.util.ArrayList;
import java.util.List;

import org.alljoyn.bus.SessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.kaizencode.tchaikovsky.speaker.Speaker;

/**
 * Specific implementation of {@link SessionListener}
 * 
 * @author Dominic Lerbs
 */
public class SpeakerSessionListener extends SessionListener {

    private final Logger logger = LoggerFactory.getLogger(SpeakerSessionListener.class);
    private final List<SpeakerConnectionListener> listeners = new ArrayList<>();

    private final Speaker speaker;

    public SpeakerSessionListener(Speaker speaker) {
        this.speaker = speaker;
    }

    public void addConnectionListener(SpeakerConnectionListener listener) {
        listeners.add(listener);
    }

    public void removeConnectionListener(SpeakerConnectionListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void sessionLost(int sessionId, int reason) {
        logger.info("Session lost for speaker " + speaker + " with reason " + reason);

        for (SpeakerConnectionListener listener : listeners) {
            listener.onConnectionLost(speaker, reason);
        }
        super.sessionLost(sessionId, reason);
    }

}
