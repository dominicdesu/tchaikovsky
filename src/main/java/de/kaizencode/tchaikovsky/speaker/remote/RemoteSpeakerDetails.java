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

import java.util.Map;

import org.alljoyn.bus.BusException;
import org.alljoyn.bus.Variant;

import de.kaizencode.tchaikovsky.speaker.SpeakerDetails;

public class RemoteSpeakerDetails implements SpeakerDetails {

    private byte[] appId;
    private String defaultLanguage;
    private String deviceName;
    private String deviceId;
    private String appName;
    private String manufacturer;
    private String modelNumber;
    private String description;
    private String softwareVersion;
    private String allJoynSdkVersion;

    public RemoteSpeakerDetails(Map<String, Variant> aboutData) throws BusException {
        parseAboutData(aboutData);
    }

    private void parseAboutData(Map<String, Variant> aboutData) throws BusException {
        appId = aboutData.get("AppId").getObject(byte[].class);
        defaultLanguage = getAsString(aboutData, "DefaultLanguage");
        deviceName = getAsString(aboutData, "DeviceName");
        deviceId = getAsString(aboutData, "DeviceId");
        appName = getAsString(aboutData, "AppName");
        manufacturer = getAsString(aboutData, "Manufacturer");
        modelNumber = getAsString(aboutData, "ModelNumber");
        description = getAsString(aboutData, "Description");
        softwareVersion = getAsString(aboutData, "SoftwareVersion");
        allJoynSdkVersion = getAsString(aboutData, "AJSoftwareVersion");
    }

    @Override
    public byte[] getAppId() {
        return appId;
    }

    @Override
    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    @Override
    public String getDeviceName() {
        return deviceName;
    }

    @Override
    public String getDeviceId() {
        return deviceId;
    }

    @Override
    public String getAppName() {
        return appName;
    }

    @Override
    public String getManufacturer() {
        return manufacturer;
    }

    @Override
    public String getModelNumber() {
        return modelNumber;
    }

    @Override
    public String getDescrition() {
        return description;
    }

    @Override
    public String getSoftwareVersion() {
        return softwareVersion;
    }

    @Override
    public String getAllJoynSdkVersion() {
        return allJoynSdkVersion;
    }

    private String getAsString(Map<String, Variant> aboutData, String key) throws BusException {
        String value = "";
        if (aboutData.containsKey(key)) {
            value = aboutData.get(key).getObject(String.class);
        }
        return value;
    }

    @Override
    public String toString() {
        return deviceName + " (" + deviceId + ")";
    }

}
