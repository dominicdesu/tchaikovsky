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

import org.alljoyn.bus.annotation.Position;
import org.alljoyn.bus.annotation.Signature;

import de.kaizencode.tchaikovsky.speaker.VolumeRange;

public class RemoteVolumeRange implements VolumeRange {

    @Position(0)
    @Signature("n")
    public short low;

    @Position(1)
    @Signature("n")
    public short high;

    @Position(2)
    @Signature("n")
    public short increment;

    @Override
    public short getMin() {
        return low;
    }

    @Override
    public short getMax() {
        return high;
    }

    @Override
    public short getIncrement() {
        return increment;
    }

}
