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

import de.kaizencode.tchaikovsky.speaker.Speaker;

/**
 * Listener for the {@link Speaker} connection state.
 * 
 * @author Dominic Lerbs
 */
public interface SpeakerConnectionListener {

    /**
     * Called if the coneection to the {@link Speaker} has been lost
     * 
     * @param speaker
     *            The {@link Speaker} to which the connection has been lost
     * @param alljoynReasonCode
     *            The AllJoyn internal reason code for the session disconnect
     */
    void onConnectionLost(Speaker speaker, int alljoynReasonCode);

}
