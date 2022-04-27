package de.hhn.aib.labsw.blackmirror.view.widgets.clock.ClockFaces;

import de.hhn.aib.labsw.blackmirror.view.widgets.clock.ClockFace;

import javax.swing.*;
import java.awt.*;
import java.time.LocalTime;

/**
 * Analog clock widget Black Mirror
 * Help: https://github.com/nguyenphu27/Java-Project/tree/master/ClockGui
 *
 * @author Lukas Michalsky, Luis Gutzeit
 * @version 2022-04-27
 * 2022-04-19: fixed error with stuttering.
 * 2022-04-27: completly rewrote the clock face from scratch
 */

public class AnalogClock implements ClockFace {

    @Override
    public JPanel getClockFace() {
        return new DrawPanel();
    }

    static class DrawPanel extends JPanel {
        private int size;
        private int centerX;
        private int centerY;

        public DrawPanel() {
            this.setBackground(Color.BLACK);
        }

        //draw the new Image
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            centerX = getWidth() / 2;
            centerY = getHeight() / 2;
            size = Math.min(getWidth(), getHeight());

            if (g instanceof Graphics2D g2d) {
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            }

            drawNumbers(g);
            drawLines(g);
            drawHourHand(g);
            drawMinuteHand(g);
            drawSecondsHand(g);
            drawCenterPoint(g);
        }

        private void drawCenterPoint(Graphics g) {
            int diameter = (int) (size * 0.075);
            g.setColor(Color.GRAY);
            g.fillOval(centerX - diameter / 2, centerY - diameter / 2, diameter, diameter);
        }

        private void drawHourHand(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            int value = LocalTime.now().getHour();
            double handLength = (size / 2.0) * 0.5;
            int x2 = (int) (Math.sin((value / 12.0) * 2 * Math.PI) * handLength);
            int y2 = (int) (Math.cos((value / 12.0) * 2 * Math.PI) * handLength);
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(4));
            g2.drawLine(centerX, centerY, centerX + x2, centerY - y2);
        }

        private void drawMinuteHand(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            int value = LocalTime.now().getMinute();
            double handLength = (size / 2.0) * 0.6;
            int x2 = (int) (Math.sin((value / 60.0) * 2 * Math.PI) * handLength);
            int y2 = (int) (Math.cos((value / 60.0) * 2 * Math.PI) * handLength);
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(3));
            g2.drawLine(centerX, centerY, centerX + x2, centerY - y2);
        }

        private void drawSecondsHand(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            int value = LocalTime.now().getSecond();
            double handLength = (size / 2.0) * 0.7;
            int x2 = (int) (Math.sin((value / 60.0) * 2 * Math.PI) * handLength);
            int y2 = (int) (Math.cos((value / 60.0) * 2 * Math.PI) * handLength);
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2));
            g2.drawLine(centerX, centerY, centerX + x2, centerY - y2);
        }

        private void drawLines(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            double lineStart = (size / 2.0) * 0.75;
            double lineStartHour = (size / 2.0) * 0.7;
            double lineEnd = (size / 2.0) * 0.8;
            for (int i = 0; i < 60; i++) {
                int x1, y1;
                if (i % 5 == 0) {
                    x1 = (int) (Math.sin((i / 60.0) * 2 * Math.PI) * lineStartHour);
                    y1 = (int) (Math.cos((i / 60.0) * 2 * Math.PI) * lineStartHour);
                } else {
                    x1 = (int) (Math.sin((i / 60.0) * 2 * Math.PI) * lineStart);
                    y1 = (int) (Math.cos((i / 60.0) * 2 * Math.PI) * lineStart);
                }
                int x2 = (int) (Math.sin((i / 60.0) * 2 * Math.PI) * lineEnd);
                int y2 = (int) (Math.cos((i / 60.0) * 2 * Math.PI) * lineEnd);

                g2.setColor(Color.GRAY);
                g2.drawLine(centerX + x1, centerY - y1, centerX + x2, centerY - y2);
            }
        }

        private void drawNumbers(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            double numberPos = (size / 2.0) * 0.85;
            for (int i = 1; i <= 12; i++) {
                String num = Integer.toString(i);
                int x = (int) (Math.sin((i / 12.0) * 2 * Math.PI) * numberPos);
                int y = (int) (Math.cos((i / 12.0) * 2 * Math.PI) * numberPos);

                g2.setFont(new Font("calibri", Font.PLAIN, (int) (size*0.075)));
                g2.setColor(Color.GRAY);

                double numXCorrection = (Math.sin((i / 12.0) * 2 * Math.PI) * (g2.getFontMetrics().stringWidth(num) / 2.0))-(g2.getFontMetrics().stringWidth(num) / 2.0);
                double numYCorrection = (Math.cos((i / 12.0) * 2 * Math.PI) * (g2.getFontMetrics().getAscent() / 2.0))-(g2.getFontMetrics().getAscent() / 2.0);

                g2.drawString(num, centerX + (x + (int) numXCorrection), centerY - (y + (int) numYCorrection));
            }
        }
    }
}
