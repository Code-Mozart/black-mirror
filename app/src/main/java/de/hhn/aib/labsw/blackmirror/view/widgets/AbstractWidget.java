package de.hhn.aib.labsw.blackmirror.view.widgets;

import de.hhn.aib.labsw.blackmirror.controller.Page;

import javax.swing.*;
import java.awt.*;

/**
 * Common parent class from which all widgets should be derived.
 *
 * @author Markus Marewitz
 * @version 2022-06-23
 */
public abstract class AbstractWidget extends JDialog {

    /**
     * Predefined positions for the widgets on a {@link Page}.
     *
     * @deprecated Positions are now referenced by their respective row and column.
     * @see #setPosition(Position)
     */
    @Deprecated(forRemoval = true, since = "2022-06-07")
    public enum Position {
        TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT, CENTER
    }

    /**
     * Performs the common initialization for all widgets:
     * <ul>
     *     <li>Makes the {@link JDialog} undecorated.</li>
     * </ul>
     */
    public AbstractWidget() {
        this.setUndecorated(true);
    }

    /**
     * Sets the position of this widget within a {@link Page}.
     *
     * @deprecated Positions are now referenced by their respective row and column
     * so {@link #setPosition(int, int)} is preferred.
     */
    @Deprecated(forRemoval = true, since = "2022-06-07")
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

    /**
     * @param xPosition The column in which the widget should be placed, starting with {@code 0}.
     * @param yPosition The row in which the widget should be placed, starting with {@code 0}.
     */
    public void setPosition(int xPosition, int yPosition) {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        Point pos = new Point(xPosition, yPosition);

        if(pos.y == 1) {
            if(pos.x == 1) {
                this.setLocation(0, 0);
            } else if(pos.x == 2) {
                this.setLocation(screen.width/2 - this.getWidth(), 0);
            } else {
                this.setLocation(screen.width - this.getWidth(), 0);
            }
        }

        if(pos.y == 2) {
            if(pos.x == 1) {
                this.setLocation(0, screen.height/2 - this.getHeight()/2);
            } else if(pos.x == 2) {
                this.setLocation(screen.width/2 - this.getWidth(), screen.height/2 - this.getHeight()/2);
            } else {
                this.setLocation(screen.width - this.getWidth(), screen.height/2 - this.getHeight()/2);
            }
        }

        if(pos.y == 3) {
            if(pos.x == 1) {
                this.setLocation(0, screen.height - this.getHeight());
            } else if(pos.x == 2) {
                this.setLocation(screen.width/2 - this.getWidth(), screen.height - this.getHeight());
            } else {
                this.setLocation(screen.width - this.getWidth(), screen.height - this.getHeight());
            }
        }

    }
}