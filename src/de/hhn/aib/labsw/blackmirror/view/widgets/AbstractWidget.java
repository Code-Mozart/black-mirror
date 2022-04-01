package de.hhn.aib.labsw.blackmirror.view.widgets;

import javax.swing.*;

/**
 * Common parent class from which all widgets should be derived.
 *
 * @author Markus Marewitz
 * @version 2022-03-24
 */
public abstract class AbstractWidget extends JDialog {
    public AbstractWidget() {
        this.setUndecorated(true);
    }

    /**
     * A method hook to be called every second.
     */
    public void onNextSecond() {
    }
}
