package de.hhn.aib.labsw.blackmirror.controller;

import de.hhn.aib.labsw.blackmirror.view.widgets.AbstractWidget;

import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Class to hold all widgets on one page.
 *
 * @author Niklas Binder
 * @version 2022-07-26
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
     * @throws NoSuchElementException Exception thrown if the given widget is not in the list.
     */
    protected void deleteWidget(AbstractWidget widget) throws NoSuchElementException {
        if (!widgetsOnPage.contains(widget)) {
            throw new NoSuchElementException("The given widget doesn`t exist.");
        } else {
            widgetsOnPage.remove(widget);
        }
    }

    /**
     * Deletes a specific widget with a given index.
     *
     * @param widgetIndex Index of the widget to be deleted
     * @throws IndexOutOfBoundsException Exception if pageIndex is lower than 0 or higher than the size of the list - 1.
     */
    protected void deleteWidgetByIndex(int widgetIndex) throws IndexOutOfBoundsException {
        if (widgetIndex < 0) {
            throw new IndexOutOfBoundsException("Index has to be higher than 0.");
        } else if (widgetIndex >= widgetsOnPage.size()) {
            throw new IndexOutOfBoundsException("Index has to be lower than the size of the list - 1.");
        } else {
            widgetsOnPage.remove(widgetIndex);
        }
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