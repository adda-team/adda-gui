package adda.item.tab.shape.surface;

import adda.base.models.IModel;
import adda.base.views.ViewBase;
import adda.base.views.ViewDialogBase;
import adda.item.tab.shape.granules.GranulesModel;

import javax.swing.*;
import java.awt.*;

public class SurfaceView extends ViewDialogBase {

    @Override
    public void initPanel() {
        this.panel = new JPanel(new FlowLayout());
        this.outerPanel = new JPanel(new FlowLayout());
        this.outerPanel.add(this.panel);
        JPanel additionalPanel = new JPanel();
        additionalPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        this.additionalPanel = additionalPanel;
        this.outerPanel.add(this.additionalPanel);
    }

    @Override
    protected boolean isShowAdditionalPanel(IModel model) {
        if (model instanceof SurfaceModel) {
            SurfaceModel surfaceModel = (SurfaceModel) model;
            return surfaceModel.isUseSurface();
        }
        return false;
    }

    @Override
    protected JPanel getOverview(IModel model) {
        return null;
    }
}