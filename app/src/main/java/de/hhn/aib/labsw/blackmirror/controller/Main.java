package de.hhn.aib.labsw.blackmirror.controller;

import com.fazecast.jSerialComm.SerialPort;
import de.hhn.aib.labsw.blackmirror.controller.API.MirrorApi;
import de.hhn.aib.labsw.blackmirror.controller.API.websockets.MirrorApiWebsockets;
import de.hhn.aib.labsw.blackmirror.controller.gesture.SerialGestureController;
import de.hhn.aib.labsw.blackmirror.controller.widgets.*;
import de.hhn.aib.labsw.blackmirror.view.widgets.AbstractWidget;
import de.hhn.aib.labsw.blackmirror.view.widgets.clock.ClockFaceType;

import java.io.IOException;
import java.util.ArrayList;
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
    public static void main(String[] args) {
        new Main();
    }

    private final PageController pageController = new PageController();

    public Main() {
        MirrorApi server = MirrorApiWebsockets.getInstance();
        //adjust this for the pi because it uses a different naming schema for serial ports

        try {
            /*SerialGestureController c = new SerialGestureController(SerialPort.getCommPort(
                    "/dev/serial/by-path/platform-3f980000.usb-usb-0:1.2:1.0"
            ), pageController);*/
            SerialGestureController c = new SerialGestureController(SerialPort.getCommPort("COM3"), pageController);
        } catch (Exception e){
            System.out.println("could not establish connection with serial gesture controller");
        }
        server.init();

        // @Team add your widgets here to test them -Markus
        ArrayList<AbstractWidgetController> widgets = new ArrayList<>();
        widgets.add(new TodosWidgetController());
        widgets.add(new ClockWidgetController(ClockFaceType.ANALOG));
        widgets.add(new ClockWidgetController(ClockFaceType.DIGITAL));
        widgets.add(new WeatherWidgetController());
        widgets.add(new CalendarWidgetController());
        widgets.add(new EmailNotificationController());

        int i = 0;
        widgets.get(i++).getWidget().setPosition(0, 0);
        widgets.get(i++).getWidget().setPosition(1, 0);
        widgets.get(i++).getWidget().setPosition(2, 2);
        widgets.get(i++).getWidget().setPosition(3, 0);
        widgets.get(i++).getWidget().setPosition(0, 1);
        widgets.get(i++).getWidget().setPosition(1, 1);


        ArrayList<AbstractWidgetController> widgetsPage2 = new ArrayList<>();
        widgetsPage2.add(new TodosWidgetController());
        widgetsPage2.add(new WeatherWidgetController());
        widgetsPage2.add(new EmailNotificationController());
        widgetsPage2.add(new CalendarWidgetController());
        widgetsPage2.add(new ClockWidgetController(ClockFaceType.DIGITAL));

        i = 0;
        widgetsPage2.get(i++).getWidget().setPosition(0, 0);
        widgetsPage2.get(i++).getWidget().setPosition(1, 0);
        widgetsPage2.get(i++).getWidget().setPosition(2, 2);
        widgetsPage2.get(i++).getWidget().setPosition(3, 0);

        //@Team Use this method to add a new page, with a ArrayList of widgets -Niklas
        pageController.addPage(widgets);
        pageController.addPage(widgetsPage2);

        pageController.getCurrentPage().setWidgetsVisible(); //Sets all widgets on the default page visible.
        new SecondsTimer(this::onNextSecond);
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(
                this::onRegularUpdate, 0L, 30L, TimeUnit.SECONDS);
    }

    private void onNextSecond() {
        Page cPage = pageController.getCurrentPage();
        if (cPage != null) {
            pageController.getCurrentPage().onNextSecond();
        }
    }

    private void onRegularUpdate() {
        pageController.getCurrentPage().onRegularUpdate();
    }
}