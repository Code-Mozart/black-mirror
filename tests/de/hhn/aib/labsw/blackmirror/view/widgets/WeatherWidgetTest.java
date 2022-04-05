package de.hhn.aib.labsw.blackmirror.view.widgets;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WeatherWidgetTest {

    @Test
    void setGPSLocation() {
        WeatherWidget widget = new WeatherWidget();
        assertThrows(IllegalArgumentException.class,()->widget.setGPSLocation(91,181));
        assertThrows(IllegalArgumentException.class,()->widget.setGPSLocation(90,181));
        assertThrows(IllegalArgumentException.class,()->widget.setGPSLocation(91,180));
        assertThrows(IllegalArgumentException.class,()->widget.setGPSLocation(-91,-180));
        assertThrows(IllegalArgumentException.class,()->widget.setGPSLocation(-90,-181));
        assertThrows(IllegalArgumentException.class,()->widget.setGPSLocation(-91,-181));
        assertDoesNotThrow(()->widget.setGPSLocation(90,180));
        assertDoesNotThrow(()->widget.setGPSLocation(-90,-180));
        assertDoesNotThrow(()->widget.setGPSLocation(0,0));
    }
}