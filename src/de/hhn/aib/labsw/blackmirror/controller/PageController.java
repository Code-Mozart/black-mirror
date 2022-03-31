package de.hhn.aib.labsw.blackmirror.controller;

import de.hhn.aib.labsw.blackmirror.view.widgets.AbstractWidget;

import java.util.ArrayList;

/**
 *  PageController Class to hold all pages and provide all necessary methods.
 */
public class PageController
{
    private ArrayList<Page> pages;
    private int pageIndex = 0;
    private boolean isStandby = false;

    public PageController ()
    {
        pages = new ArrayList<>();

    }

    /**
     * Creates a new Page and adds it to the list of all Pages.
     * @param widgetsOnPage Widgets to be shown on the new page.
     */
    protected void addPage(ArrayList<AbstractWidget> widgetsOnPage)
    {
        pages.add(new Page(widgetsOnPage));
    }

    /**
     * Adds a new page at the given index.
     * @param widgetsOnPage Widgets to be shown on the new page.
     * @param pageIndex Index where the page should be.
     */
    protected void addPageAtIndex(int pageIndex, ArrayList<AbstractWidget> widgetsOnPage)
    {
        pages.add(pageIndex, new Page(widgetsOnPage));
    }
    /**
     * Deletes a page at the given index.
     * @param pageIndex Index of the page to be deleted.
     */
    protected void deletePage (int pageIndex)
    {
        pages.remove(pageIndex);
    }

    /**
     * Moves a page to another position in the list.
     * @param currentPosition Index, where the page currently is.
     * @param newPosition Index, where the page should be moved to.
     */
    protected void movePage (int currentPosition, int newPosition)
    {
        Page pageToMove = pages.get(currentPosition);
        pages.remove(currentPosition);
        pages.add(newPosition, pageToMove);
    }

    /**
     * Navigates to the next page.
     * If it is on the last page, it navigates from the last page to the first page.
     */
    protected void goToNextPage()
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
    protected void goToPreviousPage()
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
    protected void deactivateStandby()
    {
        isStandby = false;
        getCurrentPage().setWidgetsVisible();
    }

    /**
     * @return Current visible page.
     */
    protected Page getCurrentPage()
    {
        return pages.get(pageIndex);
    }
}
