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

import de.kaizencode.tchaikovsky.exception.ConnectionException;
import de.kaizencode.tchaikovsky.exception.SpeakerException;
import de.kaizencode.tchaikovsky.listener.SpeakerChangedListener;
import de.kaizencode.tchaikovsky.listener.SpeakerConnectionListener;

/**
 * An AllPlay speaker.
 * 
 * @author Dominic Lerbs
 */
public interface Speaker {

    /**
     * Enum for Loop/Repeat mode of the speaker.
     */
    public enum LoopMode {
        ONE, ALL, NONE;

        public static LoopMode parse(String rawString) throws SpeakerException {
            try {
                return LoopMode.valueOf(rawString);
            } catch (Exception e) {
                throw new SpeakerException("Unknown LoopMode received: " + rawString, e);
            }
        }
    }

    /**
     * Enum for shuffle/random mode of the speaker.
     */
    public enum ShuffleMode {
        LINEAR, SHUFFLE;

        public static ShuffleMode parse(String rawString) throws SpeakerException {
            try {
                return ShuffleMode.valueOf(rawString);
            } catch (Exception e) {
                throw new SpeakerException("Unknown ShuffleMode received: " + rawString, e);
            }
        }
    }

    /**
     * @return Unique ID of the speaker
     */
    String getId();

    /**
     * @return User-defined name of the speaker
     */
    String getName();

    /**
     * @return {@link SpeakerDetails} as reported by the speaker
     */
    SpeakerDetails details();

    /**
     * Establishes a connection with this {@link Speaker}.
     * 
     * @throws ConnectionException
     *             if the connection cannot be established
     */
    void connect() throws ConnectionException;

    /**
     * Disconnect from this {@link Speaker}.
     */
    void disconnect();

    /**
     * @return Flag if a connection to the speaker is established or not
     */
    boolean isConnected();

    /**
     * Pings the speaker.
     * 
     * @param timeoutInMs
     *            The timeout after which the ping fails
     * @return True if a response was received for the ping, else false
     */
    boolean ping(int timeoutInMs);

    /**
     * Set the timeout for a session. Default is 120 seconds. AllJoyn defines 40 seconds as the minimum.
     * 
     * @param timeoutInSec
     *            Timeout for a session in seconds. If timeout occurs,
     *            {@link SpeakerConnectionListener#onConnectionLost(String, int)} is called
     */
    void setSessionTimeout(int timeoutInSec);

    /**
     * @return Current {@link PlayState} of the speaker
     * @throws SpeakerException
     *             if {@link PlayState} cannot be retrieved
     */
    PlayState getPlayState() throws SpeakerException;

    /**
     * @return Current {@link LoopMode} of the speaker
     * @throws SpeakerException
     *             if {@link LoopMode} cannot be retrieved
     */
    LoopMode getLoopMode() throws SpeakerException;

    /**
     * @param loopMode
     *            New {@link LoopMode} to be set on the speaker
     * @throws SpeakerException
     *             if the {@link LoopMode} could not be set
     */
    void setLoopMode(LoopMode loopMode) throws SpeakerException;

    /**
     * @return Current {@link ShuffleMode} of the speaker
     * @throws SpeakerException
     *             if {@link ShuffleMode} cannot be retrieved
     */
    ShuffleMode getShuffleMode() throws SpeakerException;

    /**
     * @param shuffleMode
     *            New {@link ShuffleMode} to be set on the speaker
     * @throws SpeakerException
     *             if the {@link ShuffleMode} could not be set
     */
    void setShuffleMode(ShuffleMode shuffleMode) throws SpeakerException;

    /**
     * @return {@link PlayerInfo} of the speaker
     * @throws SpeakerException
     *             if the {@link PlayerInfo} cannot be retrieved
     */
    PlayerInfo getPlayerInfo() throws SpeakerException;

    /**
     * Plays the item at the given index in the playlist.
     * 
     * @param itemIndex
     *            Index of the item in the playlist to play
     * @param offsetInMs
     *            Start offset in milliseconds
     * @param paused
     *            If the item should be set to paused initially
     * @throws SpeakerException
     *             if the item cannot be played
     */
    public void play(int itemIndex, long offsetInMs, boolean paused) throws SpeakerException;

