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
    private ArrayList<Page> pages;
    private int pageIndex = 0;
    private boolean isStandby = false;

    public Main() {
        widgets = new ArrayList<>();
        pages = new ArrayList<>();

        // @Team add your widgets here to test them -Markus
        widgets.add(new
                HelloWorldWidget()
        );

        //@Team Use this method to add a new page, with a ArrayList of widgets -Niklas
        addPage(widgets);

        getCurrentPage().setWidgetsVisible(); //Sets all widgets on the default page visible.
        new SecondsTimer(this::updateCurrentPage);
    }

    /**
     * Creates a new Page and adds it to the list of all Pages.
     * @param widgetsOnPage Widgets to be shown on the new page.
     */
    private void addPage(ArrayList<AbstractWidget> widgetsOnPage)
    {
        pages.add(new Page(widgetsOnPage));
    }

    /**
     * Adds a new page at the given index.
     * @param widgetsOnPage Widgets to be shown on the new page.
     * @param pageIndex Index where the page should be.
     */
    private void addPageAtIndex(int pageIndex, ArrayList<AbstractWidget> widgetsOnPage)
    {
        pages.add(pageIndex, new Page(widgetsOnPage));
    }
    /**
     * Deletes a page at the given index.
     * @param pageIndex Index of the page to be deleted.
     */
    private void deletePage (int pageIndex)
    {
        pages.remove(pageIndex);
    }

    /**
     * Moves a page to another position in the list.
     * @param currentPosition Index, where the page currently is.
     * @param newPosition Index, where the page should be moved to.
     */
    private void movePage (int currentPosition, int newPosition)
    {
        Page pageToMove = pages.get(currentPosition);
        pages.remove(currentPosition);
        pages.add(newPosition, pageToMove);
    }

    /**
     * Navigates to the next page.
     * If it is on the last page, it navigates from the last page to the first page.
     */
    private void goToNextPage()
    {
        if (isStandby == false)
        {
            int oldIndex = pageIndex;
            if (pageIndex==(pages.size()-1))
            {
                pageIndex = 0;
            }   else {
                pageIndex = pageIndex+1;
            }
            pages.get(oldIndex).setWidgetsInvisible();
            getCurrentPage().setWidgetsVisible();
        }
    }
    /**
     * Navigates to the previous page.
     * If it is on the first page, it navigates from the first page to the last page.
     */
    private void goToPreviousPage()
    {
        if (isStandby == false)
        {
            int oldIndex = pageIndex;
            if (pageIndex==0)
            {
                pageIndex = pages.size()-1;
            }   else {
                pageIndex = pageIndex-1;
            }
            pages.get(oldIndex).setWidgetsInvisible();
            getCurrentPage().setWidgetsVisible();
        }
    }

    /**
     * Activates the standby-mode. After Activation the navigation is deactivated.
     */
    private void activateStandby()
    {
        isStandby = true;
        getCurrentPage().setWidgetsInvisible();
    }
    /**
     * Deactivates the standby-mode. After deactivation the navigation is activated again.
     */
    private void deactivateStandby()
    {
        isStandby = false;
        getCurrentPage().setWidgetsVisible();
    }

    /**
     * @return Current visible page.
     */
    private Page getCurrentPage()
    {
        return pages.get(pageIndex);
    }
    private void updateCurrentPage()
    {
        getCurrentPage().updatePage();
    }
}
