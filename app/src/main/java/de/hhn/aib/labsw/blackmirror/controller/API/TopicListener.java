package de.hhn.aib.labsw.blackmirror.controller.API;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.JsonNode;
import de.hhn.aib.labsw.blackmirror.controller.API.websockets.MirrorApiWebsockets;

/**
 * This Interface must be implemented in order to subscribe to new messages
 * Author: Luis Gutzeit
 * Version: 1.1 - 19.04.2022
 */
public interface TopicListener {
    void dataReceived(String topic, JsonNode object);

    /**
     * converts a JsonNode into the instance of a java record/dataclass
     *
     * @param node   the node to be converted
     * @param tClass Class of which an instance should be created
     * @return instance of the provided class filled with data from the json node
     * @throws JsonProcessingException when json could not be parsed
     * @see com.fasterxml.jackson.databind.ObjectMapper#treeToValue(TreeNode, Class)
     */
    static <T> T nodeToObject(JsonNode node, Class<T> tClass) throws JsonProcessingException {
        return MirrorApiWebsockets.getInstance().getMapper().treeToValue(node, tClass);
    }
}
