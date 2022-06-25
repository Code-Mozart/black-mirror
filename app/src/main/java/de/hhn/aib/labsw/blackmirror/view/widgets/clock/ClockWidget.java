package de.hhn.aib.labsw.blackmirror.view.widgets.clock;

import de.hhn.aib.labsw.blackmirror.view.widgets.AbstractWidget;
import de.hhn.aib.labsw.blackmirror.view.widgets.clock.ClockFaces.AnalogClock;
import de.hhn.aib.labsw.blackmirror.view.widgets.clock.ClockFaces.DigitalClock;

import javax.swing.*;
import java.awt.*;

/**
 * clock widget that can display ClockFaces
 *
 * @author Luis Gutzeit
 * @version 25.06.2022
 */
public class ClockWidget extends AbstractWidget {
    ClockFace face;
    JPanel panel;

    public ClockWidget(ClockFaceType type) {
        setBounds(700, 100, 400, 400);
        getContentPane().setBackground(Color.BLACK);

        setType(type);
        this.add(panel);
    }

    /**
     * update the clock face
     */
    public void update() {
        panel.repaint();
    }

    /**
     * set the type of the Clock Face
     *
     * @param type the type of ClockFace
     */
    public void setType(ClockFaceType type) {
        face = switch (type) {
            case ANALOG -> new AnalogClock();
            case DIGITAL -> new DigitalClock();
        };
        panel = face.getClockFace();
    }
}
