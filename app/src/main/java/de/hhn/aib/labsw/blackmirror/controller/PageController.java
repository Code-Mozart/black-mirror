package de.hhn.aib.labsw.blackmirror.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import de.hhn.aib.labsw.blackmirror.controller.widgets.*;
import de.hhn.aib.labsw.blackmirror.model.ApiDataModels.LayoutData;
import de.hhn.aib.labsw.blackmirror.model.ApiDataModels.WidgetData;
import de.hhn.aib.labsw.blackmirror.view.widgets.AbstractWidget;
import de.hhn.aib.labsw.blackmirror.view.widgets.clock.ClockFaceType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * PageController Class to hold all pages and provide all necessary methods.
 *
 * @author Niklas Binder
 * @author Markus Marewitz
 * @version 2022-05-11
 */
public class PageController extends AbstractWidgetController {
    private ArrayList<Page> pages = new ArrayList<>();
    private static final String PAGE_UPDATE_TOPIC = "pageUpdate";

    private int pageIndex = 0;
    private boolean isStandby = false;

    public PageController() {
        new SecondsTimer(this::autoChangePageByTime);
        subscribe(PAGE_UPDATE_TOPIC);
    }

    /**
     * Creates a new Page and adds it to the list of all Pages.
     *
     * @param widgetsOnPage Widgets to be shown on the new page.
     */
    protected void addPage(ArrayList<AbstractWidgetController> widgetsOnPage) {
        pages.add(new Page(widgetsOnPage));
    }

    /**
     * Adds a new page at the given index.
     *
     * @param pageIndex     Index where the page should be.
     * @param widgetsOnPage Widgets to be shown on the new page.
     * @throws IndexOutOfBoundsException Exception if pageIndex is lower than 0 or higher than the size of the list - 1.
     */
    protected void addPageAtIndex(int pageIndex, ArrayList<AbstractWidgetController> widgetsOnPage) throws IndexOutOfBoundsException {
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

    /**
     * Automatically changes once to default Pages depending on current time.
     *  Time     Page
     *  08 AM     0
     *  16 AM     1
     */
    private void autoChangePageByTime() {
        // at least 2 pages have to be initialised beforehand for this method to work properly
        if(pages.size() >= 2) {
            LocalDateTime now = LocalDateTime.now();

            if (now.getHour() == 8 && now.getMinute() == 0 && now.getSecond() == 0) {
                goToAnyPage(0);
                return;
            }

            if (now.getHour() == 16 && now.getMinute() == 0 && now.getSecond() == 0) {
                goToAnyPage(1);
            }
        }
    }

    @Override
    public void dataReceived(String topic, JsonNode object) {
        super.dataReceived(topic, object);

        assert (topic.equals(PAGE_UPDATE_TOPIC)) : "Wrong topic received by PageController.";

        //reset the current pages
        for(int i = 0; i < pages.size(); i++) {
            goToAnyPage(i);
            getCurrentPage().setWidgetsInvisible();
            deletePageAtIndex(i);
        }

        try {
            LayoutData data = nodeToObject(object, LayoutData.class);

            // iterate the pages
            for(int i = 0; i < data.widgets().length; i++) {
                ArrayList<AbstractWidgetController> page = new ArrayList();

                // process all widgets for this page
                for(WidgetData widget : data.widgets()[i]) {

                    switch (widget.name()) {
                        case "calendar" -> {
                            CalendarWidgetController calendarWidgetController = new CalendarWidgetController();
                            calendarWidgetController.getWidget().setPosition(widget.x(), widget.y());
                            page.add(calendarWidgetController);
                        }
                        case "clock" -> {
                            // todo : FaceType handling
                            ClockWidgetController clockWidgetController = new ClockWidgetController(ClockFaceType.ANALOG);
                            clockWidgetController.getWidget().setPosition(widget.x(), widget.y());
                            page.add(clockWidgetController);
                        }
                        case "mail" -> {
                            EmailNotificationController emailNotificationController = new EmailNotificationController();
                            emailNotificationController.getWidget().setPosition(widget.x(), widget.y());
                            System.out.printf("mail x: %d\n", widget.x());
                            System.out.printf("mail y: %d\n", widget.y());
                            page.add(emailNotificationController);
                        }
                        case "reminder" -> {
                            TodosWidgetController todosWidgetController = new TodosWidgetController();
                            todosWidgetController.getWidget().setPosition(widget.x(), widget.y());
                            page.add(todosWidgetController);
                        }
                        case "weather" -> {
                            WeatherWidgetController weatherWidgetController = new WeatherWidgetController();
                            weatherWidgetController.getWidget().setPosition(widget.x(), widget.y());
                            System.out.printf("weather x: %d\n", widget.x());
                            System.out.printf("weather y: %d\n", widget.y());
                            page.add(weatherWidgetController);
                        }
                    }
                }
                addPageAtIndex(i, page);
            }

            pageIndex = 0;
            getCurrentPage().setWidgetsVisible();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public AbstractWidget getWidget() {
        return null;
    }
}