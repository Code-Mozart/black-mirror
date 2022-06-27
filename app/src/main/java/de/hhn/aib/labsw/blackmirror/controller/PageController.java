package de.hhn.aib.labsw.blackmirror.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import de.hhn.aib.labsw.blackmirror.controller.API.MirrorApi;
import de.hhn.aib.labsw.blackmirror.controller.API.TopicListener;
import de.hhn.aib.labsw.blackmirror.controller.API.websockets.MirrorApiWebsockets;
import de.hhn.aib.labsw.blackmirror.controller.widgets.*;
import de.hhn.aib.labsw.blackmirror.model.ApiDataModels.LayoutData;
import de.hhn.aib.labsw.blackmirror.model.ApiDataModels.WidgetData;
import de.hhn.aib.labsw.blackmirror.view.widgets.clock.ClockFaceType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * PageController Class to hold all pages and provide all necessary methods.
 *
 * @author Niklas Binder
 * @author Markus Marewitz
 * @author Philipp Herda
 * @version 2022-05-11
 */
public class PageController implements TopicListener {

    private final MirrorApi api = MirrorApiWebsockets.getInstance();

    private ArrayList<Page> pages = new ArrayList<>();
    private static final String PAGE_UPDATE_TOPIC = "pageUpdate";

    private int pageIndex = 0;
    private boolean isStandby = false;

    public PageController() {
        new SecondsTimer(this::autoChangePageByTime);
        api.subscribe(PAGE_UPDATE_TOPIC, this);
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
            for (AbstractWidgetController c : pages.get(pageIndex).getWidgetsOnPage()) {
                c.getWidget().dispose();
            }
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
    public void goToNextPage() {
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
    public void goToPreviousPage() {
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
    public void changeMode() {
        isStandby = !isStandby;
        if (isStandby) {
            getCurrentPage().setWidgetsInvisible();
        } else {
            getCurrentPage().setWidgetsVisible();
        }
    }

    /**
     * Gets the current mode of the mirror
     *
     * @return true when the mirror is in standby, else false
     */
    public Boolean getMode() {
        return isStandby;
    }

    /**
     * @return Current visible page.
     */
    protected Page getCurrentPage() {
        if (pages.size() > 0 && pageIndex < pages.size()) {
            return pages.get(pageIndex);
        } else {
            return null;
        }
    }

    /**
     * Automatically changes once to default Pages depending on current time.
     * Time     Page
     * 08 AM     0
     * 16 AM     1
     */
    private void autoChangePageByTime() {
        // at least 2 pages have to be initialised beforehand for this method to work properly
        if (pages.size() >= 2) {
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

        assert (topic.equals(PAGE_UPDATE_TOPIC)) : "Wrong topic received by PageController.";

        //reset the current pages
        for (int i = pages.size() - 1; i > -1; i--) {
            deletePageAtIndex(i);
        }


        try {
            LayoutData data = TopicListener.nodeToObject(object, LayoutData.class);

            // iterate the pages
            for (int i = 0; i < data.pages().size(); i++) {
                //create new page
                ArrayList<AbstractWidgetController> page = new ArrayList<>();

                // process all widgets for this page
                if (data.pages().get(i).widgets().size() > 0) {
                    for (int j = 0; j < data.pages().get(i).widgets().size(); j++) {
                        WidgetData widget = data.pages().get(i).widgets().get(j);
                        switch (widget.type()) {
                            case CALENDAR -> {
                                CalendarWidgetController calendarWidgetController = new CalendarWidgetController();
                                calendarWidgetController.getWidget().setPosition(widget.x() - 1, widget.y() - 1);
                                page.add(calendarWidgetController);
                            }
                            case CLOCK -> {
                                // todo : FaceType handling
                                ClockWidgetController clockWidgetController = new ClockWidgetController(ClockFaceType.ANALOG);
                                clockWidgetController.getWidget().setPosition(widget.x() - 1, widget.y() - 1);
                                page.add(clockWidgetController);
                            }
                            case MAIL -> {
                                EmailNotificationController emailNotificationController = new EmailNotificationController();
                                emailNotificationController.getWidget().setPosition(widget.x() - 1, widget.y() - 1);
                                page.add(emailNotificationController);
                            }
                            case REMINDER -> {
                                TodosWidgetController todosWidgetController = new TodosWidgetController();
                                todosWidgetController.getWidget().setPosition(widget.x() - 1, widget.y() - 1);
                                page.add(todosWidgetController);
                            }
                            case WEATHER -> {
                                WeatherWidgetController weatherWidgetController = new WeatherWidgetController();
                                weatherWidgetController.getWidget().setPosition(widget.x() - 1, widget.y() - 1);
                                page.add(weatherWidgetController);
                            }
                        }
                    }
                    addPage(page);
                }
            }
            pageIndex = data.currentPageIndex();
            if(pageIndex >= pages.size()||pages.get(pageIndex).getWidgetsOnPage().size() == 0){
                int newPageIndex = 0;
                while(pages.get(newPageIndex).getWidgetsOnPage().size() == 0){
                    ++newPageIndex;
                }
                pageIndex = newPageIndex;
            }
            getCurrentPage().setWidgetsVisible();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}