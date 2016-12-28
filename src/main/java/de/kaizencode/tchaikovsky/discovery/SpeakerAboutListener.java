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
package de.kaizencode.tchaikovsky.discovery;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.alljoyn.bus.AboutListener;
import org.alljoyn.bus.AboutObjectDescription;
import org.alljoyn.bus.BusAttachment;
import org.alljoyn.bus.BusException;
import org.alljoyn.bus.Status;
import org.alljoyn.bus.Variant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.kaizencode.tchaikovsky.bus.SpeakerBusHandler;
import de.kaizencode.tchaikovsky.bussignal.MediaPlayerSignalHandler;
import de.kaizencode.tchaikovsky.exception.ConnectionException;
import de.kaizencode.tchaikovsky.listener.SpeakerAnnouncedListener;
import de.kaizencode.tchaikovsky.speaker.Speaker;
import de.kaizencode.tchaikovsky.speaker.SpeakerDetails;
import de.kaizencode.tchaikovsky.speaker.remote.RemoteSpeaker;
import de.kaizencode.tchaikovsky.speaker.remote.RemoteSpeakerDetails;

/**
 * Listener for AllJoyn About messages.
 * 
 * @author Dominic Lerbs
 */
public class SpeakerAboutListener implements AboutListener {

    private final Logger logger = LoggerFactory.getLogger(SpeakerAboutListener.class);

    private final List<SpeakerAnnouncedListener> listeners = new ArrayList<>();
    private final BusAttachment busAttachment;
    private MediaPlayerSignalHandler signalHandler;

    /**
     * Constructor for a new About listener which is informed when a new speaker is discovered by the AllJoyn framework.
     * 
     * @param busAttachment
     *            The {@link BusAttachment} currently connected to
     * @throws ConnectionException
     *             if the listener cannot be created
     */
    public SpeakerAboutListener(BusAttachment busAttachment) throws ConnectionException {
        this.busAttachment = busAttachment;
        registerSignalHandler();
    }

    @Override
    public void announced(String speakerBusName, int version, short port, AboutObjectDescription[] objectDescriptions,
            Map<String, Variant> aboutData) {

        logObjectDescriptions(objectDescriptions);

        try {
            SpeakerDetails details = new RemoteSpeakerDetails(aboutData);

            logger.info("New speaker " + details.getDeviceId() + "(" + details.getDeviceName()
                    + ") announced at busName " + speakerBusName + ", version=" + version + ", port=" + port);

            SpeakerBusHandler busHandler = new SpeakerBusHandler(busAttachment, speakerBusName, port, signalHandler);
            RemoteSpeaker speaker = new RemoteSpeaker(busHandler, details);

            for (SpeakerAnnouncedListener listener : listeners) {
                listener.onSpeakerAnnounced(speaker);
            }
        } catch (BusException e) {
            logger.error("Unable to read aboutData for speaker at bus " + speakerBusName, e);
        }
    }

    /**
     * Add a new {@link SpeakerAnnouncedListener} to be informed when a new {@link Speaker} has been discovered.
     * 
     * @param listener
     *            The {@link SpeakerAnnouncedListener} to be informed
     */
    public void addSpeakerAnnouncedListener(SpeakerAnnouncedListener listener) {
        listeners.add(listener);
        logger.debug("New SpeakerAnnouncedListener " + listener.toString() + " has been added");
    }

    /**
     * Remove a {@link SpeakerAnnouncedListener}
     * 
     * @param listener
     *            The {@link SpeakerAnnouncedListener} to be removed
     */
    public void removeSpeakerAnnouncedListener(SpeakerAnnouncedListener listener) {
        listeners.remove(listener);
        logger.debug("SpeakerAnnouncedListener " + listener.toString() + " has been removed");
    }

    private void registerSignalHandler() throws ConnectionException {
        logger.debug("Registering signal handler");
        signalHandler = new MediaPlayerSignalHandler(busAttachment);

        Status status = busAttachment.registerSignalHandlers(signalHandler);
        if (status != Status.OK) {
            throw new ConnectionException("Error while registering signal handler on bus", status);
        }
    }

    private void logObjectDescriptions(AboutObjectDescription[] objectDescriptions) {
        if (logger.isTraceEnabled()) {
            logger.trace("Announced ObjectDescriptions:");
            if (objectDescriptions != null) {
                for (AboutObjectDescription o : objectDescriptions) {
                    logger.trace("Path:\t" + o.path);
                    logger.trace("Interfaces:");
                    for (String s : o.interfaces) {
                        logger.trace("\t\t" + s);
                    }
                }
            }
        }
    }
}
