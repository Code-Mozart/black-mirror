package de.hhn.aib.labsw.blackmirror.view.widgets;

import javax.swing.*;
import java.awt.*;

/**
 * Common parent class from which all widgets should be derived.
 *
 * @author Markus Marewitz
 * @version 2022-03-24
 */
public abstract class AbstractWidget extends JDialog {
    public static enum Position {
        TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT, CENTER
    }

    public AbstractWidget() {
        this.setUndecorated(true);
    }

    /**
     * A method hook to be called every second.
     */
    public void onNextSecond() {
    }

    public void setPosition(Position pos) {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        switch (pos) {
            case TOP_LEFT -> this.setLocation(0, 0);
            case TOP_RIGHT -> this.setLocation(screen.width - this.getWidth(), 0);
            case BOTTOM_LEFT -> this.setLocation(0, screen.height - this.getHeight());
            case BOTTOM_RIGHT -> this.setLocation(screen.width - this.getWidth(), screen.height - this.getHeight());
            case CENTER -> this.setLocation(screen.width / 2 - this.getWidth() / 2,
                    screen.height / 2 - this.getHeight() / 2);
        }
    }
}
