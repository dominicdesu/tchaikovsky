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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.alljoyn.bus.BusException;
import org.alljoyn.bus.Variant;

import de.kaizencode.tchaikovsky.exception.SpeakerException;

/**
 * Converter for AllJoyn {@link Variant}s to Java objects.
 * 
 * @author Dominic Lerbs
 */
public class VariantConverter {

    private static final String SIGNATURE_STRING = "s";
    private static final String SIGNATURE_INTEGER = "i";
    private static final String SIGNATURE_LONG = "x";

    /**
     * Converts a single {@link Variant} to a Java object.
     * 
     * @param variant
     *            The {@link Variant} to be converted
     * @return The converted Java object
     * @throws SpeakerException
     *             if an error occurred while converting the {@link Variant} to an object
     */
    public static Object convert(Variant variant) throws SpeakerException {
        try {
            if (SIGNATURE_STRING.equals(variant.getSignature())) {
                return variant.getObject(String.class);
            } else if (SIGNATURE_INTEGER.equals(variant.getSignature())) {
                return variant.getObject(Integer.class);
            } else if (SIGNATURE_LONG.equals(variant.getSignature())) {
                return variant.getObject(Long.class);
            } else {
                throw new SpeakerException("Unsupported Variant signature " + variant.getSignature());
            }
        } catch (BusException e) {
            throw new SpeakerException("Error while trying to resolve Variant " + e.getMessage(), e);
        }
    }

    /**
     * Converts a map containing {@link Variant}s as the value to a map containing Java objects.
     * 
     * @param variantMap
     *            The map to be converted
     * @return A {@link Map} containing Objects as the value
     * @throws SpeakerException
     *             if an error occurred while converting the {@link Variant} to an object
     */
    public static Map<String, Object> convertToMap(Map<String, Variant> variantMap) throws SpeakerException {
        Map<String, Object> map = new HashMap<>();
        for (Entry<String, Variant> entry : variantMap.entrySet()) {
            map.put(entry.getKey(), convert(entry.getValue()));
        }
        return map;
    }
}
