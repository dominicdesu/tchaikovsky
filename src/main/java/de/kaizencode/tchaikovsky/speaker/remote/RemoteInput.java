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
package de.kaizencode.tchaikovsky.speaker.remote;

import java.util.Arrays;
import java.util.List;

import org.alljoyn.bus.BusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.kaizencode.tchaikovsky.businterface.InputSelectorInterface;
import de.kaizencode.tchaikovsky.exception.SpeakerException;
import de.kaizencode.tchaikovsky.speaker.Input;

/**
 * Concrete implementation of {@link Input}.
 * 
 * @author Dominic Lerbs
 */
public class RemoteInput implements Input {

    private final Logger logger = LoggerFactory.getLogger(RemoteInput.class);
    private final InputSelectorInterface inputSelectorInterface;

    public RemoteInput(InputSelectorInterface inputSelectorInterface) {
        this.inputSelectorInterface = inputSelectorInterface;
    }

    @Override
    public List<String> getInputList() throws SpeakerException {
        try {
            return Arrays.asList(inputSelectorInterface.getInputList());
        } catch (BusException e) {
            throw new SpeakerException("Unable to receive input list", e);
        }
    }

    @Override
    public String getActiveInput() throws SpeakerException {
        try {
            return inputSelectorInterface.getActiveInput();
        } catch (BusException e) {
            throw new SpeakerException("Unable to receive active input", e);
        }
    }

    @Override
    public void setInput(String input) throws SpeakerException {
        logger.debug("Setting speaker to input " + input);
        try {
            inputSelectorInterface.selectInput(input);
        } catch (BusException e) {
            throw new SpeakerException("Unable to set input to " + input, e);
        }
    }

    @Override
    public short getVersion() throws SpeakerException {
        try {
            return inputSelectorInterface.getVersion();
        } catch (BusException e) {
            throw new SpeakerException("Unable to receive version", e);
        }
    }

}
