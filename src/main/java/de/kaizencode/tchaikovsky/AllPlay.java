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
package de.kaizencode.tchaikovsky;

import java.util.ArrayList;
import java.util.List;

import org.alljoyn.bus.BusAttachment;
import org.alljoyn.bus.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.kaizencode.tchaikovsky.discovery.SpeakerAboutListener;
import de.kaizencode.tchaikovsky.discovery.SpeakerBusListener;
import de.kaizencode.tchaikovsky.exception.ConnectionException;
import de.kaizencode.tchaikovsky.exception.DiscoveryException;
import de.kaizencode.tchaikovsky.listener.SpeakerAnnouncedListener;

/**
 * Main class for connecting to the AllJoyn bus and starting the discovery process.
 * 
 * @author Dominic Lerbs
 */
public class AllPlay {

    private final Logger logger = LoggerFactory.getLogger(AllPlay.class);
    List<SpeakerAnnouncedListener> speakerAnnounedListeners = new ArrayList<>();

    static {
        System.loadLibrary("alljoyn_java");
    }

    private static final String SERVICE_NAME = "net.allplay.MediaPlayer";
    public static final String WELL_KNOWN_NAME_PREFIX = SERVICE_NAME + ".i";
    private static final String INTERFACES[] = { SERVICE_NAME };

    private BusAttachment busAttachment;
    private SpeakerAboutListener aboutListener;
    private SpeakerBusListener busListener;
    private String applicationName = "Tchaikovsky";

    /**
     * AllJoyn allows two discovery modes: Either via the well-known name prefix of a speaker {@link #NAME_BASED}) or
     * via speaker announcements ({@link #ANNOUNCEMENT_BASED}).
     */
    public enum DiscoveryMode {
        NAME_BASED, ANNOUNCEMENT_BASED
    }

    public AllPlay() {
    }

    /**
     * @param name
     *            The name of the application using the allJoyn bus
     */
    public AllPlay(String name) {
        this();
        applicationName = name;
    }

    /**
     * Connect to the AllJoyn bus.
     * 
     * @throws ConnectionException
     *             Exception occurred during connection setup
     */
    public void connect() throws ConnectionException {
        busAttachment = new BusAttachment(applicationName, BusAttachment.RemoteMessage.Receive);

        connectToBus();
        busListener = new SpeakerBusListener(busAttachment);
        aboutListener = new SpeakerAboutListener(busAttachment);
        for (SpeakerAnnouncedListener listener : speakerAnnounedListeners) {
            aboutListener.addSpeakerAnnouncedListener(listener);
            busListener.addSpeakerAnnouncedListener(listener);
        }
        busAttachment.registerAboutListener(aboutListener);
        busAttachment.registerBusListener(busListener);
    }

    /**
     * Disconnect from the AllJoyn bus.
     */
    public void disconnect() {
        if (busAttachment != null) {
            logger.debug("Disconnecting from AllJoyn bus " + busAttachment.getUniqueName());
            busAttachment.unregisterAboutListener(aboutListener);
            busAttachment.unregisterBusListener(busListener);
            if (busAttachment.isConnected()) {
                busAttachment.disconnect();
            }
            busAttachment = null;
        } else {
            logger.debug("Disconnect requested, but not connected to bus - ignoring");
        }
    }

    /**
     * @return True if currently connected the the AllJoyn bus, else false
     */
    public boolean isConnected() {
        return busAttachment != null && busAttachment.isConnected();
    }

    /**
     * Start the discovery of AllPlay speakers. Bus connection has to be established first using {@link #connect()}
     * method.
     * 
     * @throws DiscoveryException
     *             Exception while looking for available speakers.
     */
    public void discoverSpeakers() throws DiscoveryException {
        discoverSpeakers(DiscoveryMode.NAME_BASED);
    }

    /**
     * Start the discovery of AllPlay speakers using the given {@link DiscoveryMode}. Bus connection has to be
     * established first using {@link #connect()} method.
     * 
     * @param mode
     *            The {@link DiscoveryMode} to use
     * @throws DiscoveryException
     *             Exception while looking for available speakers.
     */
    public void discoverSpeakers(DiscoveryMode mode) throws DiscoveryException {
        if (mode == DiscoveryMode.NAME_BASED) {
            findAdvertisedName(SERVICE_NAME);
        } else {
            defineInterests();
        }
    }

    /**
     * Discovers a speaker using its device ID
     * 
     * @param deviceId
     *            The ID of the device to discover
     * @throws DiscoveryException
     *             Exception during discovery
     */
    public void discoverSpeaker(String deviceId) throws DiscoveryException {
        findAdvertisedName(WELL_KNOWN_NAME_PREFIX + deviceId);
    }

    /**
     * Reconnect to the AllJoyn bus.
     * 
     * @throws ConnectionException
     *             Error during reconnection
     */
    public void reconnect() throws ConnectionException {
        disconnect();
        connect();
    }

    /**
     * Cancel listening for new AllPlay devices
     */
    public void cancelDiscovery() {
        if (isConnected()) {
            busAttachment.cancelWhoImplements(INTERFACES);
            busAttachment.cancelFindAdvertisedName(SERVICE_NAME);
        }
    }

    /**
     * Add a listener for discovered speakers.
     * 
     * @param listener
     *            The listener to be added
     * @throws DiscoveryException
     *             if the listener cannot be added
     */
    public void addSpeakerAnnouncedListener(SpeakerAnnouncedListener listener) throws DiscoveryException {
        speakerAnnounedListeners.add(listener);
        if (aboutListener != null) {
            aboutListener.addSpeakerAnnouncedListener(listener);
        }
        if (busListener != null) {
            busListener.addSpeakerAnnouncedListener(listener);
        }
    }

    /**
     * Remove a listener for discovered speakers.
     * 
     * @param listener
     *            The listener to be removed
     */
    public void removeSpeakerAnnouncedListener(SpeakerAnnouncedListener listener) {
        speakerAnnounedListeners.remove(listener);
        if (aboutListener != null) {
            aboutListener.removeSpeakerAnnouncedListener(listener);
        }
        if (busListener != null) {
            busListener.removeSpeakerAnnouncedListener(listener);
        }
    }

    private void connectToBus() throws ConnectionException {
        logger.info("Connecting to AllJoyn bus");
        Status status = busAttachment.connect();
        if (status != Status.OK) {
            throw new ConnectionException("Unable to connect to AllJoyn bus", status);
        }
        logger.info("Successfully connected to allJoyn bus with bus name " + busAttachment.getUniqueName());
    }

    private void findAdvertisedName(String namePrefix) throws DiscoveryException {
        busAttachment.cancelFindAdvertisedName(namePrefix);
        Status status = busAttachment.findAdvertisedName(namePrefix);
        if (status != Status.OK) {
            throw new DiscoveryException("Error while finding advertised name", status);
        }
    }

    private void defineInterests() throws DiscoveryException {
        Status status = busAttachment.whoImplements(INTERFACES);
        if (status != Status.OK) {
            throw new DiscoveryException("Error while defining interests", status);
        }
    }

}
