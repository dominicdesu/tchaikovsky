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
package de.kaizencode.tchaikovsky.bussignal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.alljoyn.bus.BusAttachment;
import org.alljoyn.bus.annotation.BusSignalHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.kaizencode.tchaikovsky.exception.SpeakerException;
import de.kaizencode.tchaikovsky.speaker.Speaker.LoopMode;
import de.kaizencode.tchaikovsky.speaker.Speaker.ShuffleMode;
import de.kaizencode.tchaikovsky.speaker.remote.RemotePlayState;

/**
 * Signal handler for AllPlay interfaces.
 * 
 * @author Dominic
 */
public class MediaPlayerSignalHandler {

    private final Logger logger = LoggerFactory.getLogger(MediaPlayerSignalHandler.class);

    private final Map<String, List<SpeakerChangedListener>> speakerChangedListeners = new HashMap<>();
    private static final String MEDIA_PLAYER_INTERFACE = "de.kaizencode.tchaikovsky.businterface.MediaPlayerInterface";
    private static final String VOLUME_INTERFACE = "de.kaizencode.tchaikovsky.businterface.VolumeInterface";
    private static final String ZONEMANAGER_INTERFACE = "de.kaizencode.tchaikovsky.businterface.ZoneManagerInterface";

    private final BusAttachment busAttachment;

    public MediaPlayerSignalHandler(BusAttachment busAttachment) {
        this.busAttachment = busAttachment;
    }

    /**
     * Add a new {@link SpeakerChangedListener} for the given bus name.
     * 
     * @param busName
     *            Signals on this bus name will be forwarded to the listener
     * @param listener
     *            A {@link SpeakerChangedListener} for a single bus name
     */
    public void addSpeakerChangedListener(String busName, SpeakerChangedListener listener) {
        if (speakerChangedListeners.containsKey(busName)) {
            speakerChangedListeners.get(busName).add(listener);
        } else {
            speakerChangedListeners.put(busName,
                    new ArrayList<SpeakerChangedListener>(Collections.singleton(listener)));
        }
        logger.debug("New SpeakerChangedListener added for bus " + busName);
    }

    /**
     * Remove a {@link SpeakerChangedListener} for the given bus name.
     * 
     * @param busName
     *            The bus name to be removed from
     * @param listener
     *            The {@link SpeakerChangedListener} to be removed
     */
    public void removeSpeakerChangedListener(String busName, SpeakerChangedListener listener) {
        if (speakerChangedListeners.containsKey(busName)) {
            speakerChangedListeners.get(busName).remove(listener);
        }
        logger.debug("SpeakerChangedListener removed from bus " + busName);
    }

    @BusSignalHandler(iface = MEDIA_PLAYER_INTERFACE, signal = "onLoopModeChanged")
    public void onLoopModeChanged(String loopMode) {
        logSignalReceived("LoopModeChanged");
        try {
            LoopMode mode = LoopMode.parse(loopMode);
            for (SpeakerChangedListener listener : findListeners()) {
                listener.onLoopModeChanged(mode);
            }
        } catch (SpeakerException e) {
            logger.error("Unknown loopMode " + loopMode + ", cannot inform listeners");
        }
    }

    @BusSignalHandler(iface = MEDIA_PLAYER_INTERFACE, signal = "onPlaylistChanged")
    public void onPlaylistChanged() {
        logSignalReceived("PlaylistChanged");
        for (SpeakerChangedListener listener : findListeners()) {
            listener.onPlaylistChanged();
        }
    }

    @BusSignalHandler(iface = MEDIA_PLAYER_INTERFACE, signal = "onPlayStateChanged")
    public void onPlayStateChanged(RemotePlayState playState) {
        logSignalReceived("PlayStateChanged");
        for (SpeakerChangedListener listener : findListeners()) {
            listener.onPlayStateChanged(playState);
        }
    }

    @BusSignalHandler(iface = MEDIA_PLAYER_INTERFACE, signal = "onShuffleModeChanged")
    public void onShuffleModeChanged(String shuffleMode) {
        logSignalReceived("ShuffleModeChanged");
        try {
            ShuffleMode mode = ShuffleMode.parse(shuffleMode);
            for (SpeakerChangedListener listener : findListeners()) {
                listener.onShuffleModeChanged(mode);
            }
        } catch (SpeakerException e) {
            logger.error("Unknown shuffleMode " + shuffleMode + ", cannot inform listeners");
        }
    }

    @BusSignalHandler(iface = MEDIA_PLAYER_INTERFACE, signal = "onPlayBackError")
    public void onPlayBackError(int index, String error, String description) {
        logSignalReceived("PlayBackError");
        // TODO: This is not working? How to trigger a playback error?
    }

    @BusSignalHandler(iface = VOLUME_INTERFACE, signal = "onVolumeChanged")
    public void onVolumeChanged(short volume) {
        logSignalReceived("VolumeChanged");
        for (SpeakerChangedListener listener : findListeners()) {
            listener.onVolumeChanged(volume);
        }
    }

    @BusSignalHandler(iface = VOLUME_INTERFACE, signal = "onMuteChanged")
    public void onMuteChanged(boolean mute) {
        logSignalReceived("MuteChanged");
        for (SpeakerChangedListener listener : findListeners()) {
            listener.onMuteChanged(mute);
        }
    }

    @BusSignalHandler(iface = VOLUME_INTERFACE, signal = "onEnabledChanged")
    public void onVolumeControlChanged(boolean enabled) {
        logSignalReceived("Volume control changed, enabled = " + enabled);
        for (SpeakerChangedListener listener : findListeners()) {
            listener.onVolumeControlChanged(enabled);
        }
    }
   
    @BusSignalHandler(iface = ZONEMANAGER_INTERFACE, signal = "onZoneChanged")
    public void onZoneChanged(String zoneId, int timestamp, Map<String,Integer> slaves) {
        logSignalReceived("Zone changed");
        for (SpeakerChangedListener listener : findListeners()) {
            listener.onZoneChanged(zoneId, timestamp, slaves);
        }
    }
    
    private List<SpeakerChangedListener> findListeners() {
        String busName = busAttachment.getMessageContext().sender;
        if (speakerChangedListeners.containsKey(busName)) {
            return speakerChangedListeners.get(busName);
        }
        return new ArrayList<>();
    }

    private void logSignalReceived(String signalName) {
        if (logger.isDebugEnabled()) {
            logger.debug(busAttachment.getMessageContext().sender + ": Bus signal received [" + signalName + "]");
        }
    }

}
