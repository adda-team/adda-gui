package adda.application.validation;

import adda.Context;
import net.java.balloontip.BalloonTip;
import net.java.balloontip.styles.EdgedBalloonStyle;
import net.java.balloontip.styles.MinimalBalloonStyle;
import net.java.balloontip.styles.RoundedBalloonStyle;

import java.awt.Color;
import javax.swing.*;
import javax.swing.border.Border;

public abstract class ErrorProvider extends InputVerifier {

    private Border originalBorder;
    private Color originalBackgroundColor;
    private String originalTooltipText;
    private Object parent;

    /**
     *
     * @param c The JComponent to be validated.
     */
    public ErrorProvider(JComponent c) {
        originalBorder = c.getBorder();
        originalBackgroundColor = c.getBackground();
        originalTooltipText = c.getToolTipText();

    }

    /**
     *
     * @param parent A JFrame that implements the ValidationStatus interface.
     * @param c The JComponent to be validated.
     */
    public ErrorProvider(JFrame parent, JComponent c) {
        this(c);
        this.parent = parent;
    }

    /**
     *
     * @param parent A JDialog that implements the ValidationStatus interface.
     * @param c The JComponent to be validated.
     */
    public ErrorProvider(JDialog parent, JComponent c) {
        this(c);
        this.parent = parent;
    }

    /**
     * Define your custom Error in this method and return an Error Object.
     * @param c The JComponent to be validated.
     * @return Error
     * @see Error
     */
    protected abstract Error ErrorDefinition(JComponent c);

    /**
     * This method is called by Java when a component needs to be validated.
     * @param c The JCOmponent being validated
     * @return
     */

    volatile BalloonTip balloonTip;
    RoundedBalloonStyle style = new RoundedBalloonStyle(5, 5, Color.WHITE, Color.RED);

    @Override
    public boolean verify(JComponent c) {
        Error error = ErrorDefinition(c);
        if (error.getErrorType() == Error.NO_ERROR) {
            //revert back all changes made to the component
            c.setBackground(originalBackgroundColor);
            c.setBorder(originalBorder);
            //c.setToolTipText(originalTooltipText);
            if (balloonTip != null) {
                balloonTip.closeBalloon();
                balloonTip = null;
            }
        } else {
            c.setBorder(new IconBorder(error.getImage(), originalBorder));
            c.setBackground(error.getColor());
            //c.setToolTipText(error.getMessage());

            if (balloonTip != null) {
                balloonTip.closeBalloon();
            }
            // Now construct the balloon tip
            balloonTip = new BalloonTip(
                    c,
                    new JLabel("<html><font color=\"#ff0000\">" + error.getMessage() + "</font></html>"),
                    style,
                    BalloonTip.Orientation.RIGHT_ABOVE,
                    BalloonTip.AttachLocation.NORTHEAST,
                    30, 10,
                    false
            );


        }
        if (error.getErrorType() == Error.ERROR) {
            if (parent instanceof ValidationStatus) {
                ((ValidationStatus) parent).reportStatus(false);
            }
//            if (Context.getInstance().getLastParamsComponent() != null) {
//                Context.getInstance().getLastParamsComponent().requestFocus();
//            }
            return false;
        } else {
            if (parent instanceof ValidationStatus) {
                ((ValidationStatus) parent).reportStatus(true);
            }
            return true;
        }

    }
}

