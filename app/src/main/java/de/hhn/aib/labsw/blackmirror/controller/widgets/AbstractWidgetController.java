package de.hhn.aib.labsw.blackmirror.controller.widgets;

import com.fasterxml.jackson.databind.JsonNode;
import de.hhn.aib.labsw.blackmirror.controller.API.MirrorApi;
import de.hhn.aib.labsw.blackmirror.controller.API.TopicListener;
import de.hhn.aib.labsw.blackmirror.controller.API.websockets.MirrorApiWebsockets;
import de.hhn.aib.labsw.blackmirror.view.widgets.AbstractWidget;

/**
 * @author Luis Gutzeit
 * @author Markus Marewitz
 * @version 2022-05-16
 */
public abstract class AbstractWidgetController implements TopicListener, AutoCloseable {
    public static final int INVALID_ID = -1;

    private final MirrorApi api = MirrorApiWebsockets.getInstance();
    private int id = INVALID_ID;

    /**
     * subscribe to a topic
     * @param topic the topic to subscribe to
     */
    protected final void subscribe(String topic) {
        api.subscribe(topic, this);
    }

    /**
     * subscribe to a topic with an id
     * @param topic the topic to subscribe to
     */
    protected final void subscribeWithID(String topic) {
        subscribe((getID() != INVALID_ID) ? (topic + "/" + getID()) : topic);
    }

    /**
     * unsubscribe from a topic
     * @param topic the topic to unsubscribe from
     */
    protected final void unsubscribe(String topic) {
        api.unsubscribe(topic, this);
    }

    /**
     * publish a new message
     * @param topic the topic of the message
     * @param payload the paylaod of the message
     */
    protected final void publish(String topic, Object payload) {
        api.publish(topic, payload);
    }
    /**
     * publish a new message
     * @param topic the topic of the message
     * @param payload the paylaod of the message
     */
    protected final void publish(String topic, JsonNode payload) {
        api.publish(topic, payload);
    }

    /**
     * override this method to handle new incoming messages
     * @param topic the topic of the incoming message
     * @param object the payload of the message
     */
    @Override
    public void dataReceived(String topic, JsonNode object) {
    }

    /**
     * get the widget connected to this controller
     * @return the widget of this controller
     */
    public abstract AbstractWidget getWidget();

    /**
     * get the id of this controller
     * @return the id of the controller
     */
    protected int getID() {
        return id;
    }

    /**
     * Sets the ID of the widget depending on its page and position on this page.
     * @throws UnsupportedOperationException Not yet used. Can later be used
     * to sync the id's of widgets between app and mirror
     */
    public void setID(int id) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * A method hook to be called every second.
     */
    public void onNextSecond() {
    }

    /**
     * A method hook to be called regularly.
     */
    public void onRegularUpdate() {
    }

    /**
     * put stuff here that must be closed manually when the widget controller gets closed
     * @throws Exception
     */
    @Override
    public void close() throws Exception {
    }
}
