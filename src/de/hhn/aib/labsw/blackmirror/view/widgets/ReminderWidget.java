package de.hhn.aib.labsw.blackmirror.view.widgets;

import javax.swing.*;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

/**
 * This class represents a widget used for showing reminders on the users mirror for events that are going to happen
 * on the current day.
 *
 * @author Selim Özdemir
 * @version 03-04-2022
 */
public class ReminderWidget extends AbstractWidget {
    private JLabel reminderLabel;
    private ZonedDateTime now;
    private List<Event> events;

    private final ResourceBundle resources = ResourceBundle.getBundle("ReminderWidget", Locale.getDefault());

    public ReminderWidget() {
        this.setSize(300, 300);
        now = ZonedDateTime.now();
        initComponents();
    }

    @Override
    public void onNextSecond() {
        if (now.getDayOfMonth() != ZonedDateTime.now().getDayOfMonth()) {
            initComponents();
        }
        now = ZonedDateTime.now();
    }

    /**
     * Initiates all UI-components such as the label and the list.
     */
    private void initComponents() {
        JPanel panelMain = new JPanel(new BorderLayout());
        panelMain.setBackground(Color.BLACK);
        panelMain.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5,5,5,5);

        reminderLabel = new JLabel(resources.getString("reminderTitle"));
        reminderLabel.setFont(new Font(reminderLabel.getFont().getName(), Font.BOLD, 16));
        reminderLabel.setForeground(Color.WHITE);
        Font font = reminderLabel.getFont();
        Map a = font.getAttributes();
        a.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        reminderLabel.setFont(font.deriveFont(a));

        JLabel iconLabel = new JLabel("", SwingConstants.CENTER);
        // icon author @mallorysartwork
        ImageIcon remindIcon = new ImageIcon("icons/reminder_icon.jpg");
        iconLabel.setIcon(remindIcon);

        reminderLabel.setVerticalAlignment(SwingConstants.NORTH);

        panelMain.add(iconLabel, c);
        c.gridy = 1;
        panelMain.add(reminderLabel, c);

        reminderLabel.setHorizontalAlignment(JLabel.CENTER);
        events = new ArrayList<>();

        // Hardcoded Events
        Event e1 = new Event(ZonedDateTime.of(2022, 8, 12, 8, 30, 0,
                0, ZoneId.of("Europe/Berlin")), "Meetingnnnnnnnnnnnnnnnmnnnnnnnnnn");
        Event e2 = new Event(ZonedDateTime.of(2022, 8, 12, 10, 30, 0,
                0, ZoneId.of("Europe/Berlin")), "Meeting");
        Event e3 = new Event(ZonedDateTime.of(2022, 8, 12, 11, 30, 0,
                0, ZoneId.of("Europe/Berlin")), "Meeting");
        Event e4 = new Event(ZonedDateTime.of(2022, 8, 12, 12, 30, 0,
                0, ZoneId.of("Europe/Berlin")), "Meeting");
        Event e5 = new Event(ZonedDateTime.of(2022, 8, 12, 13, 30, 0,
                0, ZoneId.of("Europe/Berlin")), "Meeting");
        Event e6 = new Event(ZonedDateTime.of(2022, 8, 12, 14, 30, 0,
                0, ZoneId.of("Europe/Berlin")), "Meeting");
        Event e7 = new Event(ZonedDateTime.of(2022, 8, 12, 15, 30, 0,
                0, ZoneId.of("Europe/Berlin")), "Meeting");

        events.add(e2);
        events.add(e1);
        events.add(e3);
        events.add(e5);
        events.add(e4);
        events.add(e6);
        events.add(e7);

        events.sort(new EventComparator());

        c.gridy = 2;
        if (!events.isEmpty()) {
            int i = 0;
            List<String> eventsStr = new ArrayList<>();
            for (Event ev :
                    events) {
                if (i < 6) {
                    eventsStr.add(parseEvent(ev));
                    i++;
                }
            }

            if (i==6) {
                eventsStr.add("...");
            }

            JList list = new JList(eventsStr.toArray());
            list.setBackground(Color.BLACK);
            list.setForeground(Color.WHITE);
            list.setFont(new Font(list.getFont().getName(), list.getFont().getStyle(), 13));

            panelMain.add(list, c);
        } else {
            JLabel noEventsText = new JLabel(resources.getString("noEvents"));
            noEventsText.setBackground(Color.BLACK);
            noEventsText.setForeground(Color.WHITE);

            panelMain.add(noEventsText, c);
        }
        this.add(panelMain);
    }

    /**
     * Requests all Events and adds them to a list if they are on the same date.
     */
    private void getTodayEvents() {
        ArrayList<Event> requestedEvents = new ArrayList<>();
        //TODO: Get Events by requesting them over Calendar (needs to be done after app is finished)

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy,MM,dd");
        for (Event ev : requestedEvents) {
            if (formatter.format(ev.getTime()).equals(formatter.format(now))) {
                events.add(ev);
            }
        }
    }

    /**
     * Formats a time given with an event to a time String
     *
     * @param e Event
     * @return the time of the event e as a String ("hh:mm")
     */
    private String parseEvent(Event e) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault());
        String eventDesc = e.getDesc();
        if (eventDesc.length() > 30) {
            eventDesc = eventDesc.substring(0, 29) + "...";
        }
        return formatter.format(e.getTime()) + " - " + eventDesc;
    }
}

/**
 * Event class used for reminders.
 */
class Event {
    private final ZonedDateTime time;
    private final String desc;

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


