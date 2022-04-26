package de.hhn.aib.labsw.blackmirror.view.widgets.clock;

import de.hhn.aib.labsw.blackmirror.view.widgets.AbstractWidget;
import de.hhn.aib.labsw.blackmirror.view.widgets.clock.ClockFaces.AnalogClock;
import de.hhn.aib.labsw.blackmirror.view.widgets.clock.ClockFaces.DigitalClock;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ClockWidget extends AbstractWidget {
    ClockFace face;
    JPanel panel;

    public ClockWidget(ClockFaceType type){
        setBounds(700, 100, 400, 300);
        getContentPane().setBackground(Color.BLACK);

        setType(type);
        this.add(panel);

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(this::update, 0, 500, TimeUnit.MILLISECONDS);
    }

    private void update(){
        this.repaint();
    }

    public void setType(ClockFaceType type){
        face = switch(type) {
            case ANALOG -> new AnalogClock();
            case DIGITAL -> new DigitalClock();
            default -> null;
        };
        panel = face.getClockFace();
    }
}
