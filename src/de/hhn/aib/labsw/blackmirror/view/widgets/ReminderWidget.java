package de.hhn.aib.labsw.blackmirror.view.widgets;

import javax.swing.*;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.time.LocalDate;
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
    private List<Reminder> reminders;

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
        ImageIcon remindIcon = new ImageIcon(getClass().getResource("/icons/reminder_icon.jpg"));
        iconLabel.setIcon(remindIcon);

        reminderLabel.setVerticalAlignment(SwingConstants.NORTH);

        panelMain.add(iconLabel, c);
        c.gridy = 1;
        panelMain.add(reminderLabel, c);

        reminderLabel.setHorizontalAlignment(JLabel.CENTER);
        reminders = new ArrayList<>();

        // Hardcoded Events
        Reminder r1 = new Reminder(LocalDate.now(), "Daily Scrum um 14 Uhr");
        Reminder r2 = new Reminder(LocalDate.now(), "Programmieren usw.");

        reminders.add(r2);
        reminders.add(r1);

        c.gridy = 2;
        if (!reminders.isEmpty()) {
            int i = 0;
            List<String> reminderStr = new ArrayList<>();
            for (Reminder r :
                    reminders) {
                if (i < 8) {
                    reminderStr.add(parseReminder(r));
                    i++;
                }
            }

            if (i==6) {
                reminderStr.add("...");
            }

            JList list = new JList(reminderStr.toArray());
            list.setBackground(Color.BLACK);
            list.setForeground(Color.WHITE);
            list.setFont(new Font(list.getFont().getName(), list.getFont().getStyle(), 13));

            panelMain.add(list, c);
        } else {
            JLabel noRemindersText = new JLabel(resources.getString("noReminders"));
            noRemindersText.setBackground(Color.BLACK);
            noRemindersText.setForeground(Color.WHITE);

            panelMain.add(noRemindersText, c);
        }
        this.add(panelMain);
    }

    /**
     * Requests all Reminders and adds them to a list if they are on the same date.
     */
    private void getTodaysReminders() {
        ArrayList<Reminder> requestedReminders = new ArrayList<>();
        //TODO: Get Events by requesting them over Calendar (needs to be done after app is finished)

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy,MM,dd");
        for (Reminder r : requestedReminders) {
            if (formatter.format(r.getDate()).equals(formatter.format(now))) {
                reminders.add(r);
            }
        }
    }

    /**
     * Formats a time given with an event to a time String
     *
     * @param r Reminder
     * @return parsed reminder information as a String
     */
    private String parseReminder(Reminder r) {
        String eventDesc = r.getDesc();
        if (eventDesc.length() > 30) {
            eventDesc = eventDesc.substring(0, 29) + "...";
        }
        return "- "+r.getDesc();
    }
}

/**
 * Reminder class used for reminders.
 */
class Reminder {
    private final LocalDate date;
    private final String desc;

    public Reminder(LocalDate reminderDate, String description) {
        date = reminderDate;
        desc = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDesc() {
        return desc;
    }
}


