package adda.item.tab.shape.orientation;

import adda.base.models.IModel;
import adda.base.views.ViewBase;
import adda.base.views.ViewDialogBase;
import adda.item.tab.internals.formulation.FormulationDialog;

import javax.swing.*;
import java.awt.*;

public class OrientationView extends ViewDialogBase {

    @Override
    public void initPanel() {
        this.panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel outerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        this.outerPanel = outerPanel;
        this.outerPanel.add(this.panel);

        JPanel additionalPanel = new JPanel();
        additionalPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        this.additionalPanel = additionalPanel;
        this.outerPanel.add(this.additionalPanel);
    }

    @Override
    protected boolean isShowAdditionalPanel(IModel model) {
        if (!(model instanceof OrientationModel)) return false;

        OrientationModel orientationModel = (OrientationModel) model;

        return OrientationEnum.Rotation.equals(orientationModel.getEnumValue()) || OrientationEnum.Average.equals(orientationModel.getEnumValue());
    }

    @Override
    protected JPanel getOverview(IModel model) {
        return null;
    }


}