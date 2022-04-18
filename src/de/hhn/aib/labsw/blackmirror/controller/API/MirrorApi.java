package de.hhn.aib.labsw.blackmirror.controller.API;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Definition of the API methods
 * Author: Luis Gutzeit
 * Version: 1.0
 */
public interface MirrorApi {
    ObjectMapper getMapper();
    void subscribe(String topic, TopicListener listener);
    void publish(String topic, Object payload);
    void publish(String topic, JsonNode payload);
}
