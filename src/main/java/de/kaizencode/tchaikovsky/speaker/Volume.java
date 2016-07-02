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

import de.kaizencode.tchaikovsky.exception.SpeakerException;

/**
 * Volume control and information of a speaker.
 * 
 * @author Dominic Lerbs
 */
public interface Volume {

    /**
     * @return Current volume.
     * @throws SpeakerException
     *             if the volume level could not be retrieved
     */
    int getVolume() throws SpeakerException;

    /**
     * Sets the volume of the speaker. See {@link VolumeRange} for allowed values.
     * 
     * @param volume
     *            The new (absolute) volume of the speaker
     * @throws SpeakerException
     *             if the volume could not be changed
     */
    void setVolume(int volume) throws SpeakerException;

    /**
     * @return The {@link VolumeRange} of the speaker
     * @throws SpeakerException
     *             if the {@link VolumeRange} could not be retrieved
     */
    VolumeRange getVolumeRange() throws SpeakerException;

    /**
     * @return Flag if the speaker is currently set to mute
     * @throws SpeakerException
     *             if the mute state could not be retrieved
     */
    boolean isMute() throws SpeakerException;

    /**
     * Sets the mute flag of the speaker.
     * 
     * @param mute
     *            Flag to set the speaker to mute or revoke mute
     * @throws SpeakerException
     *             if the mute state could not be changed
     */
    void mute(boolean mute) throws SpeakerException;

    /**
     * @return Flag if the volume control of the speaker is currently enabled. Might be false if the speaker is part of
     *         a zone and not the master of this zone.
     * @throws SpeakerException
     *             if the enabled state could not be retrieved
     */
    public boolean isControlEnabled() throws SpeakerException;

    /**
     * Adjusts the volume by the given delta increment.
     * 
     * @param delta
     *            Increment delta to change the volume by
     * @throws SpeakerException
     *             if the volume could not be adjusted
     */
    public void adjustVolume(int delta) throws SpeakerException;

    /**
     * Adjusts the volume by the given percentage amount. Note that the percentage is relative to the remaining range to
     * the max/min. So for range [0-100] and current volume [60], incrementing by 50% will set the volume to [80].
     * Incrementing it again by 50% will set it to [90].
     * 
     * @param percent
     *            The percentage to adjust the volume by
     * @throws SpeakerException
     *             if the volume could not be adjusted
     */
    public void adjustVolumePercent(double percent) throws SpeakerException;

}
