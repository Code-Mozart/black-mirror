package de.hhn.aib.labsw.blackmirror.controller.API;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * This Interface must be implemented in order to subscribe to new messages
 */
public interface TopicListener {
    void dataReceived(String topic, JsonNode object);
}
