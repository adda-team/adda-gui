package adda.application.controls;

import javax.swing.border.AbstractBorder;
import java.awt.*;

public class MatrixBorder extends AbstractBorder {

    private Color color;
    private int gap;
    private int bracketsTopAndBottom = 10;
    private int internalGap;

    public MatrixBorder(Color color, int gap, int internalGap) {
        this.color = color;
        this.gap = gap;
        this.internalGap = internalGap;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        super.paintBorder(c, g, x, y, width, height);
        Graphics2D g2d = null;
        if (g instanceof Graphics2D) {
            g2d = (Graphics2D) g;
            g2d.setColor(color);
            g2d.setStroke(new BasicStroke(3));

            //top part of brackets
            g2d.drawLine(x + gap, y + gap, x + gap + bracketsTopAndBottom, (y +  gap));
            g2d.drawLine(width - x - gap - bracketsTopAndBottom, y + gap, width - gap - x, (y +  gap));

            //bottom part of brackets
            g2d.drawLine(x + gap, height - gap, x + gap + bracketsTopAndBottom, height - gap);
            g2d.drawLine(width - x - gap - bracketsTopAndBottom, height - gap, width - gap - x, height - gap);

            //left and right part of brackets
            g2d.drawLine(x + gap, y + gap, x + gap, height - gap);
            g2d.drawLine(width - x - gap, y + gap, width - x - gap, height - gap);
        }
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return getBorderInsets(c, new Insets(gap, gap, gap, gap));
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.left = insets.top = insets.right = insets.bottom = gap + internalGap;
        return insets;
    }
}
