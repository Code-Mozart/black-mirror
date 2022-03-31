package de.hhn.aib.labsw.blackmirror.view.widgets;

import javax.swing.*;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author Markus Marewitz
 * @version 2022-03-24
 */
public class CalendarWidget extends AbstractWidget {
    private JLabel label;
    private List<CalendarDayComponent> dayComponents;

    public CalendarWidget() {
        this.setSize(500, 100);
        initComponents();
        onNextSecond();
    }

    @Override
    public void onNextSecond() {
        ZonedDateTime now = ZonedDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter
                .ofLocalizedDate(FormatStyle.FULL)
                .withLocale(Locale.getDefault());
        String dateString = formatter.format(now);
        label.setText(dateString);
        int i = 0;
        for (CalendarDayComponent cdc : dayComponents) {
            cdc.setDay(now.plusDays(i++));
        }
    }

    private void initComponents() {
        JPanel panelMain = new JPanel(new BorderLayout());
        panelMain.setBackground(Color.BLACK);

        label = new JLabel("Time");
        label.setForeground(Color.WHITE);

        dayComponents = new ArrayList<>();
        JPanel panelDays = new JPanel(new GridLayout(1, 7));
        for (int i = 0; i < 7; i++) {
            CalendarDayComponent cdc = new CalendarDayComponent(i == 0);
            dayComponents.add(cdc);
            panelDays.add(cdc);
        }

        panelMain.add(label, BorderLayout.NORTH);
        panelMain.add(panelDays, BorderLayout.CENTER);
        this.add(panelMain);
    }

    private static class CalendarDayComponent extends JPanel {
        private final JLabel dateLabel;
        private final JLabel dayLabel;

        public CalendarDayComponent(boolean thick) {
            this.setBackground(Color.BLACK);

            this.setLayout(new BorderLayout());
            if (thick)
                this.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
            else
                this.setBorder(BorderFactory.createLineBorder(Color.WHITE));

            JPanel panelLabels = new JPanel(new GridLayout(2, 1));
            panelLabels.setBackground(Color.BLACK);
            this.add(panelLabels, BorderLayout.NORTH);

            dateLabel = new JLabel("XX");
            dateLabel.setForeground(Color.WHITE);
            panelLabels.add(dateLabel);

            dayLabel = new JLabel("Xx");
            dayLabel.setForeground(Color.WHITE);
            panelLabels.add(dayLabel);
        }

        public void setDay(ZonedDateTime date) {
            dayLabel.setText(date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.getDefault()));
            dateLabel.setText(String.valueOf(date.getDayOfMonth()));
        }
    }
}
