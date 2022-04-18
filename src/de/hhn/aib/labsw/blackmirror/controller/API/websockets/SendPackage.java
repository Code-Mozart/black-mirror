package de.hhn.aib.labsw.blackmirror.controller.API.websockets;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Dataclass to hold the Data of a Package send by the API
 * Author: Luis Gutzeit
 * Version 1.1
 */
public record SendPackage(String topic, JsonNode payload) {
}
