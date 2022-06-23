package de.hhn.aib.labsw.blackmirror.controller.widgets;

import com.fasterxml.jackson.databind.JsonNode;
import de.hhn.aib.labsw.blackmirror.controller.API.MirrorApi;
import de.hhn.aib.labsw.blackmirror.controller.API.TopicListener;
import de.hhn.aib.labsw.blackmirror.controller.API.websockets.MirrorApiWebsockets;
import de.hhn.aib.labsw.blackmirror.view.widgets.AbstractWidget;

/**
 * Base class for the widget controllers that...
 * <ol>
 *     <li>...acts as a common interface for all widget controllers</li>
 *     <li>...provides derived widget controllers with an interface to communicate with the app.</li>
 * </ol>
 * Each widget controller controls one widget instance.
 *
 * @author Luis Gutzeit
 * @author Markus Marewitz
 * @version 2022-06-23
 * @see AbstractWidget
 */
public abstract class AbstractWidgetController implements TopicListener, AutoCloseable {
    public static final int INVALID_ID = -1;

    private final MirrorApi api = MirrorApiWebsockets.getInstance();
    private int id = INVALID_ID;

    /**
     * Derived widget controllers should use this method to subscribe to a topic
     * and receive corresponding messages from the app.
     */
    protected void subscribe(String topic) {
        api.subscribe(topic, this);
    }

    /**
     * Subscribes a topic with the id of this widget controller.
     *
     * @see #subscribe(String)
     * @see #getID()
     */
    protected void subscribeWithID(String topic) {
        subscribe((getID() != INVALID_ID) ? (topic + "/" + getID()) : topic);
    }

    /**
     * Unsubscribe a topic.
     *
     * @see #subscribe(String)
     */
    protected void unsubscribe(String topic) {
        api.unsubscribe(topic, this);
    }

    /**
     * Publishes a message under the specified topic.
     *
     * @param payload May be any {@link Object} as long as it is parseable to JSON.
     */
    protected void publish(String topic, Object payload) {
        api.publish(topic, payload);
    }

    /**
     * Publishes the JSON string under the specified topic.
     */
    protected void publish(String topic, JsonNode payload) {
        api.publish(topic, payload);
    }

    @Override
    public void dataReceived(String topic, JsonNode object) {
    }

    /**
     * @return The controlled widget which is an instance of a widget class derived from {@link AbstractWidget}.
     */
    public abstract AbstractWidget getWidget();

    /**
     * The ID of a widget instance depends on its page and position on the mirror and can be used
     * to uniquely identify it amongst other widget instances of the same widget type
     * (e.g. to differentiate two clock widgets).
     */
    protected int getID() {
        return id;
    }

    /**
     * Sets the ID of the widget depending on its page and position on this page.
     *
     * @throws UnsupportedOperationException Not yet used. Can later be used
     *                                       to sync the id's of widgets between app and mirror
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

    @Override
    public void close() throws Exception {
    }
}
