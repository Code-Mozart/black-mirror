package de.hhn.aib.labsw.blackmirror.controller.widgets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import de.hhn.aib.labsw.blackmirror.controller.API.TopicListener;
import de.hhn.aib.labsw.blackmirror.model.ApiDataModels.TodoData;
import de.hhn.aib.labsw.blackmirror.model.ToDoEntry;
import de.hhn.aib.labsw.blackmirror.view.widgets.AbstractWidget;
import de.hhn.aib.labsw.blackmirror.view.widgets.TodosWidget;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @version 2022-06-27
 */
public class TodosWidgetController extends AbstractWidgetController {

    private static final String TODOS_TOPIC = "todoList";
    private static final String FETCH_TODOS_TOPIC = "fetchTodoList";

    private List<ToDoEntry> entries = new ArrayList<>();
    private final TodosWidget widget;

    public TodosWidgetController() {
        widget = new TodosWidget();
        entries.add(new ToDoEntry(System.currentTimeMillis(), "test"));
        widget.setEntries(entries);

        subscribe(TODOS_TOPIC);
        subscribe(FETCH_TODOS_TOPIC);
    }

    @Override
    public void dataReceived(String topic, JsonNode object) {
        try {
            if (Objects.equals(topic, TODOS_TOPIC)) {
                TodoData data = TopicListener.nodeToObject(object, TodoData.class);
                entries = data.entries();
                SwingUtilities.invokeLater(() -> widget.setEntries(entries));
            } else if (Objects.equals(topic, FETCH_TODOS_TOPIC)) {
                publish(TODOS_TOPIC, new TodoData(entries));
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public AbstractWidget getWidget() {
        return widget;
    }
}
