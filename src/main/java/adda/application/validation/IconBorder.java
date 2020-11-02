package adda.application.validation;


import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.net.URL;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;

/**
 * Class to draw an Icon Border around the JComponent
 * <pre>
 *           --------------------
 *           |                * |
 *           --------------------
 *   where * is the posistion of the icon
 * </pre>
 * @version 1b
 * @author Naveed Quadri
 */
public class IconBorder extends AbstractBorder {

    /**
     * icon to draw
     */
    private Icon icon;
    /**
     * The actual border of the component. Draws any component decorations you may have
     * and then adds the icon
     */
    private Border originalBorder;

    /**
     * Creates an Icon Border with the specified icon
     * @param icon icon to draw
     * @param originalBorder The actual border of the component. Draws any component decorations you may have and then adds the icon
     */
    public IconBorder(Icon icon, Border originalBorder) {
        this.icon = icon;
        this.originalBorder = originalBorder;
    }



    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        originalBorder.paintBorder(c, g, x, y, width, height);
        Insets insets = getBorderInsets(c);
        int by = (c.getHeight() / 2) - (icon.getIconHeight() / 2);
        int w = Math.max(2, insets.left);
        int bx = x + width - (icon.getIconHeight() + (w * 2)) + 2;
        g.translate(bx, by);
        icon.paintIcon(c, g, x, y);
    }

    /**
     * Returns the insets of the border.
     * @param c the component for which this border insets value applies
     */
    @Override
    public Insets getBorderInsets(Component c) {
        return originalBorder.getBorderInsets(c);
    }

    /**
     * Returns whether or not the border is opaque.
     */
    @Override
    public boolean isBorderOpaque() {
        return false;
    }





}
