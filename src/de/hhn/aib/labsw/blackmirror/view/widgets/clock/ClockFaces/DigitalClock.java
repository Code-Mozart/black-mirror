package de.hhn.aib.labsw.blackmirror.view.widgets.clock.ClockFaces;

import de.hhn.aib.labsw.blackmirror.view.widgets.clock.ClockFace;

import javax.swing.*;
import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.format.TextStyle;

public class DigitalClock implements ClockFace {
    @Override
    public JPanel getClockFace() {
        return new ClockPanel();
    }

    static class ClockPanel extends JPanel{
        public ClockPanel() {
            this.setBackground(Color.BLACK);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.WHITE);
            String time = DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM).format(LocalTime.now());
            g.setFont(new Font("Calibri", Font.PLAIN,40));
            g.drawString(time,(getWidth()/2)-(getFontMetrics(getFont()).stringWidth(time)),(getHeight()/2)-(getFontMetrics(getFont()).getHeight()/2));
        }
    }
}
