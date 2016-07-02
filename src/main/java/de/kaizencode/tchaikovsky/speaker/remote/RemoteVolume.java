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

import org.alljoyn.bus.BusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.kaizencode.tchaikovsky.businterface.VolumeInterface;
import de.kaizencode.tchaikovsky.exception.SpeakerException;
import de.kaizencode.tchaikovsky.speaker.Volume;
import de.kaizencode.tchaikovsky.speaker.VolumeRange;

public class RemoteVolume implements Volume {

    private final Logger logger = LoggerFactory.getLogger(RemoteVolume.class);
    private final VolumeInterface volumeInterface;

    public RemoteVolume(VolumeInterface volumeInterface) {
        this.volumeInterface = volumeInterface;
    }

    @Override
    public int getVolume() throws SpeakerException {
        try {
            return volumeInterface.getVolume();
        } catch (BusException e) {
            throw new SpeakerException("Unable to receive volume", e);
        }
    }

    @Override
    public void setVolume(int volume) throws SpeakerException {
        logger.debug("Setting speaker to volume " + volume);
        try {
            volumeInterface.setVolume((short) volume);
        } catch (BusException e) {
            throw new SpeakerException("Unable to set volume to " + volume, e);
        }
    }

    @Override
    public VolumeRange getVolumeRange() throws SpeakerException {
        try {
            return volumeInterface.getVolumeRange();
        } catch (BusException e) {
            throw new SpeakerException("Unable to receive volume range", e);
        }
    }

    @Override
    public boolean isMute() throws SpeakerException {
        try {
            return volumeInterface.getMute();
        } catch (BusException e) {
            throw new SpeakerException("Unable to receive mute", e);
        }
    }

    @Override
    public void mute(boolean mute) throws SpeakerException {
        logger.debug("Setting speaker to mute " + mute);
        try {
            volumeInterface.setMute(mute);
        } catch (BusException e) {
            throw new SpeakerException("Unable to set mute to " + mute, e);
        }
    }

    @Override
    public boolean isControlEnabled() throws SpeakerException {
        try {
            return volumeInterface.getEnabled();
        } catch (BusException e) {
            throw new SpeakerException("Unable to receive if control is enabled", e);
        }
    }

    @Override
    public void adjustVolume(int delta) throws SpeakerException {
        try {
            volumeInterface.adjustVolume((short) delta);
        } catch (BusException e) {
            throw new SpeakerException("Unable to adjust volume by " + delta + " increments", e);
        }
    }

    @Override
    public void adjustVolumePercent(double percent) throws SpeakerException {
        try {
            volumeInterface.adjustVolumePercent(percent);
        } catch (BusException e) {
            throw new SpeakerException("Unable to adjust volume by " + percent + "%", e);
        }
    }

}
