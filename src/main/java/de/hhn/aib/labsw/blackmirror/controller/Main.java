package de.hhn.aib.labsw.blackmirror.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.hhn.aib.labsw.blackmirror.controller.API.MirrorApi;
import de.hhn.aib.labsw.blackmirror.controller.API.websockets.MirrorApiWebsockets;
import de.hhn.aib.labsw.blackmirror.controller.widgets.*;
import de.hhn.aib.labsw.blackmirror.model.ApiDataModels.TodoData;
import de.hhn.aib.labsw.blackmirror.model.ToDoEntry;
import de.hhn.aib.labsw.blackmirror.view.widgets.AbstractWidget;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Main class containing the entry point and controlling the program.
 *
 * @author Markus Marewitz
 * @author Niklas Binder
 * @version 2022-05-11
 */
public class Main {
    public static void main(String[] args) throws JsonProcessingException {
//        new Main();
        ObjectMapper mapper = new ObjectMapper();
        TodoData data = new TodoData(List.of(
                new ToDoEntry(Date.from(Instant.now()), "Hallo Schaukelpferd"),
                new ToDoEntry(Date.from(Instant.now()), "Hallo Affe"),
                new ToDoEntry(Date.from(Instant.now()), "Hallo Welt")
        ));
        JsonNode json = mapper.valueToTree(data);
        System.out.println(json);
        TodoData data2 = mapper.treeToValue(json, TodoData.class);
        System.out.println(data.equals(data2) ? "[v] Test passed" : "[X] Test failed");
        System.out.println(data2);
    }

    private final PageController pageController = new PageController();

    public Main() {
        MirrorApi server = MirrorApiWebsockets.getInstance();
        server.init();

        // @Team add your widgets here to test them -Markus
        ArrayList<AbstractWidgetController> widgets = new ArrayList<>();
        widgets.add(new TodosWidgetController());
//        widgets.add(new ClockWidgetController(ClockFaceType.ANALOG));
//        widgets.add(new WeatherWidgetController());
//        widgets.add(new CalendarWidgetController());
//        widgets.add(new EmailNotificationController());

        int i = 0;
        widgets.get(i++).getWidget().setPosition(AbstractWidget.Position.TOP_LEFT);
//        widgets.get(i++).getWidget().setPosition(AbstractWidget.Position.TOP_RIGHT);
//        widgets.get(i++).getWidget().setPosition(AbstractWidget.Position.BOTTOM_RIGHT);
//        widgets.get(i++).getWidget().setPosition(AbstractWidget.Position.BOTTOM_LEFT);

        //@Team Use this method to add a new page, with a ArrayList of widgets -Niklas
        pageController.addPage(widgets);

        pageController.getCurrentPage().setWidgetsVisible(); //Sets all widgets on the default page visible.
        new SecondsTimer(this::onNextSecond);
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(
                this::onRegularUpdate, 0L, 30L, TimeUnit.SECONDS);
    }

    private void onNextSecond() {
        pageController.getCurrentPage().onNextSecond();
    }

    private void onRegularUpdate() {
        pageController.getCurrentPage().onRegularUpdate();
    }
}