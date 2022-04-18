package de.hhn.aib.labsw.blackmirror.controller.API.websockets;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Dataclass to hold the Data of a Package send by the API
 */
public class SendPackage {
    public String topic;
    public JsonNode payload;
}
