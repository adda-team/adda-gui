package adda.application.validation;

import jiconfont.icons.font_awesome.FontAwesome;
import jiconfont.swing.IconFontSwing;

import javax.swing.*;
import java.awt.*;

public class Error {

    /**
     * No Error
     */
    public static final int NO_ERROR = 0;
    /**
     * Just an information
     */
    public static final int INFO = 1;
    /**
     * A warning
     */
    public static final int WARNING = 2;
    /**
     * A fatal error
     */
    public static final int ERROR = 3;

    private int errorType;
    private String message;

    /**
     *
     * @param errorType Type of the error
     * @param message to be displayed in the tooltip
     */
    public Error(int errorType, String message) {
        this.errorType = errorType;
        this.message = message;
    }

    /**
     *
     * @return errorType
     */
    protected int getErrorType() {
        return errorType;
    }

    /**
     *
     * @return message
     */
    protected String getMessage() {
        return message;
    }

    /**
     * Get a suitable color depending on the error type
     * @return A color
     */
    public Color getColor() {
        switch (errorType) {
            case ERROR:
                return Color.red;
            case INFO:
                return Color.blue;
            case NO_ERROR:
                return Color.WHITE; //any random color,as we'll be using the original color from the component
            case WARNING:
                return Color.yellow;
            default:
                throw new IllegalArgumentException("Not a valid error type");
        }
    }

    /**
     * Get a suitable icon depending on the icon
     * @return ImageIcon
     */
    public Icon getImage() {
        IconFontSwing.register(FontAwesome.getIconFont());
        switch (errorType) {
            case ERROR:
                return IconFontSwing.buildIcon(FontAwesome.EXCLAMATION_CIRCLE, 17, Color.RED);
            case INFO:
                return IconFontSwing.buildIcon(FontAwesome.INFO_CIRCLE, 17, Color.BLUE);
            case NO_ERROR:
                return null;
            case WARNING:
                return IconFontSwing.buildIcon(FontAwesome.EXCLAMATION_TRIANGLE, 17, Color.YELLOW);
            default:
                throw new IllegalArgumentException("Not a valid error type");
        }
    }
}
