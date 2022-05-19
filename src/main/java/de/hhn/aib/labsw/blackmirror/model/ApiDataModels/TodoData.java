package de.hhn.aib.labsw.blackmirror.model.ApiDataModels;

import de.hhn.aib.labsw.blackmirror.model.ToDoEntry;

import java.util.List;

/**
 * @author Markus Marewitz
 * @version 2022-05-19
 */
public record TodoData(
        List<ToDoEntry> entries
) {
}
