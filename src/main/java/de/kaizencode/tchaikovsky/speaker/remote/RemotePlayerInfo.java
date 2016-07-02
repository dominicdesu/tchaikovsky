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

import java.util.Arrays;
import java.util.List;

import org.alljoyn.bus.annotation.Position;
import org.alljoyn.bus.annotation.Signature;

import de.kaizencode.tchaikovsky.speaker.PlayerInfo;

public class RemotePlayerInfo implements PlayerInfo {

    @Position(0)
    @Signature("s")
    public String displayName;

    @Position(1)
    @Signature("as")
    public String[] capabilities;

    @Position(2)
    @Signature("i")
    public int maxVolume;

    @Position(3)
    @Signature("r")
    public RemoteZoneInfo zoneInfo;

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public List<String> getCapabilities() {
        return Arrays.asList(capabilities);
    }

    @Override
    public int getMaxVolume() {
        return maxVolume;
    }

    // @Override
    // public ZoneInfo getZoneInfo() {
    // return zoneInfo;
    // }

    @Override
    public String toString() {
        return displayName;
    }

}
