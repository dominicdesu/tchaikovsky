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

import de.kaizencode.tchaikovsky.exception.SpeakerException;

/**
 * Group control of the speaker.
 * 
 * @author Yannic Wilkening
 */
public interface ZoneManager {
    
    /**
     * @return Enable state of {@link ZoneManager}.
     * @throws SpeakerException
     *             if the enable state could not be retrieved
     */    
    public boolean getEnabled() throws SpeakerException;
    
    /**
     * @return Version of {@link ZoneManager}.
     * @throws SpeakerException
     *             if the version could not be retrieved
     */
    public short getVersion() throws SpeakerException;

    /**
     * @return The {@link ZoneItem} of the speaker.
     * @param speakers
     *          Array of {@link Speaker} to add to the group
     * @throws SpeakerException
     *             if the group could not be created
     */
    public ZoneItem createZone(List<Speaker> speakerItems) throws SpeakerException;
       
    /**
     * {@link ZoneManager} release currently grouped slaves of speaker.
     * 
     * @throws SpeakerException
     *             if the {@link ZoneManager} could not release the slaves
     */
    public void releaseZone() throws SpeakerException;

}
