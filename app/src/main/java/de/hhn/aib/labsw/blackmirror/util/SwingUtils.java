package de.hhn.aib.labsw.blackmirror.util;

import javax.swing.*;
import java.awt.*;

/**
 * Utility class providing useful methods for the Java Swing Framework
 * and Swing components.
 *
 * @author Markus Marewitz
 * @version 2022-04-01
 */
public final class SwingUtils {
    private SwingUtils() {
    }

    /**
     * Recursively sets the font for the component and all its subcomponents.
     */
    public static void setFont(JComponent jc, Font f) {
        jc.setFont(f);
        for (Component c : jc.getComponents()) {
            if (c instanceof JComponent)
                setFont((JComponent) c, f);
        }
    }

    /**
     * Recursively sets the font for the dialog and all its subcomponents.
     */
    public static void setFont(JDialog d, Font f) {
        d.setFont(f);
        for (Component c : d.getComponents()) {
            if (c instanceof JComponent)
                setFont((JComponent) c, f);
        }
    }
}
