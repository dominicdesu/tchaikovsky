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
package de.kaizencode.tchaikovsky.bus;

import org.alljoyn.bus.BusAttachment;
import org.alljoyn.bus.Mutable;
import org.alljoyn.bus.ProxyBusObject;
import org.alljoyn.bus.SessionOpts;
import org.alljoyn.bus.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.kaizencode.tchaikovsky.businterface.MCUInterface;
import de.kaizencode.tchaikovsky.businterface.MediaPlayerInterface;
import de.kaizencode.tchaikovsky.businterface.VolumeInterface;
import de.kaizencode.tchaikovsky.bussignal.MediaPlayerSignalHandler;
import de.kaizencode.tchaikovsky.bussignal.SpeakerChangedListener;
import de.kaizencode.tchaikovsky.exception.ConnectionException;
import de.kaizencode.tchaikovsky.speaker.Speaker;

/**
 * Handler for bus communication of a {@link Speaker}.
 * 
 * @author Dominic Lerbs
 */
public class SpeakerBusHandler {

    private final Logger logger = LoggerFactory.getLogger(SpeakerBusHandler.class);

    private static final String OBJECT_PATH = "/net/allplay/MediaPlayer";

    private final MediaPlayerSignalHandler signalHandler;
    private final BusAttachment busAttachment;
    private final String speakerBusName;
    private final short port;
    private Mutable.IntegerValue sessionId;
    private final SpeakerSessionListener sessionListener = new SpeakerSessionListener();

    /**
     * Creates a new {@link SpeakerBusHandler}.
     * 
     * @param bus
     *            {@link BusAttachment} currently in use
     * @param busName
     *            Name of the bus to which the {@link Speaker} is connected
     * @param port
     *            Port where the {@link Speaker} is listening.
     * @param signalHandler
     *            Signal handler registered on the {@link BusAttachment}
     */
    public SpeakerBusHandler(BusAttachment bus, String busName, short port, MediaPlayerSignalHandler signalHandler) {
        this.busAttachment = bus;
        this.speakerBusName = busName;
        this.port = port;
        this.signalHandler = signalHandler;
    }

    /**
     * Establishes a connection with the given {@link Speaker} and returns the {@link ProxyBusObject} for further
     * communication.
     * 
     * @return {@link ProxyBusObject} for further communication though AllPlay interfaces
     * @throws ConnectionException
     *             Exception if connection could not be established
     */
    public ProxyBusObject connect() throws ConnectionException {
        busAttachment.enableConcurrentCallbacks();
        joinSession();
        return getProxyBusObject();
    }

    /**
     * Disconnects from the speaker.
     */
    public void disconnect() {
        busAttachment.leaveSession(sessionId.value);
    }

    /**
     * Ping the speaker.
     * 
     * @param timeoutInMs
     *            Timeout after which the ping fails
     * @return True if the ping was successful, else false
     */
    public boolean ping(int timeoutInMs) {
        logger.debug("Pinging speaker on bus " + speakerBusName);
        Status status = busAttachment.ping(speakerBusName, timeoutInMs);
        logger.debug("Ping returned with status " + status.toString());
        return status == Status.OK;
    }

    /**
     * Enables concurrent callbacks. This method needs to be called if a request is triggered from within a callback
     * method. For example, if from callback method {@link SpeakerChangedListener#onPlaylistChanged()} a call to
     * {@link Speaker#getPlayState()} should be made, {@link #enableConcurrentCallbacks()} needs to be executed
     * beforehand.
     * 
     * For more details see <a href=
     * "https://allseenalliance.org/docs/api/java/org/alljoyn/bus/BusAttachment.html#enableConcurrentCallbacks()">
     * BusAttachment.html#enableConcurrentCallbacks()</a> .
     */
    public void enableConcurrentCallbacks() {
        busAttachment.enableConcurrentCallbacks();
    }

    /**
     * @return The {@link SpeakerSessionListener}
     */
    public SpeakerSessionListener getSessionListener() {
        return sessionListener;
    }

    /**
     * Adds a new {@link SpeakerChangedListener} to be notified when the speaker changes.
     * 
     * @param listener
     *            The {@link SpeakerChangedListener} to be added
     */
    public void addSpeakerChangedListener(SpeakerChangedListener listener) {
        signalHandler.addSpeakerChangedListener(speakerBusName, listener);
    }

    /**
     * Removes a {@link SpeakerChangedListener}
     * 
     * @param listener
     *            The {@link SpeakerChangedListener} to be removed
     */
    public void removeSpeakerChangedListener(SpeakerChangedListener listener) {
        signalHandler.removeSpeakerChangedListener(speakerBusName, listener);
    }

    private void joinSession() throws ConnectionException {
        sessionId = new Mutable.IntegerValue();
        logger.debug("Joining session with busname [" + speakerBusName + "], port [" + port + "], sessionId ["
                + sessionId.value + "]");

        Status status = busAttachment.joinSession(speakerBusName, port, sessionId, createSessionOptions(),
                sessionListener);
        if (status != Status.OK) {
            throw new ConnectionException("Unable to join session " + sessionId.value + " on bus " + speakerBusName,
                    status);
        }
        logger.debug("Joined session from local bus [" + busAttachment.getUniqueName() + "] to remote bus ["
                + speakerBusName + "] on sessionId [" + sessionId.value + "]");
    }

    /**
     * @param timeoutInSec
     *            The timeout in seconds after which a session is declared as lost
     */
    public void setSessionTimeout(int timeoutInSec) {
        busAttachment.setLinkTimeout(sessionId.value, new Mutable.IntegerValue(timeoutInSec));
    }

    private ProxyBusObject getProxyBusObject() {
        ProxyBusObject proxyBusObject = busAttachment.getProxyBusObject(speakerBusName, OBJECT_PATH, sessionId.value,
                new Class<?>[] { MediaPlayerInterface.class, VolumeInterface.class, MCUInterface.class });
        logger.debug("Created ProxyBusObject BusName [" + proxyBusObject.getBusName() + "], object path ["
                + proxyBusObject.getObjPath() + "]");
        return proxyBusObject;
    }

    private SessionOpts createSessionOptions() {
        SessionOpts sessionOpts = new SessionOpts();
        sessionOpts.traffic = SessionOpts.TRAFFIC_MESSAGES;
        sessionOpts.isMultipoint = false;
        sessionOpts.proximity = SessionOpts.PROXIMITY_ANY;
        sessionOpts.transports = SessionOpts.TRANSPORT_ANY;
        return sessionOpts;
    }
}
