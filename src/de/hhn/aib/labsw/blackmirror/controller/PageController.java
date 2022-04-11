package de.hhn.aib.labsw.blackmirror.controller;

import de.hhn.aib.labsw.blackmirror.view.widgets.AbstractWidget;

import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * PageController Class to hold all pages and provide all necessary methods.
 *
 * @author Niklas Binder
 * @version 2022-07-04
 */
public class PageController {
    private ArrayList<Page> pages = new ArrayList<>();
    ;
    private int pageIndex = 0;
    private boolean isStandby = false;

    public PageController() {
    }

    /**
     * Creates a new Page and adds it to the list of all Pages.
     *
     * @param widgetsOnPage Widgets to be shown on the new page.
     */
    protected void addPage(ArrayList<AbstractWidget> widgetsOnPage) {
        pages.add(new Page(widgetsOnPage));
    }

    /**
     * Adds a new page at the given index.
     *
     * @param pageIndex     Index where the page should be.
     * @param widgetsOnPage Widgets to be shown on the new page.
     * @throws IndexOutOfBoundsException Exception if pageIndex is lower than 0 or higher than the size of the list - 1.
     */
    protected void addPageAtIndex(int pageIndex, ArrayList<AbstractWidget> widgetsOnPage) throws IndexOutOfBoundsException {
        if (pageIndex < 0) {
            throw new IndexOutOfBoundsException("Index has to be 0 or higher than 0.");
        } else if (pageIndex >= pages.size()) {
            throw new IndexOutOfBoundsException("Index has to be lower than the size of the list - 1.");
        } else {
            pages.add(pageIndex, new Page(widgetsOnPage));
        }
    }

    /**
     * Deletes a given page.
     *
     * @param page Page to be deleted.
     * @throws NoSuchElementException Exception thrown if the given page is not in the list.
     */
    protected void deletePage(Page page) throws NoSuchElementException {
        if (!pages.contains(page)) {
            throw new NoSuchElementException("The given page doesn`t exist.");
        } else {
            pages.remove(page);
        }
    }

    /**
     * Deletes a page at the given index.
     *
     * @param pageIndex Index of the page to be deleted.
     * @throws NoSuchElementException Exception if pageIndex is lower than 0 or higher than the size of the list - 1.
     */
    protected void deletePageAtIndex(int pageIndex) throws NoSuchElementException {
        if (pageIndex < 0) {
            throw new IndexOutOfBoundsException("Index has to be 0 or higher than 0.");
        } else if (pageIndex >= pages.size()) {
            throw new IndexOutOfBoundsException("Index has to be lower than the size of the list - 1.");
        } else {
            pages.remove(pageIndex);
        }
    }

    /**
     * Moves a page to another position in the list.
     *
     * @param pageIndex    Index, where the page currently is.
     * @param newPageIndex Index, where the page should be moved to.
     * @throws NoSuchElementException Exception if one of the given indexes is lower than 0 or higher than the size of the list - 1.
     */
    protected void movePage(int pageIndex, int newPageIndex) throws NoSuchElementException {
        if (pageIndex < 0) {
            throw new IndexOutOfBoundsException("PageIndex has to be 0 or higher than 0.");
        } else if (pageIndex >= pages.size()) {
            throw new IndexOutOfBoundsException("PageIndex has to be lower than the size of the list - 1.");
        } else if (newPageIndex < 0) {
            throw new IndexOutOfBoundsException("NewPageIndex has to be 0 or higher than 0.");
        } else if (newPageIndex >= pages.size()) {
            throw new IndexOutOfBoundsException("NewPageIndex has to be lower than the size of the list - 1.");
        } else {
            Page pageToMove = pages.get(pageIndex);
            pages.remove(pageIndex);
            pages.add(newPageIndex, pageToMove);
        }
    }

    /**
     * Navigates to the next page.
     * If it is on the last page, it navigates from the last page to the first page.
     */
    protected void goToNextPage() {
        if (!isStandby) {
            try {
                goToAnyPage(pageIndex + 1);
            } catch (IndexOutOfBoundsException e1) {
                try {
                    goToAnyPage(0);
                } catch (IndexOutOfBoundsException e2) {
                }
            }
        }
    }

    /**
     * Navigates to the previous page.
     * If it is on the first page, it navigates from the first page to the last page.
     */
    protected void goToPreviousPage() {
        if (!isStandby) {
            try {
                goToAnyPage(pageIndex - 1);
            } catch (IndexOutOfBoundsException e1) {
                try {
                    goToAnyPage(pages.size() - 1);
                } catch (IndexOutOfBoundsException e2) {
                }
            }
        }
    }

    /**
     * Method to navigate to any page.
     *
     * @param pageIndex Index of the page to be navigated to.
     * @throws IndexOutOfBoundsException Exception to be thrown, if the given Index is lower than 0 or higher than the amount of pages-1.
     */
    protected void goToAnyPage(int pageIndex) throws IndexOutOfBoundsException {
        if (pageIndex < 0) {
            throw new IndexOutOfBoundsException("Index has to be 0 or higher than 0");
        } else if (pageIndex >= pages.size()) {
            throw new IndexOutOfBoundsException("Index has to be lower than " + pages.size());
        } else {
            getCurrentPage().setWidgetsInvisible();
            this.pageIndex = pageIndex;
            getCurrentPage().setWidgetsVisible();
        }
    }

    /**
     * Activates & Deactivates the standby-mode. While the Standby-Mode is activated,  the navigation is deactivated.
     */
    protected void changeMode() {
        isStandby = !isStandby;
        if (isStandby) {
            getCurrentPage().setWidgetsInvisible();
        } else {
            getCurrentPage().setWidgetsVisible();
        }
    }

    /**
     * @return Current visible page.
     */
    protected Page getCurrentPage() {
        return pages.get(pageIndex);
    }
}