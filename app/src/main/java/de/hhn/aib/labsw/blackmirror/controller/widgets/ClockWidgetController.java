package de.hhn.aib.labsw.blackmirror.controller.widgets;

import de.hhn.aib.labsw.blackmirror.view.widgets.AbstractWidget;
import de.hhn.aib.labsw.blackmirror.view.widgets.clock.ClockFaceType;
import de.hhn.aib.labsw.blackmirror.view.widgets.clock.ClockWidget;

/**
 * @author Markus Marewitz
 * @version 2022-05-11
 */
public class ClockWidgetController extends AbstractWidgetController {
    private ClockWidget widget;

    public ClockWidgetController(ClockFaceType type) {
        widget = new ClockWidget(type);
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
