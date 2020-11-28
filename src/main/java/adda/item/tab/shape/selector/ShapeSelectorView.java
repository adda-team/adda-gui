package adda.item.tab.shape.selector;

import adda.application.controls.JScaledImageLabel;
import adda.application.controls.VerticalLayout;
import adda.base.events.IModelPropertyChangeEvent;
import adda.base.models.IModel;
import adda.base.views.ViewBase;
import adda.item.tab.TabEnumModel;

import javax.swing.*;
import java.awt.*;

public class ShapeSelectorView extends ViewBase {

    JLabel pictureLabel = new JScaledImageLabel();
    //todo copypaste from DialogView. create AdditionalPanelView and inherite from it dialog view and this
    protected JPanel outerPanel;
    protected JPanel additionalPanel;




    @Override
    public JComponent getRootComponent() {
        return this.outerPanel;
    }
    @Override
    public void initPanel() {
        super.initPanel();
        JPanel outerPanel = new JPanel(new GridBagLayout());

        outerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        outerPanel.setAlignmentY(Component.TOP_ALIGNMENT);

        JPanel additionalPanel = new JPanel();
        additionalPanel.setLayout(new BorderLayout());
        additionalPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        additionalPanel.setAlignmentY(Component.TOP_ALIGNMENT);
        additionalPanel.setPreferredSize(new Dimension(140, 155));
        additionalPanel.setMaximumSize(new Dimension(140, 155));

        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrapper.setAlignmentY(Component.TOP_ALIGNMENT);
        wrapper.add(Box.createVerticalGlue());
        wrapper.add(panel);
        wrapper.add(additionalPanel);

        this.additionalPanel = additionalPanel;
        this.outerPanel = outerPanel;

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        this.outerPanel.add(wrapper, gbc);

        //this.outerPanel.add(wrapper);

    }

    @Override
    protected void initFromModelInner(IModel model) {
        super.initFromModelInner(model);

        if (components.containsKey(ShapeSelectorModel.ENUM_VALUE_FIELD_NAME)) {
            final Component component = components.get(ShapeSelectorModel.ENUM_VALUE_FIELD_NAME);
            component.setPreferredSize(new Dimension(120, 20));
            setHelpTooltip(model, (JComponent) component);
        }

        pictureLabel.setAlignmentY(Component.TOP_ALIGNMENT);
        pictureLabel.setVerticalAlignment(JLabel.TOP);
        pictureLabel.setVerticalTextPosition(JLabel.TOP);
        pictureLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        pictureLabel.setHorizontalAlignment(JLabel.CENTER);
        pictureLabel.setHorizontalTextPosition(JLabel.CENTER);
//        pictureLabel.setBorder(BorderFactory.createLineBorder(Color.black));
        additionalPanel.setAlignmentY(Component.TOP_ALIGNMENT);
        pictureLabel.setPreferredSize(new Dimension(140, 165));
        pictureLabel.setMaximumSize(new Dimension(140, 165));
//        pictureLabel.setPreferredSize(new Dimension(230, 230));
        ShapeSelectorModel shapeSelectorModel = (ShapeSelectorModel) model;
        pictureLabel.setIcon(getImageIcon(shapeSelectorModel.getEnumValue().toString()));


        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        outerPanel.add(pictureLabel, gbc);
        //outerPanel.add(pictureLabel);
        if (shapeSelectorModel.getParamsBox() != null) {
            additionalPanel.add(shapeSelectorModel.getParamsBox().getLayout());
            additionalPanel.repaint();
            additionalPanel.revalidate();
        }
    }

    @Override
    public void modelPropertyChanged(IModel sender, IModelPropertyChangeEvent event) {
        super.modelPropertyChanged(sender, event);
        if (event.getPropertyValue() instanceof ShapeSelectorEnum) {
            pictureLabel.setIcon(getImageIcon(event.getPropertyValue().toString()));
            ShapeSelectorModel model = (ShapeSelectorModel) sender;
            if (model.getParamsBox() == null) {
                if (additionalPanel.getComponents().length > 0) {
                    additionalPanel.removeAll();
                    additionalPanel.repaint();
                    additionalPanel.revalidate();
                    additionalPanel.add(new JPanel());
                }
            } else {
                if (additionalPanel.getComponents().length == 1) {
                    if (!additionalPanel.getComponent(0).equals(model.getParamsBox().getLayout())) {
                        additionalPanel.removeAll();
                        additionalPanel.repaint();
                        additionalPanel.revalidate();
                        additionalPanel.add(model.getParamsBox().getLayout());
                        additionalPanel.repaint();
                        additionalPanel.revalidate();
                    }
                } else {
                    if (additionalPanel.getComponents().length > 1) {
                        additionalPanel.removeAll();
                        additionalPanel.repaint();
                        additionalPanel.revalidate();
                    }
                    additionalPanel.add(model.getParamsBox().getLayout());
                    additionalPanel.repaint();
                    additionalPanel.revalidate();
                }
            }
        }
    }

    protected static ImageIcon getImageIcon(String imageName) {
        String path = "image/shape/" + imageName + ".png";

        java.net.URL imgURL = ShapeSelectorView.class.getClassLoader().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            return null;
        }
    }

    @Override
    protected void initLabel(IModel model) {

    }


}