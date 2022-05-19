package de.hhn.aib.labsw.blackmirror.controller.widgets;

import de.hhn.aib.labsw.blackmirror.model.ToDoEntry;
import de.hhn.aib.labsw.blackmirror.view.widgets.AbstractWidget;
import de.hhn.aib.labsw.blackmirror.view.widgets.TodosWidget;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TodosWidgetController extends AbstractWidgetController {

    private static final String TODOS_TOPIC = "todoList";

    private List<ToDoEntry> entries = new ArrayList<>();
    private final TodosWidget widget;

    public TodosWidgetController(){
        widget = new TodosWidget();
        widget.setEntries(entries);

//        entries.add(new ToDoEntry(ZonedDateTime.now(), "Briefe abschicken"));
//        entries.add(new ToDoEntry(ZonedDateTime.now(), "Programmieren für LabSw"));
//        entries.add(new ToDoEntry(ZonedDateTime.now(), "Entry 3"));
//        entries.add(new ToDoEntry(ZonedDateTime.now(), "Entry 4"));
//        entries.add(new ToDoEntry(ZonedDateTime.now(), "Entry 5"));
//        entries.add(new ToDoEntry(ZonedDateTime.now(), "Entry 6"));
//        widget.setEntries(entries);
    }

    @Override
    public AbstractWidget getWidget() {
        return widget;
    }
}
