package de.hhn.aib.labsw.blackmirror.controller.widgets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import de.hhn.aib.labsw.blackmirror.controller.API.websockets.MirrorApiWebsockets;
import de.hhn.aib.labsw.blackmirror.model.ApiDataModels.TodoData;
import de.hhn.aib.labsw.blackmirror.model.ToDoEntry;
import de.hhn.aib.labsw.blackmirror.view.widgets.AbstractWidget;
import de.hhn.aib.labsw.blackmirror.view.widgets.TodosWidget;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TodosWidgetController extends AbstractWidgetController {

    private static final String TODOS_TOPIC = "todoList";

    private List<ToDoEntry> entries = new ArrayList<>();
    private final TodosWidget widget;

    public TodosWidgetController() {
        widget = new TodosWidget();
        widget.setEntries(entries);

        subscribe(TODOS_TOPIC);

        // for test purposes
        Executors.newSingleThreadScheduledExecutor().schedule(
                () -> this.dataReceived(
                        TODOS_TOPIC, MirrorApiWebsockets.getInstance().getMapper().valueToTree(
                                new TodoData(List.of(
                                        new ToDoEntry(System.currentTimeMillis(), "Briefe abschicken"),
                                        new ToDoEntry(System.currentTimeMillis(), "Programmieren für LabSw"),
                                        new ToDoEntry(System.currentTimeMillis(), "Lustige Sachen machen"),
                                        new ToDoEntry(System.currentTimeMillis(), "Karotten einkaufen"),
                                        new ToDoEntry(System.currentTimeMillis(), "Den Müll rausbringen"),
                                        new ToDoEntry(System.currentTimeMillis(), "{wird nicht mehr angezeigt}")
                                ))
                        )),
                3, TimeUnit.SECONDS
        );
    }

    @Override
    public void dataReceived(String topic, JsonNode object) {
        // replace this with a switch statement later and also listen to
        // a topic "getTodoList" in which case the current todo_list is sent
        // from the mirror to the app
        assert Objects.equals(topic, TODOS_TOPIC);

        try {
            TodoData data = nodeToObject(object, TodoData.class);
            entries = data.entries();
            SwingUtilities.invokeLater(() -> widget.setEntries(entries));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public AbstractWidget getWidget() {
        return widget;
    }
}
