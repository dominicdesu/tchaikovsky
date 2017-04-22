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
package de.kaizencode.tchaikovsky.speaker;

import java.util.List;

import de.kaizencode.tchaikovsky.exception.SpeakerException;

/**
 * Input of a speaker. This might not be available for all speakers.
 * 
 * @author Dominic Lerbs
 */
public interface Input {

    /**
     * @return List of all possible inputs
     * @throws SpeakerException
     *             Exception if input list could not be retrieved
     */
    List<String> getInputList() throws SpeakerException;

    /**
     * @return Currently active input
     * @throws SpeakerException
     *             Exception if active input could not be retrieved
     */
    String getActiveInput() throws SpeakerException;

    /**
     * @param input
     *            The input to activate
     * @throws SpeakerException
     *             Exception if input could not be changed
     */
    void setInput(String input) throws SpeakerException;

    short getVersion() throws SpeakerException;

}