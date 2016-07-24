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

import java.util.List;

import org.alljoyn.bus.BusException;
import org.alljoyn.bus.ProxyBusObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.kaizencode.tchaikovsky.bus.SpeakerBusHandler;
import de.kaizencode.tchaikovsky.bus.SpeakerConnectionListener;
import de.kaizencode.tchaikovsky.bus.SpeakerSessionListener;
import de.kaizencode.tchaikovsky.businterface.MCUInterface;
import de.kaizencode.tchaikovsky.businterface.MediaPlayerInterface;
import de.kaizencode.tchaikovsky.businterface.VolumeInterface;
import de.kaizencode.tchaikovsky.bussignal.SpeakerChangedListener;
import de.kaizencode.tchaikovsky.exception.ConnectionException;
import de.kaizencode.tchaikovsky.exception.SpeakerException;
import de.kaizencode.tchaikovsky.speaker.PlaylistItem;
import de.kaizencode.tchaikovsky.speaker.Speaker;
import de.kaizencode.tchaikovsky.speaker.SpeakerDetails;
import de.kaizencode.tchaikovsky.speaker.Volume;

public class RemoteSpeaker implements Speaker, SpeakerConnectionListener {

    private final Logger logger = LoggerFactory.getLogger(RemoteSpeaker.class);

    private final SpeakerBusHandler busHandler;
    private int sessionTimeoutInSec = 40;
    private boolean isConnected = false;
    private final SpeakerSessionListener sessionListener;

    private ProxyBusObject allPlayObject;
    private MediaPlayerInterface mediaPlayerInterface;
    private MCUInterface mcuInterface;

    private final SpeakerDetails details;
    private Volume volume;

    public RemoteSpeaker(SpeakerBusHandler bus, SpeakerDetails details) {
        this.busHandler = bus;
        sessionListener = new SpeakerSessionListener(this);
        sessionListener.addConnectionListener(this);
        busHandler.setSessionListener(sessionListener);
        this.details = details;
    }

    @Override
    public String getName() {
        return details.getDeviceName();
    }

    @Override
    public String getId() {
        return details.getDeviceId();
    }

    @Override
    public SpeakerDetails details() {
        return details;
    }

    @Override
    public void connect() throws ConnectionException {

        allPlayObject = busHandler.connect();
        busHandler.setSessionTimeout(sessionTimeoutInSec);
        isConnected = true;

        mediaPlayerInterface = allPlayObject.getInterface(MediaPlayerInterface.class);
        volume = new RemoteVolume(allPlayObject.getInterface(VolumeInterface.class));
        mcuInterface = allPlayObject.getInterface(MCUInterface.class);

        // For an unknown reason, it is necessary to perform at least one method call
        // after registering the signal handler, else the signal handler will not receive any updates
        try {
            getPlaylist();
        } catch (SpeakerException e) {
            logger.warn("Connection to speaker established but unable to get playlist. "
                    + "Speaker update receiving might fail.");
        }

    }

    @Override
    public void disconnect() {
        busHandler.disconnect();
    }

    @Override
    public boolean isConnected() {
        return isConnected;
    }

    @Override
    public void onConnectionLost(Speaker speaker, int alljoynReasonCode) {
        isConnected = false;
    }

    @Override
    public boolean ping(int timeoutInMs) {
        return busHandler.ping(timeoutInMs);
    }

    @Override
    public void setSessionTimeout(int timeoutInSec) {
        sessionTimeoutInSec = timeoutInSec;
        busHandler.setSessionTimeout(sessionTimeoutInSec);
    }

    @Override
    public Volume volume() {
        return volume;
    }

    @Override
    public RemotePlayerInfo getPlayerInfo() throws SpeakerException {
        try {
            return mediaPlayerInterface.getPlayerInfo();
        } catch (BusException e) {
            throw new SpeakerException("Unable to retrieve player info", e);
        }
    }

    @Override
    public void play(int itemIndex, long offsetInMs, boolean paused) throws SpeakerException {
        try {
            mediaPlayerInterface.play(itemIndex, offsetInMs, paused);
        } catch (BusException e) {
            throw new SpeakerException("Unable to play item", e);
        }
    }

    @Override
    public RemotePlaylist getPlaylist() throws SpeakerException {
        try {
            return mediaPlayerInterface.getPlaylist();
        } catch (BusException e) {
            throw new SpeakerException("Unable to retrieve playlist", e);
        }
    }

    @Override
    public RemotePlayState getPlayState() throws SpeakerException {
        try {
            return mediaPlayerInterface.getPlayState();
        } catch (BusException e) {
            throw new SpeakerException("Unable to retrieve play state", e);
        }
    }

