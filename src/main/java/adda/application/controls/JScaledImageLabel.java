package adda.application.controls;

import adda.utils.ImageDrawer;

import javax.swing.*;
import java.awt.*;

public class JScaledImageLabel extends JLabel {
    protected void paintComponent(Graphics g) {
        ImageIcon icon = (ImageIcon) getIcon();
        if (icon != null) {
            ImageDrawer.drawScaledImage(icon.getImage(), this, g);
        }
    }
}
