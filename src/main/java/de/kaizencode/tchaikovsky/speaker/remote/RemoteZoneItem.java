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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.alljoyn.bus.annotation.Position;
import org.alljoyn.bus.annotation.Signature;

import de.kaizencode.tchaikovsky.AllPlay;
import de.kaizencode.tchaikovsky.speaker.ZoneItem;

public class RemoteZoneItem implements ZoneItem {

    @Position(0)
    @Signature("s")
    public String zoneId;

    @Position(1)
    @Signature("i")
    public int zoneTimestamp;

    @Position(2)
    @Signature("a{si}")
    public Map<String, Integer> slaves;

    @Override
    public String getZoneId() {
        return zoneId;
    }

    @Override
    public int getZoneTimestamp() {
        return zoneTimestamp;
    }

    @Override
    public Map<String, Integer> getSlaves() {
        return getSlaveMapWithoutPrefix();
    }

    private Map<String, Integer> getSlaveMapWithoutPrefix() {
        Map<String, Integer> slavesMap = new HashMap<>();
        for (Entry<String, Integer> entry : slaves.entrySet()) {
            String key = entry.getKey().replaceFirst(AllPlay.WELL_KNOWN_NAME_PREFIX, "");
            slavesMap.put(key, entry.getValue());
        }
        return slavesMap;
    }
}
