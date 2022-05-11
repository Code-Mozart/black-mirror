package de.hhn.aib.labsw.blackmirror.controller.widgets;

import de.hhn.aib.labsw.blackmirror.view.widgets.AbstractWidget;
import de.hhn.aib.labsw.blackmirror.view.widgets.HelloWorldWidget;

/**
 * @author Markus Marewitz
 * @version 2022-05-11
 */
public class HelloWorldWidgetController extends AbstractWidgetController {
    private final HelloWorldWidget widget;

    public HelloWorldWidgetController() {
        this.widget = new HelloWorldWidget();
    }

    @Override
    public AbstractWidget getWidget() {
        return widget;
    }
}
