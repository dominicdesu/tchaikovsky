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
import de.kaizencode.tchaikovsky.discovery.SpeakerAnnouncedListener;
import de.kaizencode.tchaikovsky.exception.ConnectionException;
import de.kaizencode.tchaikovsky.exception.DiscoveryException;

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

    private static final String INTERFACES[] = { "net.allplay.MediaPlayer" };

    private BusAttachment busAttachment;
    private SpeakerAboutListener aboutListener;

    public AllPlay() {
    }

    /**
     * Connect to the AllJoyn bus.
     * 
     * @throws ConnectionException
     *             Exception occurred during connection setup
     */
    public void connect() throws ConnectionException {
        busAttachment = new BusAttachment("Tchaikovsky", BusAttachment.RemoteMessage.Receive);
        connectToBus();
        aboutListener = new SpeakerAboutListener(busAttachment);
        for (SpeakerAnnouncedListener listener : speakerAnnounedListeners) {
            aboutListener.addSpeakerAnnouncedListener(listener);
        }
        busAttachment.registerAboutListener(aboutListener);
    }

    /**
     * Disconnect from the AllJoyn bus.
     */
    public void disconnect() {
        if (isConnected()) {
            logger.debug("Disconnecting from AllJoyn bus " + busAttachment.getUniqueName());
            busAttachment.unregisterAboutListener(aboutListener);
            busAttachment.cancelWhoImplements(INTERFACES);
            busAttachment.disconnect();
            busAttachment = null;
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
        defineInterests();
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
    }

    private void connectToBus() throws ConnectionException {
        logger.info("Connecting to AllJoyn bus");
        Status status = busAttachment.connect();
        if (status != Status.OK) {
            throw new ConnectionException("Unable to connect to AllJoyn bus", status);
        }
        logger.info("Successfully connected to allJoyn bus with bus name " + busAttachment.getUniqueName());
    }

    private void defineInterests() throws DiscoveryException {
        Status status = busAttachment.whoImplements(INTERFACES);
        if (status != Status.OK) {
            throw new DiscoveryException("Error while defining interests", status);
        }
    }

}
