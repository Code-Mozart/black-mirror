package de.hhn.aib.labsw.blackmirror.view.widgets;

import de.hhn.aib.labsw.blackmirror.util.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Calendar widget displaying the date and an overview of the coming week.
 *
 * @author Markus Marewitz
 * @version 2022-03-24
 */
public class CalendarWidget extends AbstractWidget {
    private JLabel label;
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
        DateTimeFormatter formatter = DateTimeFormatter
                .ofLocalizedDate(FormatStyle.FULL)
                .withLocale(Locale.getDefault());
        String dateString = formatter.format(now);
        int i = 0;
        for (CalendarDayComponent cdc : dayComponents) {
            cdc.setDay(now.plusDays(i++));
        }
    }

    private void initComponents() {
        JPanel panelMain = new JPanel(new GridBagLayout());
        panelMain.setBackground(Color.BLACK);
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

        mainConstraints.weightx = 0.2;
        mainConstraints.weighty = 0.2;
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

        SwingUtils.setFont(this, new Font("Calibri", Font.PLAIN, 18));
    }

    /**
     * A sheet for a single calendar day.
     */
    private static class CalendarDayComponent extends JPanel {
        private final JLabel dateLabel;
        private final JLabel dayLabel;

        /**
         * @param thick Sets whether the border should be thick or thin.
         */
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

        /**
         * Sets the day this sheet displays and updates the UI respectively.
         *
         * @param date The date of the day that should be displayed by this sheet.
         */
        public void setDay(ZonedDateTime date) {
            dayLabel.setText(date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.getDefault()));
            if(date.getDayOfWeek() == DayOfWeek.SUNDAY){
                dayLabel.setForeground(new Color(255,65,0));
                dateLabel.setForeground(new Color(255,65,0));
            }
            dateLabel.setText(String.valueOf(date.getDayOfMonth()));
        }
    }
}
