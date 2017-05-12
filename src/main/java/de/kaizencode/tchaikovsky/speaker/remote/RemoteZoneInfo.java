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
import org.alljoyn.bus.Variant;
import org.alljoyn.bus.annotation.Position;
import org.alljoyn.bus.annotation.Signature;

import de.kaizencode.tchaikovsky.AllPlay;
import de.kaizencode.tchaikovsky.speaker.ZoneInfo;

public class RemoteZoneInfo implements ZoneInfo {

    @Position(0)
    @Signature("s")
    public String zoneId;

    @Position(1)
    @Signature("i")
    public int zoneTimestamp;

    @Position(2)
    @Signature("v")
    public Variant leadPlayerName;

    @Override
    public String getZoneId() {
        return zoneId;
    }

    @Override
    public int getZoneTimestamp() {
        return zoneTimestamp;
    }

    @Override
    public boolean isLeadPlayer() {
        try {
            if ("s".equals(leadPlayerName.getSignature())) {
                return false;
            }
        } catch (BusException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public String getLeadPlayerID() {
        try {
            if ("s".equals(leadPlayerName.getSignature())) {
                return leadPlayerName.getObject(String.class).replaceAll(AllPlay.WELL_KNOWN_NAME_PREFIX, "");
            }
        } catch (BusException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public String toString() {
        return zoneId + "-" + zoneTimestamp + "-" + leadPlayerName;
    }

}
