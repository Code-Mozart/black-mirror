package de.hhn.aib.labsw.blackmirror.view.widgets;

import javax.swing.*;
import java.awt.*;

/**
 * Template widget. TODO remove!
 *
 * @author Markus Marewitz
 * @version 2022-03-22
 */
public class HelloWorldWidget extends AbstractWidget {
    public HelloWorldWidget() {
        this.setSize(200, 200);

        JPanel panel = new JPanel();
        panel.setBackground(Color.BLACK);

        JLabel label = new JLabel("Hello World");
        label.setForeground(Color.WHITE);

        panel.add(label);
        this.add(panel);

        this.setLocation(200,200);
    }
}