    /**
     * Jumps to the next track in the playlist.
     * 
     * @throws SpeakerException
     *             if the {@link #next()} command failed
     */
    void next() throws SpeakerException;

    /**
     * Jumps to the previous track in the playlist if still at the beginning of the current track, else jumps to the
     * beginning of the current track. To always jump to the previous track, use {@link #forcePrevious()}.
     * 
     * @throws SpeakerException
     *             if the {@link #previous()} command failed
     */
    void previous() throws SpeakerException;

    /**
     * Jumps to the previous track in the playlist. For a more dynamic option, see {@link #previous()}.
     * 
     * @throws SpeakerException
     *             if the {@link #forcePrevious()} command failed
     */
    void forcePrevious() throws SpeakerException;

    /**
     * Pauses the currently playing track.
     * 
     * @throws SpeakerException
     *             if the {@link #pause()} command failed
     */
    void pause() throws SpeakerException;

    /**
     * Resumes a paused track.
     * 
     * @throws SpeakerException
     *             if the {@link #resume()} command failed
     */
    void resume() throws SpeakerException;

    /**
     * Stops the playback.
     * 
     * @throws SpeakerException
     *             if the {@link #stop()} command failed
     */
    void stop() throws SpeakerException;

    /**
     * Sets the position (in milliseconds) from where to play the current track.
     * 
     * @param offsetInMs
     *            The position offset in milliseconds
     * @throws SpeakerException
     *             if the position offset could not be set
     */
    public void setPosition(long offsetInMs) throws SpeakerException;

    /**
     * Update the {@link Playlist} of the speaker.
     * 
     * @param playlistItems
     *            A list of item to add to the {@link Playlist}
     * @param index
     *            New index of the current item
     * @param controllerType
     *            Controller type (user-defined)
     * @param playlistUserData
     *            Custom user data
     * @throws SpeakerException
     *             if the {@link Playlist} could not be updated
     */
    public void updatePlaylist(List<PlaylistItem> playlistItems, int index, String controllerType,
            String playlistUserData) throws SpeakerException;

    /**
     * @return The current {@link Playlist} of the speaker
     * @throws SpeakerException
     *             if the {@link Playlist} could not be retrieved
     */
    Playlist getPlaylist() throws SpeakerException;

    /**
     * Plays the item at the given URL.
     * 
     * @param url
     *            The URL to play
     * @throws SpeakerException
     *             If the item cannot be played
     */
    void playItem(String url) throws SpeakerException;

    /**
     * @return The {@link Volume} of the speaker.
     */
    Volume volume();

    /**
     * @return The {@link ZoneManager} of the speaker.
     */
    ZoneManager zoneManager();

    /**
     * Adds a listener which is notified when the state of the speaker changes.
     * 
     * @param listener
     *            The {@link SpeakerChangedListener} to add
     */
    void addSpeakerChangedListener(SpeakerChangedListener listener);

    /**
     * @param listener
     *            The {@link SpeakerChangedListener} to remove
     */
    void removeSpeakerChangedListener(SpeakerChangedListener listener);

    /**
     * Adds a listener which is notified when the connection state of the speaker changes.
     * 
     * @param listener
     *            The {@link SpeakerConnectionListener} to add
     */
    void addSpeakerConnectionListener(SpeakerConnectionListener listener);

    /**
     * @param listener
     *            The {@link SpeakerConnectionListener} to remove
     */
    void removeSpeakerConnectionListener(SpeakerConnectionListener listener);

    /**
     * Enables concurrent callbacks. This method needs to be called if a request is triggered from within a callback
     * method. For example, if from callback method {@link SpeakerChangedListener#onPlaylistChanged()} a call to
     * {@link Speaker#getPlayState()} should be made, {@link #enableConcurrentCallbacks()} needs to be executed
     * beforehand.
     * 
     * For more details see <a href=
     * "https://allseenalliance.org/docs/api/java/org/alljoyn/bus/BusAttachment.html#enableConcurrentCallbacks()">
     * BusAttachment.html#enableConcurrentCallbacks()</a> .
     */
    void enableConcurrentCallbacks();

}