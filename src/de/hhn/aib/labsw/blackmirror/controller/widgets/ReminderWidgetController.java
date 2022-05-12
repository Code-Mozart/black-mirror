package de.hhn.aib.labsw.blackmirror.controller.widgets;

import de.hhn.aib.labsw.blackmirror.view.widgets.AbstractWidget;
import de.hhn.aib.labsw.blackmirror.view.widgets.ReminderWidget;

public class ReminderWidgetController extends AbstractWidgetController{
    ReminderWidget widget;

    public ReminderWidgetController(){
        widget = new ReminderWidget();
    }

    @Override
    public AbstractWidget getWidget() {
        return widget;
    }

    @Override
    public void onNextSecond() {
        widget.update();
    }
}
