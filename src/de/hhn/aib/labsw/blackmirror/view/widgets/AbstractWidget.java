package de.hhn.aib.labsw.blackmirror.view.widgets;

import javax.swing.*;

/**
 * Common parent class from which all widgets should be derived.
 *
 * @author Markus Marewitz
 * @version 2022-03-22
 */
public abstract class AbstractWidget extends JDialog {
    public AbstractWidget() {
        this.setUndecorated(true);
    }
}
