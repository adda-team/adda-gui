package adda.item.tab.shape.granules;

import adda.base.models.IModel;
import adda.base.views.ViewBase;
import adda.base.views.ViewDialogBase;
import adda.item.tab.base.refractiveIndex.RefractiveIndexDialog;
import adda.item.tab.base.refractiveIndex.RefractiveIndexModel;

import javax.swing.*;
import java.awt.*;

public class GranulesView extends ViewDialogBase {

    @Override
    public void initPanel() {
        this.panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        this.outerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        this.outerPanel.add(this.panel);

        JPanel additionalPanel = new JPanel();
        additionalPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        this.additionalPanel = additionalPanel;
        this.outerPanel.add(this.additionalPanel);
    }

    @Override
    protected void initFromModelInner(IModel model) {
        super.initFromModelInner(model);
        if (components.containsKey(RefractiveIndexModel.IS_ANISOTROP_FIELD_NAME)) {
            components.get(RefractiveIndexModel.IS_ANISOTROP_FIELD_NAME).setVisible(false);
        }
    }

    @Override
    protected boolean isShowAdditionalPanel(IModel model) {
        if (model instanceof GranulesModel) {
            GranulesModel granulesModel = (GranulesModel) model;
            return granulesModel.isUseGranul();
        }
        return false;
    }


    @Override
    protected JPanel getOverview(IModel model) {
        return null;
//        JPanel panel = new JPanel();
//        if (model instanceof GranulesModel) {
//            GranulesModel granulesModel = (GranulesModel) model;
//            //panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
//            JTextArea textArea = new JTextArea();
//            StringBuilder builder = new StringBuilder();
//
//            builder.append(String.format("fraction: %s %%", granulesModel.getFraction()));//todo localization
//            builder.append(String.format("diameter: %s", granulesModel.getDiameter()));//todo localization
//            builder.append(String.format("shape domain: %s", granulesModel.getDomainDisplayString()));//todo localization
//            builder.append(String.format("refractive index: %s + i%s", granulesModel.getRealX(), granulesModel.getImagX()));//todo localization
//
//            textArea.setText(builder.toString());
//            textArea.setWrapStyleWord(true);
//            textArea.setLineWrap(true);
//            textArea.setAlignmentX(Component.CENTER_ALIGNMENT);
//            textArea.setOpaque(false);
//            textArea.setEditable(false);
//            textArea.setFocusable(true);
//            textArea.setBackground(UIManager.getColor("Label.background"));
//            textArea.setBorder(UIManager.getBorder("Label.border"));
//            textArea.setAlignmentY(Component.CENTER_ALIGNMENT);
//            panel.add(textArea);
//
//
//        }
//        return panel;
    }


}