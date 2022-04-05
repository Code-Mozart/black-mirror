package de.hhn.aib.labsw.blackmirror.view.widgets;

import java.awt.*;
import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Analog clock widget Black Mirror
 * <p>
 * Help: https://github.com/nguyenphu27/Java-Project/tree/master/ClockGui
 *
 * @author Lukas Michalsky
 * @version 2022-04-05
 */

public class ClockWidget extends AbstractWidget {
    private static final int spacing = 35;
    private static final float radPerSecMin = (float) (Math.PI / 30.0);
    private static final float radPerNum = (float) (Math.PI / -6);
    private int size;
    private int centerX;
    private int centerY;

    Calendar cal;
    int hour;
    int minute;
    int second;
    Color color;


    Runnable refresh = new Runnable() {
        @Override
        public void run() {
            cal = Calendar.getInstance();
            repaint();
        }
    };

    /**
     * Constructor clock widget
     */
    public ClockWidget() {

        setBounds(700, 100, 400, 430);
        getContentPane().setBackground(Color.BLACK);

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(refresh, 0, 1, TimeUnit.SECONDS);
    }

    /**
     * Method drawing clock
     *
     * @param g drawing on this graphic
     */
    @Override
    public void paint(Graphics g) {

        super.paint(g);

        if (g instanceof Graphics2D) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }

        //border clock
        g.setColor(Color.BLACK);
        g.fillOval(35, spacing + 10, 330, 330);


        size = 400 - spacing;
        centerX = 400 / 2;
        centerY = 400 / 2 + 10;

        //clock face
        drawClockFace(g);

        //number clock face
        drawNumberClock(g);

        //get system time
        cal = Calendar.getInstance();
        hour = cal.get(Calendar.HOUR);
        minute = cal.get(Calendar.MINUTE);
        second = cal.get(Calendar.SECOND);

        //draw hands
        drawHands(g, hour, minute, second, color);

        //draw point clock
        g.setColor(Color.WHITE);
        g.fillOval(centerX - 5, centerY - 5, 10, 10);
        g.setColor(Color.WHITE);
        g.fillOval(centerX - 3, centerY - 3, 6, 6);

    }

    /**
     * Methode drawing clock face
     *
     * @param g drawing on this graphic
     */
    private void drawClockFace(Graphics g) {

        // tick marks
        for (int sec = 0; sec < 60; sec++) {
            int ticStart;
            if (sec % 5 == 0) {
                ticStart = size / 2 - 10;
            } else {
                ticStart = size / 2 - 5;
            }
            drawRadius(g, centerX, centerY, radPerSecMin * sec, ticStart - 20, size / 2 - 20, color.WHITE);

        }
    }

    /**
     * Methode drawing size of clock face
     *
     * @param g           drawing on this graphic
     * @param x           x-value middle of clock
     * @param y           y-value middle of clock
     * @param angle       angle of second marks
     * @param minRadius   start point of second marks
     * @param maxRadius   end point of second marks
     * @param colorNumber color of clock face border
     */
    private void drawRadius(Graphics g, int x, int y, double angle,
                            int minRadius, int maxRadius, Color colorNumber) {
        float sine = (float) Math.sin(angle);
        float cosine = (float) Math.cos(angle);
        int dxmin = (int) (minRadius * sine);
        int dymin = (int) (minRadius * cosine);
        int dxmax = (int) (maxRadius * sine);
        int dymax = (int) (maxRadius * cosine);
        g.setColor(colorNumber);
        g.drawLine(x + dxmin, y + dymin, x + dxmax, y + dymax);
    }


    /**
     * Methode drawing numbers of the clock
     *
     * @param g drawing on this graphic
     */
    private void drawNumberClock(Graphics g) {

        for (int num = 12; num > 0; num--) {
            drawnum(g, radPerNum * num, num);
        }
    }

    /**
     * Methode drawing numbers on the right point on the clock face
     *
     * @param g     drawing on this graphic
     * @param angle angle position of number
     * @param n     number for clock face position
     */
    private void drawnum(Graphics g, float angle, int n) {

        float sine = (float) Math.sin(angle);
        float cosine = (float) Math.cos(angle);
        int dx = (int) ((size / 2 - 20 - 25) * -sine);
        int dy = (int) ((size / 2 - 20 - 25) * -cosine);

        g.drawString("" + n, dx + centerX - 5, dy + centerY + 5);
    }


    /**
     * Methode drawing clock hands
     *
     * @param g      drawing on this graphic
     * @param hour   current hour
     * @param minute current minute
     * @param second current second
     * @param color  color of clock hands
     */
    private void drawHands(Graphics g, double hour, double minute, double second, Color color) {

        double rsecond = (second * 6) * (Math.PI) / 180;
        double rminute = ((minute + (second / 60)) * 6) * (Math.PI) / 180;
        double rhours = ((hour + (minute / 60)) * 30) * (Math.PI) / 180;

        g.setColor(color);
        g.drawLine(centerX, centerY, centerX + (int) (150 * Math.cos(rsecond - (Math.PI / 2))), centerY + (int) (150 * Math.sin(rsecond - (Math.PI / 2))));
        g.setColor(color);
        g.drawLine(centerX, centerY, centerX + (int) (120 * Math.cos(rminute - (Math.PI / 2))), centerY + (int) (120 * Math.sin(rminute - (Math.PI / 2))));
        g.drawLine(centerX, centerY, centerX + (int) (90 * Math.cos(rhours - (Math.PI / 2))), centerY + (int) (90 * Math.sin(rhours - (Math.PI / 2))));
    }

}
