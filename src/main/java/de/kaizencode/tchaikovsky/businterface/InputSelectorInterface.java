/**
 * Tchaikovsky - A Java library for controlling AllPlay-compatible devices.
 * Copyright (c) 2017 Dominic Lerbs
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

/**
 * {@link BusInterface} for <code>net.allplay.InputSelector</code> interface.
 * 
 * @author Dominic Lerbs
 */
@BusInterface(name = "net.allplay.InputSelector")
public interface InputSelectorInterface {

    @BusProperty
    public String getActiveInput() throws BusException;

    @BusProperty
    public String[] getInputList() throws BusException;

    @BusProperty
    public short getVersion() throws BusException;

    @BusMethod(name = "SelectInput", signature = "s")
    public void selectInput(String input) throws BusException;

    @BusSignal(name = "InputChanged")
    public void onInputChanged(String input) throws BusException;

}