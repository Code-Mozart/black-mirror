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

    public Page(ArrayList<AbstractWidget> widgetsOnPage)
    {
        this.widgetsOnPage = widgetsOnPage;
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
    protected void updatePage() {
        widgetsOnPage.forEach(AbstractWidget::onNextSecond);
    }
}