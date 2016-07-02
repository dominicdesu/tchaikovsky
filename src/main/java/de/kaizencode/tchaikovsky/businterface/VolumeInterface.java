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

import de.kaizencode.tchaikovsky.speaker.remote.RemoteVolumeRange;

/**
 * {@link BusInterface} for <code>org.alljoyn.Control.Volume</code> interface.
 * 
 * @author Dominic Lerbs
 */
@BusInterface(name = "org.alljoyn.Control.Volume")
public interface VolumeInterface {

    @BusProperty
    public short getVolume() throws BusException;

    @BusProperty
    public void setVolume(short volume) throws BusException;

    @BusProperty
    public RemoteVolumeRange getVolumeRange() throws BusException;

    @BusProperty
    public boolean getMute() throws BusException;

    @BusProperty
    public void setMute(boolean mute) throws BusException;

    @BusProperty
    public boolean getEnabled() throws BusException;

    @BusMethod(name = "AdjustVolume")
    public void adjustVolume(short incrementDelta) throws BusException;

    @BusMethod(name = "AdjustVolumePercent")
    public void adjustVolumePercent(double percent) throws BusException;

    @BusSignal(name = "VolumeChanged")
    public void onVolumeChanged(short volume) throws BusException;

    @BusSignal(name = "MuteChanged")
    public void onMuteChanged(boolean mute) throws BusException;

    @BusSignal(name = "EnabledChanged")
    public void onEnabledChanged(boolean enabled) throws BusException;
}