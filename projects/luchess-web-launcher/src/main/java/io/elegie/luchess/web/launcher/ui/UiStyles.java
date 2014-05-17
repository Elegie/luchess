package io.elegie.luchess.web.launcher.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

/**
 * Style sheet used for the application. If needed, could be expanded into a
 * theme-based design.
 */
@SuppressWarnings("javadoc")
public final class UiStyles {

    private static final int DEFAULT_PADDING = 5;
    private static final int DEFAULT_FONT_SIZE = 12;
    private static final String DEFAULT_FONT_FACE = "Verdana";
    private static final String DEFAULT_BACKGROUND_COLOR = "#ffffff";
    private static final String DEFAULT_COLOR = "#222222";
    private static final String DEFAULT_ERROR_COLOR = "#cc0000";
    private static final String DEFAULT_ITEM_BACKGROUND_COLOR = "#F7F6CB";
    private static final String DEFAULT_ITEM_COLOR = "#7A4B00";

    private UiStyles() {

    }

    public static int getDefaultPadding() {
        return DEFAULT_PADDING;
    }

    public static Border getBorder(int padding) {
        return BorderFactory.createEmptyBorder(padding, padding, padding, padding);
    }

    // --- Button -------------------------------------------------------------

    public static Dimension getDefaultButtonDimension() {
        final int width = 100;
        return new Dimension(width, DEFAULT_FONT_SIZE * 2);
    }

    public static Color getButtonBackgroundColor() {
        return Color.decode(DEFAULT_ITEM_BACKGROUND_COLOR);
    }

    public static Color getButtonTextColor() {
        return Color.decode(DEFAULT_ITEM_COLOR);
    }

    public static Font getButtonFont() {
        return new Font(DEFAULT_FONT_FACE, Font.BOLD, DEFAULT_FONT_SIZE);
    }

    // --- Link ---------------------------------------------------------------

    public static Color getLinkTextColor() {
        return Color.decode(DEFAULT_ITEM_COLOR);
    }

    public static Font getLinkFont() {
        return new Font(DEFAULT_FONT_FACE, Font.PLAIN, DEFAULT_FONT_SIZE);
    }

    // --- Text ---------------------------------------------------------------

    public static Color getBackgroundColor() {
        return Color.decode(DEFAULT_BACKGROUND_COLOR);
    }

    public static Color getTextColor() {
        return Color.decode(DEFAULT_COLOR);
    }

    public static Color getTextErrorColor() {
        return Color.decode(DEFAULT_ERROR_COLOR);
    }

    public static Font getTextFont() {
        return new Font(DEFAULT_FONT_FACE, Font.PLAIN, DEFAULT_FONT_SIZE);
    }

    // --- Textarea -----------------------------------------------------------

    public static Dimension getAreaDimension() {
        final int width = 600;
        final int height = 30;
        return new Dimension(width, height * DEFAULT_FONT_SIZE);
    }

    public static Font getAreaFont() {
        return new Font(DEFAULT_FONT_FACE, Font.PLAIN, DEFAULT_FONT_SIZE - 1);
    }

}
