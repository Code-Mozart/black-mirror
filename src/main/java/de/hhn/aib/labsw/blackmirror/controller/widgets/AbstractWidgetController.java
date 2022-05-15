package de.hhn.aib.labsw.blackmirror.controller.widgets;

import de.hhn.aib.labsw.blackmirror.controller.API.MirrorApi;
import de.hhn.aib.labsw.blackmirror.controller.API.TopicListener;
import de.hhn.aib.labsw.blackmirror.controller.API.websockets.MirrorApiWebsockets;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import de.hhn.aib.labsw.blackmirror.view.widgets.AbstractWidget;

/**
 * @author Luis Gutzeit
 * @version 2022-05-10
 */
public abstract class AbstractWidgetController implements TopicListener, AutoCloseable {
    private final MirrorApi api = MirrorApiWebsockets.getInstance();

    protected final void subscribe(String topic) {
        api.subscribe(topic, this);
    }

    protected final void unsubscribe(String topic) {
        api.unsubscribe(topic, this);
    }

    protected final void publish(String topic, Object payload) {
        api.publish(topic, payload);
    }

    protected final void publish(String topic, JsonNode payload) {
        api.publish(topic, payload);
    }

    @Override
    public void dataReceived(String topic, JsonNode object) {
    }

    /**
     * converts a JsonNode into the instance of a java record/dataclass
     *
     * @param node   the node to be converted
     * @param tClass Class of which an instance should be created
     * @return instance of the provided class filled with data from the json node
     * @throws JsonProcessingException when json could not be parsed
     */
    protected <T> T nodeToObject(JsonNode node, Class<T> tClass) throws JsonProcessingException {
        return api.getMapper().treeToValue(node, tClass);
    }

    public abstract AbstractWidget getWidget();

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
