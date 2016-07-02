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
package de.kaizencode.tchaikovsky.businterface;

import org.alljoyn.bus.BusException;
import org.alljoyn.bus.annotation.BusInterface;
import org.alljoyn.bus.annotation.BusMethod;
import org.alljoyn.bus.annotation.BusProperty;
import org.alljoyn.bus.annotation.BusSignal;

import de.kaizencode.tchaikovsky.speaker.PlaylistItem;
import de.kaizencode.tchaikovsky.speaker.remote.RemotePlayState;
import de.kaizencode.tchaikovsky.speaker.remote.RemotePlayerInfo;
import de.kaizencode.tchaikovsky.speaker.remote.RemotePlaylist;

/**
 * {@link BusInterface} for <code>net.allplay.MediaPlayer</code> interface.
 * 
 * @author Dominic Lerbs
 */
@BusInterface(name = "net.allplay.MediaPlayer")
public interface MediaPlayerInterface {

    @BusProperty
    public RemotePlayState getPlayState() throws BusException;

    @BusProperty
    public String getLoopMode() throws BusException;

    @BusProperty
    public void setLoopMode(String loopMode) throws BusException;

    @BusProperty
    public String getShuffleMode() throws BusException;

    @BusProperty
    public void setShuffleMode(String shuffleMode) throws BusException;

    @BusMethod(name = "GetPlayerInfo", replySignature = "sasi(siv)")
    public RemotePlayerInfo getPlayerInfo() throws BusException;

    @BusMethod(name = "Play")
    public void play(int itemIndex, long offsetInMs, boolean paused) throws BusException;

    @BusMethod(name = "Next")
    public void next() throws BusException;

    @BusMethod(name = "Previous")
    public void previous() throws BusException;

    @BusMethod(name = "ForcedPrevious")
    public void forcePrevious() throws BusException;

    @BusMethod(name = "Pause")
    public void pause() throws BusException;

    @BusMethod(name = "Resume")
    public void resume() throws BusException;

    @BusMethod(name = "Stop")
    public void stop() throws BusException;

    @BusMethod(name = "SetPosition")
    public void setPosition(long offsetInMs) throws BusException;

    @BusMethod(name = "UpdatePlaylist", signature = "a(ssssxsssa{ss}a{sv}v)iss")
    public void updatePlaylist(PlaylistItem[] playlistItems, int index, String controllerType, String playlistUserData)
            throws BusException;

    @BusMethod(name = "GetPlaylist", replySignature = "a(ssssxsssa{ss}a{sv}v)ss")
    public RemotePlaylist getPlaylist() throws BusException;

    @BusSignal(name = "PlaylistChanged")
    public void onPlaylistChanged() throws BusException;

    @BusSignal(name = "PlayStateChanged")
    public void onPlayStateChanged(RemotePlayState playState) throws BusException;

    @BusSignal(name = "LoopModeChanged")
    public void onLoopModeChanged(String loopMode) throws BusException;

    @BusSignal(name = "ShuffleModeChanged")
    public void onShuffleModeChanged(String shuffleMode) throws BusException;

    @BusSignal(name = "OnPlayBackError")
    public void onPlayBackError(int index, String error, String description);
}