package de.hhn.aib.labsw.blackmirror.controller;

import de.hhn.aib.labsw.blackmirror.view.widgets.AbstractWidget;

import java.util.ArrayList;

/**
 *  PageController Class to hold all pages and provide all necessary methods.
 *
 * @author Niklas Binder
 * @version 02.04.2022
 */
public class PageController
{
    private ArrayList<Page> pages  = new ArrayList<>();;
    private int pageIndex = 0;
    private boolean isStandby = false;

    public PageController ()
    {
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
     * @param pageIndex Index, where the page currently is.
     * @param newPageIndex Index, where the page should be moved to.
     */
    protected void movePage (int pageIndex, int newPageIndex)
    {
        Page pageToMove = pages.get(pageIndex);
        pages.remove(pageIndex);
        pages.add(newPageIndex, pageToMove);
    }

    /**
     * Navigates to the next page.
     * If it is on the last page, it navigates from the last page to the first page.
     */
    protected void goToNextPage()
    {
        if (!isStandby)
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
        if (!isStandby)
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
     * Activates & Deactivates the standby-mode. While the Standby-Mode is activated,  the navigation is deactivated.
     */
    private void changeMode()
    {
        isStandby = !isStandby;
        if (isStandby)
        {
            getCurrentPage().setWidgetsInvisible();
        }
        else
        {
            getCurrentPage().setWidgetsVisible();
        }
    }

    /**
     * @return Current visible page.
     */
    protected Page getCurrentPage()
    {
        return pages.get(pageIndex);
    }
}
