package de.hhn.aib.labsw.blackmirror.controller.API.websockets;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Dataclass to hold the Data of a Package send by the API
 * Author: Luis Gutzeit
 * Version: 1.1 - 19.04.2022
 */
public record SendPackage(String topic, JsonNode payload) {
}
