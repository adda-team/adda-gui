package adda.item.tab.shape.surface;

import adda.base.events.IModelPropertyChangeEvent;
import adda.base.models.IModel;
import adda.base.views.ViewBase;
import adda.base.views.ViewDialogBase;
import adda.item.tab.base.refractiveIndex.RefractiveIndexModel;
import adda.item.tab.shape.granules.GranulesModel;
import adda.utils.StringHelper;
import net.java.balloontip.BalloonTip;
import net.java.balloontip.styles.RoundedBalloonStyle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SurfaceView extends ViewDialogBase {

    JCheckBox checkBox;

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