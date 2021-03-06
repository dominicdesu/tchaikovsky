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
package de.kaizencode.tchaikovsky.exception;

import org.alljoyn.bus.Status;

/**
 * Exception in case the communication with the speaker fails.
 * 
 * @author Dominic Lerbs
 */
public class SpeakerException extends AllPlayException {

    private static final long serialVersionUID = 1L;

    public SpeakerException(String message) {
        super(message);
    }

    public SpeakerException(String message, Status status) {
        super(message, status);
    }

    public SpeakerException(String message, Throwable e) {
        super(message, e);
    }

}
