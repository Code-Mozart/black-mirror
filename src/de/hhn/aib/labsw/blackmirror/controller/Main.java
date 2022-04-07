package de.hhn.aib.labsw.blackmirror.controller;

import de.hhn.aib.labsw.blackmirror.view.widgets.AbstractWidget;
import de.hhn.aib.labsw.blackmirror.view.widgets.HelloWorldWidget;
import de.hhn.aib.labsw.blackmirror.view.widgets.ReminderWidget;
import de.hhn.aib.labsw.blackmirror.view.widgets.WeatherWidget;

import java.util.ArrayList;

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
        // @Team add your widgets here to test them -Markus
        //widgets.add(new HelloWorldWidget());
        widgets.add(new WeatherWidget());

        //@Team Use this method to add a new page, with a ArrayList of widgets -Niklas
        pageController.addPage(widgets);

        pageController.getCurrentPage().setWidgetsVisible(); //Sets all widgets on the default page visible.
        new SecondsTimer(this::onNextSecond);
    }

    private void onNextSecond() {
        pageController.getCurrentPage().onNextSecond();
    }
}