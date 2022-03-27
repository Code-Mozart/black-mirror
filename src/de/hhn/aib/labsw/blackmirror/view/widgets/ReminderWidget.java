package de.hhn.aib.labsw.blackmirror.view.widgets;

import javax.swing.*;
import java.awt.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReminderWidget extends AbstractWidget {
    private JLabel label;
    private ZonedDateTime now;
    private JList<String> list;
    private Calendar calendar;
    private List<Event> events;

    public ReminderWidget() {
        this.setSize(600, 400);
        label = new JLabel();
        label.setText("Reminders");
        initComponents();
    }

    @Override
    public void onNextSecond() {
        now = ZonedDateTime.now();
    }

    private void initComponents() {
        JPanel panelMain = new JPanel(new BorderLayout());
        panelMain.setBackground(Color.BLACK);

        label = new JLabel("Reminders");
        label.setForeground(Color.WHITE);

        JPanel panel = new JPanel();
        events = new ArrayList<>();

        // Hardcoded Events
        Event e1 = new Event(ZonedDateTime.of(2022, 8, 12, 8, 30, 0,
                0, ZoneId.of("Europe/Berlin")), "Meeting");
        Event e2 = new Event(ZonedDateTime.of(2022, 8, 12, 10, 30, 0,
                0, ZoneId.of("Europe/Berlin")), "Meeting");
        events.add(e1);
        events.add(e2);

        List eventsStr = new ArrayList<>();
        for (Event ev :
                events) {
            eventsStr.add(parseEvent(ev));
        }

        list = new JList(eventsStr.toArray());
        list.setBackground(Color.BLACK);
        list.setForeground(Color.WHITE);

        panel.add(list);
        panel.setBackground(Color.BLACK);

        panelMain.add(label, BorderLayout.NORTH);
        panelMain.add(panel, BorderLayout.CENTER);
        this.add(panelMain);
    }

    private void getTodayEvents() {
        //TODO: Get Events by requesting them over Calendar
    }

    private String parseEvent(Event e) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm");
        return formatter.format(e.getTime()) + " - " + e.getDesc();
    }
}

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


