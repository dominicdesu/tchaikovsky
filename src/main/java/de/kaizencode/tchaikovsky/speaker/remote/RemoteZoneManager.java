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

import java.util.ArrayList;
import java.util.List;

import org.alljoyn.bus.BusException;

import de.kaizencode.tchaikovsky.AllPlay;
import de.kaizencode.tchaikovsky.businterface.ZoneManagerInterface;
import de.kaizencode.tchaikovsky.exception.SpeakerException;
import de.kaizencode.tchaikovsky.speaker.ZoneItem;
import de.kaizencode.tchaikovsky.speaker.ZoneManager;

public class RemoteZoneManager implements ZoneManager {

    private final ZoneManagerInterface zoneManagerInterface;

    public RemoteZoneManager(ZoneManagerInterface zoneManagerInterface) {
        this.zoneManagerInterface = zoneManagerInterface;
    }

    @Override
    public boolean getEnabled() throws SpeakerException {
        try {
            return zoneManagerInterface.getEnabled();
        } catch (BusException e) {
            throw new SpeakerException("Unable to receive enabled", e);
        }
    }

    @Override
    public short getVersion() throws SpeakerException {
        try {
            return zoneManagerInterface.getVersion();
        } catch (BusException e) {
            throw new SpeakerException("Unable to receive version", e);
        }
    }

    @Override
    public ZoneItem createZone(List<String> deviceIds) throws SpeakerException {
        try {
            String[] speakers = new String[deviceIds.size()];
            for (int i = 0; i < deviceIds.size(); i++) {
                speakers[i] = AllPlay.WELL_KNOWN_NAME_PREFIX + deviceIds.get(i);
            }
            return zoneManagerInterface.createZone(speakers);
        } catch (BusException e) {
            throw new SpeakerException("Unable to create zone", e);
        }
    }

    @Override
    public void releaseZone() throws SpeakerException {
        createZone(new ArrayList<String>());
    }

}
