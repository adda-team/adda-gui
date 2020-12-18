package adda.item.root.shortcut;

import adda.base.views.ViewBase;
import adda.base.models.IModel;
import jiconfont.swing.IconFontSwing;
import jiconfont.icons.font_awesome.FontAwesome;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ShortcutsView extends ViewBase {

    public JButton runButton;
    public JButton prognosisButton;

    public void initFromModel(IModel model) {
        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(5, 30, 5, 30));
        panel.setBackground(Color.WHITE);
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        //panel.setPreferredSize(new Dimension(50, 50));
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.gridx = 0;
//        gbc.gridy = 1;
//        gbc.weightx = 1.0;
//        gbc.weighty = 1.0;
//        gbc.fill = GridBagConstraints.BOTH;

//        panel.add(new JLabel("shortcuts"), gbc);
        panel.add(new JLabel("Mode:"));

        //todo get from the model
        String[] items = {
                "CPU",
//                "GPU [#1]",
//                "GPU [#2]"
        };
        JComboBox editComboBox = new JComboBox(items);
        editComboBox.setPreferredSize(new Dimension(70, 33));
        //editComboBox.setEditable(true);
        panel.add(editComboBox);

        panel.add( Box.createHorizontalStrut(20) );

        IconFontSwing.register(FontAwesome.getIconFont());



        Icon runIcon = IconFontSwing.buildIcon(FontAwesome.PLAY, 25, Color.DARK_GRAY);
        JButton runButton = new JButton("Run",runIcon);
//        button.setBorder(new RoundedBorder(10)); //10 is the radius
        panel.add(runButton);
        this.runButton = runButton;
        panel.add( Box.createHorizontalStrut(20) );
        Icon icon = IconFontSwing.buildIcon(FontAwesome.STEP_FORWARD, 25, Color.blue);
        JButton button = new JButton("Prognosis",icon);
        button.setEnabled(false);
//        button.setBorder(new RoundedBorder(10)); //10 is the radius
        panel.add(button);
        prognosisButton = button;



        this.panel = panel;
    }
//    private static class RoundedBorder implements Border {
//
//        private int radius;
//
//
//        RoundedBorder(int radius) {
//            this.radius = radius;
//        }
//
//
//        public Insets getBorderInsets(Component c) {
//            return new Insets(this.radius+1, this.radius+1, this.radius+2, this.radius);
//        }
//
//
//        public boolean isBorderOpaque() {
//            return true;
//        }
//
//
//        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
//            g.drawRoundRect(x, y, width-1, height-1, radius, radius);
//        }
//    }
}
