package adda.help;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.stream.Stream;

import javax.swing.*;

import net.java.balloontip.BalloonTip;
import net.java.balloontip.CustomBalloonTip;

/**
 * This class allows you to use a balloon tip as a tooltip
 * That is, the balloon tip will only show up for a certain amount of time while you hover over the attached component.
 * @author Tim Molderez
 */
public final class HelpUtil {

    /*
     * Disallow instantiating this class
     */
    private HelpUtil() {}

    /*
     * This class monitors when the balloon tooltip should be shown
     */
    private static class ToolTipController extends MouseAdapter implements MouseMotionListener {
        private final BalloonTip balloonTip;
        private final Timer initialTimer;
        private final Timer exitTimer;

        /**
         * Constructor
         * @param balloonTip	the balloon tip to turn into a tooltip
         * @param initialDelay	in milliseconds, how long should you hover over the attached component before showing the tooltip
         */
        public ToolTipController(final BalloonTip balloonTip, int initialDelay) {
            super();
            this.balloonTip = balloonTip;
            initialTimer = new Timer(initialDelay, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    balloonTip.setVisible(true);

                }
            });
            initialTimer.setRepeats(false);

            exitTimer = new Timer(Math.min(initialDelay, 100), new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    balloonTip.setVisible(false);
                }
            });
            exitTimer.setRepeats(false);
        }

        public void mouseEntered(MouseEvent e) {
            stopTimers();
            initialTimer.start();
        }

        volatile Component comp;

        public void mouseExited(MouseEvent e) {
            if (!isMouseWithinBaloon()) {
                stopTimers();
                exitTimer.start();
            }
        }

        /*
         * Stops all timers related to this tool tip
         */
        private void stopTimers() {
            initialTimer.stop();
            exitTimer.stop();
        }

        public boolean isMouseWithinBaloon()
        {
            if (balloonTip.isVisible()) {
                Point mousePos = MouseInfo.getPointerInfo().getLocation();
                Rectangle bounds = balloonTip.getBounds();
                bounds.setLocation(balloonTip.getLocationOnScreen());
                return bounds.contains(mousePos);
            }
            return false;
        }

    }

    /**
     * Turns a balloon tip into a tooltip
     * This is done by adding a mouse listener to the attached component.
     * (Call toolTipToBalloon() if you wish to remove this listener.)
     * @param bT			the balloon tip
     * @param initialDelay	in milliseconds, how long should you hover over the attached component before showing the tooltip
     */
    public static void balloonToHelpToolTip(final BalloonTip bT, int initialDelay) {
        bT.setVisible(false);
        // Add tooltip behaviour
        ToolTipController tTC = new ToolTipController(bT, initialDelay);
        bT.getAttachedComponent().addMouseListener(tTC);

        tTC = new ToolTipController(bT, 0);
        bT.addMouseListener(tTC);

        ArrayList<JButton> buttons = new ArrayList<>();
        recursivePropagateButtons(bT, buttons);

        for (JButton button : buttons) {
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    bT.setVisible(false);
                }
            });
        }
    }

    protected static void recursivePropagateButtons(JComponent panel, ArrayList<JButton> buttons) {
        for (Component component : panel.getComponents()) {
            if (component.getClass().equals(JButton.class)) {
                buttons.add((JButton) component);
            }

            if (component instanceof JComponent && ((JComponent) component).getComponents().length > 0) {
                recursivePropagateButtons((JComponent) component, buttons);
            }
        }
    }

    /**
     * Turns a balloon tooltip back into a regular balloon tip
     * @param bT			the balloon tip
     */
    public static void toolTipToBalloon(final BalloonTip bT) {
        // Remove tooltip behaviour
        for (MouseListener m: bT.getAttachedComponent().getMouseListeners()) {
            if (m instanceof ToolTipController) {
                bT.getAttachedComponent().removeMouseListener(m);
                ((ToolTipController) m).stopTimers();
                break;
            }
        }
        for (MouseMotionListener m: bT.getAttachedComponent().getMouseMotionListeners()) {
            if (m instanceof ToolTipController) {
                bT.getAttachedComponent().removeMouseMotionListener(m);
                break;
            }
        }

        bT.setVisible(true);
    }
}

