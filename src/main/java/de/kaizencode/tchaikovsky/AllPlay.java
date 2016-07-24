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

    static {
        System.loadLibrary("alljoyn_java");
    }

    private static final String INTERFACES[] = { "net.allplay.MediaPlayer" };

    private final BusAttachment busAttachment;
    private SpeakerAboutListener aboutListener;

    public AllPlay() {
        busAttachment = new BusAttachment("Tchaikovsky", BusAttachment.RemoteMessage.Receive);
    }

    /**
     * Connect to the AllJoyn bus.
     * 
     * @throws ConnectionException
     *             Exception occurred during connection setup
     */
    public void connect() throws ConnectionException {
        connectToBus();
        aboutListener = new SpeakerAboutListener(busAttachment);
        busAttachment.registerAboutListener(aboutListener);
    }

    /**
     * Disconnect from the AllJoyn bus.
     */
    public void disconnect() {
        logger.debug("Disconnecting from AllJoyn bus " + busAttachment.getUniqueName());
        busAttachment.unregisterAboutListener(aboutListener);
        busAttachment.disconnect();
    }

    /**
     * @return True if currently connected the the AllJoyn bus, else false
     */
    public boolean isConnected() {
        return busAttachment.isConnected();
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
     * Add a listener for discovered speakers.
     * 
     * @param listener
     *            The listener to be added
     * @throws DiscoveryException
     *             if the listener cannot be added
     */
    public void addSpeakerAnnouncedListener(SpeakerAnnouncedListener listener) throws DiscoveryException {
        if (aboutListener == null) {
            throw new DiscoveryException("Unable to add listener. Connection to bus must be established first");
        }
        aboutListener.addSpeakerAnnouncedListener(listener);
    }

    /**
     * Remove a listener for discovered speakers.
     * 
     * @param listener
     *            The listener to be removed
     */
    public void removeSpeakerAnnouncedListener(SpeakerAnnouncedListener listener) {
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
