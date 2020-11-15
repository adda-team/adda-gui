package adda.item.tab.output.qabs;

import adda.base.models.IModel;
import adda.base.views.ViewBase;
import jiconfont.icons.font_awesome.FontAwesome;
import jiconfont.swing.IconFontSwing;
import net.java.balloontip.BalloonTip;
import net.java.balloontip.styles.RoundedBalloonStyle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class QabsSaveView extends ViewBase {
    @Override
    protected void initFromModelInner(IModel model) {
        super.initFromModelInner(model);
        components.entrySet()
                .stream()
                .filter(component -> component.getValue() instanceof JCheckBox)
                .forEach(component -> component.getValue().setEnabled(false));

        final JCheckBox checkBox = (JCheckBox) components.get("flag");
        checkBox.addMouseListener(new MouseAdapter() {

            volatile BalloonTip balloonTip;

            @Override
            public void mouseEntered(MouseEvent me) {
                if (balloonTip == null || !balloonTip.isVisible()) {
                    RoundedBalloonStyle style = new RoundedBalloonStyle(5, 5, Color.WHITE, Color.black);
                    balloonTip = new BalloonTip(
                            checkBox,
                            new JLabel("Calculation of Qabs is required"),
                            style,
                            BalloonTip.Orientation.RIGHT_ABOVE,
                            BalloonTip.AttachLocation.NORTHWEST,
                            30, 10,
                            false
                    );
//                    Icon clearIcon = IconFontSwing.buildIcon(FontAwesome.TIMES_CIRCLE, 12, Color.DARK_GRAY);
//                    JButton button = new JButton();
//                    button.setBorder(BorderFactory.createEmptyBorder(5, 6, 2, 5));
//                    button.setBorderPainted(false);
//                    button.setFocusPainted(false);
//                    button.setOpaque(false);
//                    button.setContentAreaFilled(false);
//                    button.setIcon(clearIcon);
//                    balloonTip.setCloseButton(button, true);
                }

            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (balloonTip != null) {
                    balloonTip.closeBalloon();
                    balloonTip = null;
                }
            }
        });
        
    }
}