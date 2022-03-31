package de.hhn.aib.labsw.blackmirror.controller;

import de.hhn.aib.labsw.blackmirror.view.widgets.AbstractWidget;
import de.hhn.aib.labsw.blackmirror.view.widgets.HelloWorldWidget;

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

    private ArrayList<AbstractWidget> widgets;
    private PageController pageController = new PageController();

    public Main() {
        widgets = new ArrayList<>();

        // @Team add your widgets here to test them -Markus
        widgets.add(new
                HelloWorldWidget()
        );

        //@Team Use this method to add a new page, with a ArrayList of widgets -Niklas
        pageController.addPage(widgets);

        pageController.getCurrentPage().setWidgetsVisible(); //Sets all widgets on the default page visible.
        new SecondsTimer(this::updateCurrentPage);
    }
    private void updateCurrentPage()
    {
        pageController.getCurrentPage().updatePage();
    }
}