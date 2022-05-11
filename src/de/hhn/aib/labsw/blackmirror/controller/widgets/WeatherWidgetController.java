package de.hhn.aib.labsw.blackmirror.controller.widgets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import de.hhn.aib.labsw.blackmirror.model.ApiDataModels.Location;
import de.hhn.aib.labsw.blackmirror.view.widgets.AbstractWidget;
import de.hhn.aib.labsw.blackmirror.view.widgets.WeatherWidget;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

/**
 * @author Markus Marewitz
 * @version 2022-05-11
 */
public class WeatherWidgetController extends AbstractWidgetController {

    private final WeatherWidget widget;

    public WeatherWidgetController() {
        this.widget = new WeatherWidget();
    }

    @Override
    public AbstractWidget getWidget() {
        return widget;
    }
}
