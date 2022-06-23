package de.hhn.aib.labsw.blackmirror.controller;

import com.fazecast.jSerialComm.SerialPort;
import de.hhn.aib.labsw.blackmirror.controller.API.MirrorApi;
import de.hhn.aib.labsw.blackmirror.controller.API.websockets.MirrorApiWebsockets;
import de.hhn.aib.labsw.blackmirror.controller.gesture.SerialGestureController;
import de.hhn.aib.labsw.blackmirror.controller.widgets.*;
import de.hhn.aib.labsw.blackmirror.view.widgets.AbstractWidget;
import de.hhn.aib.labsw.blackmirror.view.widgets.clock.ClockFaceType;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Main class containing the entry point and controlling the program.
 *
 * @author Markus Marewitz
 * @author Niklas Binder
 * @version 2022-06-23
 */
public class Main {
    public static void main(String[] args) {
        new Main();
    }

    private final PageController pageController = new PageController();

    public Main() {
        MirrorApi server = MirrorApiWebsockets.getInstance();
        //adjust this for the pi because it uses a different naming schema for serial ports
        SerialGestureController c = new SerialGestureController(SerialPort.getCommPort("COM3"), pageController);
        server.init();

        // @Team add your widgets here to test them -Markus
        ArrayList<AbstractWidgetController> widgets = new ArrayList<>();
        widgets.add(new TodosWidgetController());
        widgets.add(new ClockWidgetController(ClockFaceType.ANALOG));
        widgets.add(new WeatherWidgetController());
        widgets.add(new CalendarWidgetController());
        widgets.add(new EmailNotificationController());

        int i = 0;
        widgets.get(i++).getWidget().setPosition(AbstractWidget.Position.TOP_LEFT);
        widgets.get(i++).getWidget().setPosition(AbstractWidget.Position.TOP_RIGHT);
        widgets.get(i++).getWidget().setPosition(AbstractWidget.Position.BOTTOM_RIGHT);
        widgets.get(i++).getWidget().setPosition(AbstractWidget.Position.BOTTOM_LEFT);

        ArrayList<AbstractWidgetController> widgetsPage2 = new ArrayList<>();
        widgetsPage2.add(new TodosWidgetController());
        widgetsPage2.add(new WeatherWidgetController());
        widgetsPage2.add(new EmailNotificationController());
        widgetsPage2.add(new CalendarWidgetController());
        widgetsPage2.add(new ClockWidgetController(ClockFaceType.DIGITAL));

        i = 0;
        widgetsPage2.get(i++).getWidget().setPosition(AbstractWidget.Position.TOP_LEFT);
        widgetsPage2.get(i++).getWidget().setPosition(AbstractWidget.Position.TOP_RIGHT);
        widgetsPage2.get(i++).getWidget().setPosition(AbstractWidget.Position.BOTTOM_RIGHT);
        widgetsPage2.get(i++).getWidget().setPosition(AbstractWidget.Position.BOTTOM_LEFT);

        //@Team Use this method to add a new page, with a ArrayList of widgets -Niklas
        pageController.addPage(widgets);
        pageController.addPage(widgetsPage2);

        pageController.getCurrentPage().setWidgetsVisible(); //Sets all widgets on the default page visible.
        new SecondsTimer(this::onNextSecond);
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(
                this::onRegularUpdate, 0L, 30L, TimeUnit.SECONDS);
    }

    /**
     * Called each second. See {@link SecondsTimer}.
     */
    private void onNextSecond() {
        pageController.getCurrentPage().onNextSecond();
    }

    /**
     * Called regularly (currently every 30 seconds).
     */
    private void onRegularUpdate() {
        pageController.getCurrentPage().onRegularUpdate();
    }
}