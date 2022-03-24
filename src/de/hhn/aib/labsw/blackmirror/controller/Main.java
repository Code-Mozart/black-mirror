package de.hhn.aib.labsw.blackmirror.controller;

import de.hhn.aib.labsw.blackmirror.view.widgets.AbstractWidget;
import de.hhn.aib.labsw.blackmirror.view.widgets.CalendarWidget;
import de.hhn.aib.labsw.blackmirror.view.widgets.HelloWorldWidget;

import java.util.ArrayList;
import java.util.List;

/**
 * Main class containing the entry point and controlling the program.
 *
 * @author Markus Marewitz
 * @version 2022-03-24
 */
public class Main {
    public static void main(String[] args) {
        new Main();
    }

    private List<AbstractWidget> widgets;

    public Main() {
        widgets = new ArrayList<>();

        // @Team add your widgets here to test them -Markus
        widgets.add(new
                CalendarWidget()
        );

        widgets.forEach(w -> w.setVisible(true));

        new SecondsTimer(this::onNextSecond);
    }

    private void onNextSecond() {
        widgets.forEach(AbstractWidget::onNextSecond);
    }
}
