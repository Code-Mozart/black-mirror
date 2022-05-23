package de.hhn.aib.labsw.blackmirror.model;

/**
 * @author Markus Marewitz
 * @version 2022-05-19
 */
public record ToDoEntry(
        long createdTimestamp,
        String text
) {
}
