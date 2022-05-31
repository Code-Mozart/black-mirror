package de.hhn.aib.labsw.blackmirror.view.widgets.clock.ClockFaces;

import de.hhn.aib.labsw.blackmirror.view.widgets.clock.ClockFace;

import javax.swing.*;
import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class DigitalClock implements ClockFace {
    @Override
    public JPanel getClockFace() {
        return new ClockPanel();
    }

    static class ClockPanel extends JPanel {
        public ClockPanel() {
            this.setBackground(Color.BLACK);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.WHITE);
            String time = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).format(LocalTime.now());
            int size = 5;
            while (true) {
                g.setFont(new Font("Arial", Font.PLAIN, size));
                if (g.getFontMetrics().stringWidth(time) > (getWidth() * 0.75) ||
                        g.getFontMetrics().getHeight() > (getHeight() * 0.75)) {
                    break;
                } else {
                    size++;
                }
            }

            if (LocalTime.now().getSecond() % 2 == 0) {
                time = time.replace(":", " ");
            }
            g.drawString(time, (getWidth() / 2) - (g.getFontMetrics().stringWidth(time) / 2), (getHeight() / 2) + (g.getFontMetrics().getAscent() / 2));
        }
    }
}
