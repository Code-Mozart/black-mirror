package de.hhn.aib.labsw.blackmirror.view.widgets;

import de.hhn.aib.labsw.blackmirror.controller.widgets.*;
import de.hhn.aib.labsw.blackmirror.view.widgets.clock.ClockFaceType;
import de.hhn.aib.labsw.blackmirror.view.widgets.clock.ClockWidget;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class WidgetControllersTest {

    /**
     * User can create an instance of a CalendarWidget by using the CalendarWidgetController.
     *
     * @result fails if return value is either null or no instance of a CalendarWidget.
     */
    @Test
    void testCalendarWidget() {
        CalendarWidgetController calendar = new CalendarWidgetController();
        Assertions.assertNotNull(calendar.getWidget(),
                "getWidget() should not return null");
        Assertions.assertTrue(calendar.getWidget() instanceof CalendarWidget,
                "CalendarWidgetController.getWidget() should return a CalendarWidget");
    }

    /**
     * User can create an instance of a EmailNotificationWidget by using the EmailNotificationController.
     *
     * @result fails if return value is either null or no instance of an EmailNotificationWidget.
     */
    @Test
    void testEmailNotificationWidget() {
        EmailNotificationController emailNotifier = new EmailNotificationController();
        Assertions.assertNotNull(emailNotifier.getWidget(),
                "getWidget() should not return null");
        Assertions.assertTrue(emailNotifier.getWidget() instanceof EmailNotificationWidget,
                "EmailNotificationController.getWidget() should return a EmailNotificationWidget");
    }

    /**
     * User can create an instance of a ClockWidget by using the ClockWidgetController.
     *
     * @result fails if return value is either null or no instance of an ClockWidget.
     */
    @Test
    void testClockWidget() {
        ClockWidgetController clock = new ClockWidgetController(ClockFaceType.DIGITAL);
        Assertions.assertNotNull(clock.getWidget(),
                "getWidget() should not return null");
        Assertions.assertTrue(clock.getWidget() instanceof ClockWidget,
                "ClockWidgetController.getWidget() should return a ClockWidget");
    }

    /**
     * User can create an instance of a TodosWidget by using the TodosWidgetController.
     *
     * @result fails if return value is either null or no instance of an TodosWidget.
     */
    @Test
    void testReminderWidget() {
        TodosWidgetController reminder = new TodosWidgetController();
        Assertions.assertNotNull(reminder.getWidget(),
                "getWidget() should not return null");
        Assertions.assertTrue(reminder.getWidget() instanceof TodosWidget,
                "ReminderWidgetController.getWidget() should return a ReminderWidget");
    }

    /**
     * User can create an instance of a WeatherWidget by using the WeatherWidgetController.
     *
     * @result fails if return value is either null or no instance of an WeatherWidget.
     */
    @Test
    void testWeatherWidget() {
        WeatherWidgetController reminder = new WeatherWidgetController();
        Assertions.assertNotNull(reminder.getWidget(),
                "getWidget() should not return null");
        Assertions.assertTrue(reminder.getWidget() instanceof WeatherWidget,
                "WeatherWidgetController.getWidget() should return a WeatherWidget");
    }

}
