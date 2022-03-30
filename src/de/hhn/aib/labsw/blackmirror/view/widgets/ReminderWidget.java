package de.hhn.aib.labsw.blackmirror.view.widgets;

import javax.swing.*;
import java.awt.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.List;

/**
 * This class represents a widget used for showing reminders on the users mirror for events that are going to happen
 * on the current day.
 *
 * @author Selim Özdemir
 * @version 30-03-2022
 */
public class ReminderWidget extends AbstractWidget {
    private JLabel label;
    private ZonedDateTime now;
    private JList<String> list;
    private Calendar calendar;
    private List<Event> events;


    public ReminderWidget() {
        this.setSize(200, 200);
        label = new JLabel();
        label.setText("Reminders");
        initComponents();
    }

    @Override
    public void onNextSecond() {
        now = ZonedDateTime.now();
    }

    /**
     * Initiates all UI-components such as the label and the list.
     */
    private void initComponents() {
        JPanel panelMain = new JPanel(new BorderLayout());
        panelMain.setBackground(Color.BLACK);

        label = new JLabel("Todays events:");
        label.setForeground(Color.WHITE);

        JPanel panel = new JPanel();
        events = new ArrayList<>();

        // Hardcoded Events
        Event e1 = new Event(ZonedDateTime.of(2022, 8, 12, 8, 30, 0,
                0, ZoneId.of("Europe/Berlin")), "Meeting");
        Event e2 = new Event(ZonedDateTime.of(2022, 8, 12, 10, 30, 0,
                0, ZoneId.of("Europe/Berlin")), "Meeting");
        events.add(e2);
        events.add(e1);
        events.sort(new EventComparator());

        if(!events.isEmpty()) {
            int i = 0;
            List eventsStr = new ArrayList<>();
            for (Event ev :
                    events) {
                if (i<6) {
                    eventsStr.add(parseEvent(ev));
                    i++;
                }
            }

            list = new JList(eventsStr.toArray());
            list.setBackground(Color.BLACK);
            list.setForeground(Color.WHITE);

            panel.add(list);
        } else {

        }
        panel.setBackground(Color.BLACK);

        panelMain.add(label, BorderLayout.NORTH);
        panelMain.add(panel, BorderLayout.CENTER);
        this.add(panelMain);
    }

    /**
     * Requests all Events and adds them to a list if they are on the same date.
     */
    private void getTodayEvents() {
        ArrayList<Event> requestedEvents = new ArrayList<>();
        //TODO: Get Events by requesting them over Calendar (needs to be done after app is finished)

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy,MM,dd");
        for (Event ev: requestedEvents) {
            if (formatter.format(ev.getTime()).equals(formatter.format(now))) {
                events.add(ev);
            }
        }
    }

    /**
     * Formats a time given with an event to a time String
     * @param e Event
     * @return the time of the event e as a String ("hh:mm")
     */
    private String parseEvent(Event e) {
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)
                .withLocale(Locale.getDefault()).ofPattern("hh:mm");
        return formatter.format(e.getTime()) + " - " + e.getDesc();
    }
}

/**
 * Event class used for reminders.
 */
class Event {
    private ZonedDateTime time;
    private String desc;

    public Event(ZonedDateTime time, String description) {
        this.time = time;
        desc = description;
    }

    public ZonedDateTime getTime() {
        return time;
    }

    public String getDesc() {
        return desc;
    }
}

/**
 * Used as a sorting algorithm for events.
 */
class EventComparator implements Comparator<Event> {
    public int compare(Event e1, Event e2) {
        if (e1.getTime() == e2.getTime()) {
            return 0;
        } else if (e1.getTime().isBefore(e2.getTime())) {
            return -1;
        } else {
            return 1;
        }
    }
}


