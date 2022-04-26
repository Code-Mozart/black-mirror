package de.hhn.aib.labsw.blackmirror.view.widgets.clock.ClockFaces;

import de.hhn.aib.labsw.blackmirror.view.widgets.clock.ClockFace;

import javax.swing.*;
import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.format.TextStyle;
import java.util.Locale;

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
            g.setFont(new Font("Arial", Font.PLAIN,46));
            String time = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).format(LocalTime.now());
            if(LocalTime.now().getSecond() % 2 == 0){
                time = time.replace(":"," ");
            }
            g.drawString(time,(getWidth()/2)-(g.getFontMetrics().stringWidth(time)/2),(getHeight()/2)+(g.getFontMetrics().getHeight()/2));
        }
    }
}
