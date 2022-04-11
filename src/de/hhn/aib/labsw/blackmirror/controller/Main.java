package de.hhn.aib.labsw.blackmirror.controller;

import de.hhn.aib.labsw.blackmirror.view.widgets.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Main class containing the entry point and controlling the program.
 *
 * @author Markus Marewitz
 * @author Niklas Binder
 * @version 2022-03-26
 */
public class Main {
    public static void main(String[] args) {
        new Main();
    }

    private ArrayList<AbstractWidget> widgets = new ArrayList<>();
    private PageController pageController = new PageController();

    public Main() {
        AWTEventListener listener = event -> {
            try {
                KeyEvent ke = (KeyEvent) event;
                if (ke.getID() != KeyEvent.KEY_PRESSED) return;
                switch (ke.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> pageController.goToPreviousPage();
                    case KeyEvent.VK_RIGHT -> pageController.goToNextPage();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        Toolkit.getDefaultToolkit().addAWTEventListener(listener, AWTEvent.KEY_EVENT_MASK);

        ArrayList<AbstractWidget> widgets = new ArrayList<>();

        // @Team add your widgets here to test them -Markus
        //widgets.add(new HelloWorldWidget());
        widgets.add(new WeatherWidget());
        widgets.add(new CalendarWidget());
        widgets.add(new EmailNotificationWidget());
        widgets.add(new ReminderWidget());

        widgets.get(0).setPosition(AbstractWidget.Position.TOP_LEFT);
        widgets.get(1).setPosition(AbstractWidget.Position.TOP_RIGHT);
        widgets.get(2).setPosition(AbstractWidget.Position.BOTTOM_LEFT);
        widgets.get(3).setPosition(AbstractWidget.Position.BOTTOM_RIGHT);

        //@Team Use this method to add a new page, with a ArrayList of widgets -Niklas
        pageController.addPage(widgets);

        widgets = new ArrayList<>();
        widgets.add(new WeatherWidget());
        widgets.get(0).setPosition(AbstractWidget.Position.TOP_LEFT);
        pageController.addPage(widgets);

        widgets = new ArrayList<>();
        widgets.add(new ClockWidget());
        widgets.add(new EmailNotificationWidget());
        widgets.get(0).setPosition(AbstractWidget.Position.TOP_LEFT);
        widgets.get(1).setPosition(AbstractWidget.Position.TOP_RIGHT);
        pageController.addPage(widgets);

        widgets = new ArrayList<>();
        widgets.add(new CalendarWidget());
        widgets.add(new ReminderWidget());
        widgets.get(0).setPosition(AbstractWidget.Position.BOTTOM_LEFT);
        widgets.get(1).setPosition(AbstractWidget.Position.BOTTOM_RIGHT);
        pageController.addPage(widgets);

        pageController.getCurrentPage().setWidgetsVisible(); //Sets all widgets on the default page visible.
        new SecondsTimer(this::onNextSecond);
    }

    private void onNextSecond() {
        pageController.getCurrentPage().onNextSecond();
    }
}