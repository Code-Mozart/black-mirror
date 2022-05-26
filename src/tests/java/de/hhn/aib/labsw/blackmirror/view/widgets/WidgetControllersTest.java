package de.hhn.aib.labsw.blackmirror.view.widgets;

import de.hhn.aib.labsw.blackmirror.controller.widgets.*;
import de.hhn.aib.labsw.blackmirror.view.widgets.clock.ClockFaceType;
import de.hhn.aib.labsw.blackmirror.view.widgets.clock.ClockWidget;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class WidgetControllersTest {

    @Test
    void testCalendarWidget() {
        CalendarWidgetController calendar = new CalendarWidgetController();
        Assertions.assertNotNull(calendar.getWidget(),
                "getWidget() should not return null");
        Assertions.assertTrue(calendar.getWidget() instanceof CalendarWidget,
                "CalendarWidgetController.getWidget() should return a CalendarWidget");
    }

    @Test
    void testEmailNotificationWidget() {
        EmailNotificationController emailNotifier = new EmailNotificationController();
        Assertions.assertNotNull(emailNotifier.getWidget(),
                "getWidget() should not return null");
        Assertions.assertTrue(emailNotifier.getWidget() instanceof EmailNotificationWidget,
                "EmailNotificationController.getWidget() should return a EmailNotificationWidget");
    }

    @Test
    void testClockWidget() {
        ClockWidgetController clock = new ClockWidgetController(ClockFaceType.DIGITAL);
        Assertions.assertNotNull(clock.getWidget(),
                "getWidget() should not return null");
        Assertions.assertTrue(clock.getWidget() instanceof ClockWidget,
                "ClockWidgetController.getWidget() should return a ClockWidget");
    }

    @Test
    void testReminderWidget() {
        ReminderWidgetController reminder = new ReminderWidgetController();
        Assertions.assertNotNull(reminder.getWidget(),
                "getWidget() should not return null");
        Assertions.assertTrue(reminder.getWidget() instanceof ReminderWidget,
                "ReminderWidgetController.getWidget() should return a ReminderWidget");
    }

    @Test
    void testWeatherWidget() {
        WeatherWidgetController reminder = new WeatherWidgetController();
        Assertions.assertNotNull(reminder.getWidget(),
                "getWidget() should not return null");
        Assertions.assertTrue(reminder.getWidget() instanceof WeatherWidget,
                "WeatherWidgetController.getWidget() should return a WeatherWidget");
    }

}
