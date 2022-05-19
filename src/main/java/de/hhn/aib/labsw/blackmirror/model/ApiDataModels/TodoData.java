package de.hhn.aib.labsw.blackmirror.model.ApiDataModels;

import de.hhn.aib.labsw.blackmirror.model.ToDoEntry;

import java.util.List;

public record TodoData(
        List<ToDoEntry> entries
) {
}
