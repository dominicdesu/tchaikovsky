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

import de.kaizencode.tchaikovsky.speaker.PlayState;
import de.kaizencode.tchaikovsky.speaker.Speaker;
import de.kaizencode.tchaikovsky.speaker.Speaker.LoopMode;
import de.kaizencode.tchaikovsky.speaker.Speaker.ShuffleMode;

/**
 * Listener for various state changes of a {@link Speaker}.
 *
 * @author Dominic Lerbs
 */
public interface SpeakerChangedListener {

    /**
     * @param loopMode
     *            The new {@link LoopMode} of the speaker
     */
    public void onLoopModeChanged(LoopMode loopMode);

    /**
     * Callback when the playlist has changed.
     */
    public void onPlaylistChanged();

    /**
     * @param playState
     *            The new {@link PlayState} of the speaker
     */
    public void onPlayStateChanged(PlayState playState);

    /**
     * @param shuffleMode
     *            The new {@link ShuffleMode} of the speaker
     */
    public void onShuffleModeChanged(ShuffleMode shuffleMode);

    /**
     * @param volume
     *            The new volume of the speaker
     */
    public void onVolumeChanged(int volume);

    /**
     * @param mute
     *            True if the speaker has been muted, false if it has been unmuted
     */
    public void onMuteChanged(boolean mute);

    /**
     * @param enabled
     *            True if the volume control has been enabled, false if it has been disabled
     */
    public void onVolumeControlChanged(boolean enabled);

}
