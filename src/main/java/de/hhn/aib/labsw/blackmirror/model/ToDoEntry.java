package de.hhn.aib.labsw.blackmirror.model;

import java.time.ZonedDateTime;

/**
 * @author Markus Marewitz
 * @version 2022-05-19
 */
public record ToDoEntry(
        ZonedDateTime createdDate,
        String text
) {
}
