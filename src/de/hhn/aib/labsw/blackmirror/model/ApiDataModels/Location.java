package de.hhn.aib.labsw.blackmirror.model.ApiDataModels;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

/**
 * Dataclass to hold latitude and longtitude
 * Author: Luis Gutzeit
 * Version: 1.0
 */
public record Location(double lat, double lon) {
}
