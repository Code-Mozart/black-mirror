package de.hhn.aib.labsw.blackmirror.view.widgets;

import java.awt.*;

public enum WidgetFont {
    STANDARD_FONT("Arial", Font.PLAIN);

    private final String fontName;
    private final int fontStyle;
    WidgetFont(String fontName, int fontStyle) {
        this.fontName = fontName;
        this.fontStyle = fontStyle;
    }

    public String getFontName() {
        return fontName;
    }
    public int getFontStyle() {
        return fontStyle;
    }
}
