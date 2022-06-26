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

    public AbstractWidget() {
        this.setUndecorated(true);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screen.width/4, screen.height/3);
    }

    public void setPosition(int xPosition, int yPosition) {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

        int windowSizeX = screen.width;
        int windowSizeY = screen.height;

        final int COLUMNS = 4;
        final int ROWS = 3;

        final int stepSizeX = windowSizeX / COLUMNS;
        final int stepSizeY = windowSizeY / ROWS;

        final int widgetPositionX = (xPosition * stepSizeX);
        final int widgetPositionY = (yPosition * stepSizeY);

        setLocation(widgetPositionX, widgetPositionY);
    }
}