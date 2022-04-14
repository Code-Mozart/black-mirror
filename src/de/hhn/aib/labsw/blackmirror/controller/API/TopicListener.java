package de.hhn.aib.labsw.blackmirror.controller.API;

import com.fasterxml.jackson.databind.JsonNode;

public interface TopicListener {
    public void dataReceived(String topic, JsonNode object);
}