    @Override
    public LoopMode getLoopMode() throws SpeakerException {
        try {
            return LoopMode.parse(mediaPlayerInterface.getLoopMode());
        } catch (BusException e) {
            throw new SpeakerException("Unable to retrieve loop mode", e);
        }
    }

    @Override
    public ShuffleMode getShuffleMode() throws SpeakerException {
        try {
            return ShuffleMode.parse(mediaPlayerInterface.getShuffleMode());
        } catch (BusException e) {
            throw new SpeakerException("Unable to retrieve shuffle mode", e);
        }
    }

    @Override
    public void setLoopMode(LoopMode loopMode) throws SpeakerException {
        try {
            mediaPlayerInterface.setLoopMode(loopMode.toString());
        } catch (BusException e) {
            throw new SpeakerException("Unable to set loop mode to " + loopMode, e);
        }
    }

    @Override
    public void setShuffleMode(ShuffleMode shuffleMode) throws SpeakerException {
        try {
            mediaPlayerInterface.setShuffleMode(shuffleMode.toString());
        } catch (BusException e) {
            throw new SpeakerException("Unable to set shuffle mode to " + shuffleMode, e);
        }
    }

    @Override
    public void next() throws SpeakerException {
        logger.debug("Setting speaker to next");
        try {
            mediaPlayerInterface.next();
        } catch (BusException e) {
            throw new SpeakerException("Unable to play next", e);
        }
    }

    @Override
    public void previous() throws SpeakerException {
        logger.debug("Setting speaker to previous");
        try {
            mediaPlayerInterface.previous();
        } catch (BusException e) {
            throw new SpeakerException("Unable to play previous", e);
        }
    }

    @Override
    public void forcePrevious() throws SpeakerException {
        logger.debug("Setting speaker to force previous");
        try {
            mediaPlayerInterface.forcePrevious();
        } catch (BusException e) {
            throw new SpeakerException("Unable to force previous", e);
        }
    }

    @Override
    public void pause() throws SpeakerException {
        logger.debug("Setting speaker to pause");
        try {
            mediaPlayerInterface.pause();
        } catch (BusException e) {
            throw new SpeakerException("Unable to pause", e);
        }
    }

    @Override
    public void resume() throws SpeakerException {
        logger.debug("Setting speaker to resume");
        try {
            mediaPlayerInterface.resume();
        } catch (BusException e) {
            throw new SpeakerException("Unable to resume", e);
        }
    }

    @Override
    public void stop() throws SpeakerException {
        logger.debug("Setting speaker to stop");
        try {
            mediaPlayerInterface.stop();
        } catch (BusException e) {
            throw new SpeakerException("Unable to stop", e);
        }
    }

    @Override
    public void setPosition(long offsetInMs) throws SpeakerException {
        logger.debug("Setting speaker to positon offset " + offsetInMs);
        try {
            mediaPlayerInterface.setPosition(offsetInMs);
        } catch (BusException e) {
            throw new SpeakerException("Unable to set position to " + offsetInMs, e);
        }
    }

    @Override
    public void updatePlaylist(List<PlaylistItem> playlistItems, int index, String controllerType,
            String playlistUserData) throws SpeakerException {
        logger.debug("Setting new playlist on spekaer");
        try {
            mediaPlayerInterface.updatePlaylist(playlistItems.toArray(new PlaylistItem[playlistItems.size()]), index,
                    controllerType, playlistUserData);
        } catch (BusException e) {
            throw new SpeakerException("Unable to update playlist", e);
        }
    }

    @Override
    public void playItem(String url) throws SpeakerException {
        try {
            mcuInterface.playItem(url, "", "", "", 0, "", "");
        } catch (BusException e) {
            throw new SpeakerException("Unable to play item", e);
        }
    }

    @Override
    public void enableConcurrentCallbacks() {
        busHandler.enableConcurrentCallbacks();
    }

    @Override
    public void addSpeakerChangedListener(SpeakerChangedListener listener) {
        busHandler.addSpeakerChangedListener(listener);
    }

    @Override
    public void removeSpeakerChangedListener(SpeakerChangedListener listener) {
        busHandler.removeSpeakerChangedListener(listener);
    }

    @Override
    public void addSpeakerConnectionListener(SpeakerConnectionListener listener) {
        sessionListener.addConnectionListener(listener);
    }

    @Override
    public void removeSpeakerConnectionListener(SpeakerConnectionListener listener) {
        sessionListener.removeConnectionListener(listener);
    }

    @Override
    public String toString() {
        return details.getDeviceName() + " (" + details.getDeviceId() + ")";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        RemoteSpeaker other = (RemoteSpeaker) obj;
        if (getId() == null || other.getId() == null)
            return false;
        return getId().equals(other.getId());
    }

}
