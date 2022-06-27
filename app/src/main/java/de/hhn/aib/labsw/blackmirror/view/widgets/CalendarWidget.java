package de.hhn.aib.labsw.blackmirror.view.widgets;

import javax.swing.*;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Calendar widget displaying the date and an overview of the coming week.
 *
 * @author Markus Marewitz
 * @author Niklas Binder
 * @version 2022-06-27
 */
public class CalendarWidget extends AbstractWidget {
    private List<CalendarDayComponent> dayComponents;

    public CalendarWidget() {
        //this.setSize(500, 100);
        initComponents();
        update();
    }

    /**
     * Checks every second if it is the next day in which case it updates the UI.
     */
    public void update() {
        ZonedDateTime now = ZonedDateTime.now();
        int i = 0;
        for (CalendarDayComponent cdc : dayComponents) {
            cdc.setDay(now.plusDays(i++));
        }
    }

    private void initComponents() {
        JPanel panelMain = new JPanel(new GridBagLayout());
        panelMain.setBackground(Color.BLACK);
        panelMain.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        GridBagConstraints mainConstraints = new GridBagConstraints();
        mainConstraints.weightx = 1;
        mainConstraints.weighty = 1;
        mainConstraints.gridwidth = 3;
        mainConstraints.gridheight = 3;

        dayComponents = new ArrayList<>();

        CalendarDayComponent cdc = new CalendarDayComponent(true);
        dayComponents.add(cdc);

        mainConstraints.gridx = 0;
        mainConstraints.gridy = 0;
        mainConstraints.fill = GridBagConstraints.BOTH;
        panelMain.add(cdc, mainConstraints);

        mainConstraints.weightx = 0.4;
        mainConstraints.weighty = 0.4;
        mainConstraints.gridwidth = 1;
        mainConstraints.gridheight = 1;
        mainConstraints.gridx = 3;
        for (int i = 0; i < 3; i++) {
            cdc = new CalendarDayComponent(false);
            dayComponents.add(cdc);
            mainConstraints.gridy = i;
            panelMain.add(cdc, mainConstraints);
        }
        this.add(panelMain);
    }

    /**
     * A sheet for a single calendar day.
     */
    private static class CalendarDayComponent extends JPanel {
        private final JLabel dateLabel;
        private final JLabel dayLabel;

        private final String fontName = "Calibri";
        private final int fontStyle = Font.PLAIN;
        private int dayFontSize = 40;
        private int dateFontSize = 50;

        private final boolean isCurrentDay;

        /**
         * @param isCurrentDay Indicates if the {@link CalendarDayComponent} is the current day.
         *                     If so, the fonts are bigger and the day will be shown in full length.
         */
        public CalendarDayComponent(boolean isCurrentDay) {
            this.isCurrentDay = isCurrentDay;

            this.setBackground(Color.BLACK);
            this.setLayout(new BorderLayout());
            this.setBorder(BorderFactory.createLineBorder(Color.WHITE));

            if (isCurrentDay) {
                dayFontSize = 70;
                dateFontSize = 220;
            }
            dayLabel = new JLabel();
            dayLabel.setForeground(Color.WHITE);
            dayLabel.setHorizontalAlignment(JLabel.CENTER);
            dayLabel.setFont(new Font(fontName, fontStyle, dayFontSize));
            this.add(dayLabel, BorderLayout.NORTH);

            dateLabel = new JLabel();
            dateLabel.setForeground(Color.WHITE);
            dateLabel.setHorizontalAlignment(JLabel.CENTER);
            dateLabel.setFont(new Font(fontName, fontStyle, dateFontSize));
            this.add(dateLabel, BorderLayout.CENTER);
        }

        /**
         * Sets the day this sheet displays and updates the UI respectively.
         *
         * @param date The date of the day that should be displayed by this sheet.
         */
        public void setDay(ZonedDateTime date) {
            if (isCurrentDay) {
                dayLabel.setText(date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault()));
            } else {
                dayLabel.setText(date.getDayOfWeek().getDisplayName(TextStyle.SHORT_STANDALONE, Locale.getDefault()));
            }
            if(date.getDayOfWeek() == DayOfWeek.SUNDAY){
                dayLabel.setForeground(new Color(255,65,0));
                dateLabel.setForeground(new Color(255,65,0));
            }
            dateLabel.setText(String.valueOf(date.getDayOfMonth()));
        }
    }
}
