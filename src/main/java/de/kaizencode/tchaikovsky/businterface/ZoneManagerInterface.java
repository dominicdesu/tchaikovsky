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


import java.util.Map;

import org.alljoyn.bus.BusException;
import org.alljoyn.bus.annotation.BusInterface;
import org.alljoyn.bus.annotation.BusMethod;
import org.alljoyn.bus.annotation.BusProperty;
import org.alljoyn.bus.annotation.BusSignal;

import de.kaizencode.tchaikovsky.speaker.remote.RemoteZoneItem;

/**
 * {@link BusInterface} for <code>net.allplay.ZoneManager</code> interface.
 * 
 * @author Yannic Wilkening
 */
@BusInterface(name = "net.allplay.ZoneManager")
public interface ZoneManagerInterface {

    @BusProperty
    public boolean getEnabled() throws BusException;
    
    @BusProperty
    public short getVersion() throws BusException;

    @BusMethod(name = "CreateZone", signature = "as", replySignature = "sia{si}")
    public RemoteZoneItem createZone(String[] speakers) throws BusException;
    
    @BusSignal(name = "OnZoneChanged")
    public void onZoneChanged(String zoneId, int timestamp, Map<String,Integer> slaves) throws BusException;
    
}