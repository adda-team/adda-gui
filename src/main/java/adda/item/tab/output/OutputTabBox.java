package adda.item.tab.output;

import adda.application.controls.VerticalLayout;
import adda.base.annotation.*;
import adda.base.boxes.BoxBase;
import adda.item.tab.output.actualDirectory.ActualDirectoryBox;
import adda.item.tab.output.asymParams.AsymParamsSaveBox;
import adda.item.tab.output.beam.BeamSaveBox;
import adda.item.tab.output.geometry.GeometrySaveBox;
import adda.item.tab.output.granul.GranulSaveBox;
import adda.item.tab.output.internalField.InternalFieldSaveBox;
import adda.item.tab.output.plane.PlaneSaveBox;
import adda.item.tab.output.polarization.PolarizationSaveBox;
import adda.item.tab.output.qabs.QabsSaveBox;
import adda.item.tab.output.qext.QextSaveBox;
import adda.item.tab.output.qsca.QscaSaveBox;
import adda.item.tab.output.radiationForce.RadiationForceSaveBox;
import adda.item.tab.output.scatteringMatrix.ScatteringMatrixSaveBox;
import adda.item.tab.output.theta.ThetaSaveBox;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

@BindModel
@BindView
@BindController
public class OutputTabBox extends BoxBase {

    public OutputTabBox() {
        needInitSelf = false;
        children = new ArrayList<>();
        addChild(new ActualDirectoryBox());
        addChild(new QabsSaveBox());
        addChild(new QextSaveBox());
        addChild(new QscaSaveBox());
        addChild(new RadiationForceSaveBox());
        addChild(new AsymParamsSaveBox());
        addChild(new ScatteringMatrixSaveBox());
        addChild(new GeometrySaveBox());
        addChild(new BeamSaveBox());
        addChild(new PolarizationSaveBox());
        addChild(new GranulSaveBox());
        addChild(new InternalFieldSaveBox());
        addChild(new ThetaSaveBox());
        addChild(new PlaneSaveBox());
    }

    @Override
    protected JPanel getPanel() {
        JPanel panel = new JPanel();
//        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setLayout(new VerticalLayout());
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setAlignmentY(Component.TOP_ALIGNMENT);
        return panel;
    }
}
