package de.hhn.aib.labsw.blackmirror.model;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * @author Markus Marewitz
 * @version 2022-05-19
 */
public record ToDoEntry(
        Date createdDate,
        String text
) {
}
