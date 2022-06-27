package de.hhn.aib.labsw.blackmirror.controller.widgets;

import de.hhn.aib.labsw.blackmirror.view.widgets.AbstractWidget;
import de.hhn.aib.labsw.blackmirror.view.widgets.CalendarWidget;

/**
 * Calendar Widget Controller
 * @author Luis Gutzeit
 * @version 11.05.2022
 */
public class CalendarWidgetController extends AbstractWidgetController{
    CalendarWidget widget;

    public CalendarWidgetController(){
        widget = new CalendarWidget();
    }

    @Override
    public AbstractWidget getWidget() {
        return widget;
    }

    @Override
    public void onNextSecond() {
        widget.update();
    }

    @Override
    public void close() throws Exception {
        widget.dispose();
    }
}
