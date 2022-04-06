package de.hhn.aib.labsw.blackmirror.controller;

import de.hhn.aib.labsw.blackmirror.view.widgets.AbstractWidget;

import java.util.ArrayList;

/**
 * Class to hold all widgets on one page.
 *
 * @author Niklas Binder
 * @version 2022-03-26
 */
public class Page {

    private ArrayList<AbstractWidget> widgetsOnPage;

    public Page(ArrayList<AbstractWidget> widgetsOnPage) {
        this.widgetsOnPage = widgetsOnPage;
    }

    /**
     * Adds a new widget to the page.
     *
     * @param newWidget Widget to be added
     */
    protected void addWidget(AbstractWidget newWidget) {
        widgetsOnPage.add(newWidget);
    }

    /**
     * Deletes a specific widget on the page.
     *
     * @param widget Widget to be deleted
     */
    protected void deleteWidget(AbstractWidget widget) {
        widgetsOnPage.remove(widget);
    }

    /**
     * Deletes a specific widget with a given index.
     *
     * @param widgetIndex Index of the widget to be deleted
     */
    protected void deleteWidgetByIndex(int widgetIndex) {
        widgetsOnPage.remove(widgetIndex);
    }

    /**
     * Method to set all widgets in the widgets ArrayList invisible.
     */
    protected void setWidgetsInvisible() {
        widgetsOnPage.forEach(w -> w.setVisible(false)); //Sets each widget invisible
    }

    /**
     * Method to set all widgets in the widgets ArrayList visible.
     */
    protected void setWidgetsVisible() {
        widgetsOnPage.forEach(w -> w.setVisible(true)); //Sets each widget visible
    }

    /**
     * Updates all visible widgets on the page every second.
     * Method coded from Markus Marewitz.
     */
    protected void onNextSecond() {
        widgetsOnPage.forEach(AbstractWidget::onNextSecond);
    }
}