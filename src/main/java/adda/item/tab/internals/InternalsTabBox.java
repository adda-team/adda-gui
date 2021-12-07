package adda.item.tab.internals;

import adda.application.controls.VerticalLayout;
import adda.base.annotation.*;
import adda.base.boxes.BoxBase;
import adda.item.tab.internals.accuracy.AccuracyBox;
import adda.item.tab.internals.formulation.FormulationBox;
import adda.item.tab.internals.initialField.InitialFieldBox;
import adda.item.tab.internals.iterativeSolver.IterativeSolverBox;
import adda.item.tab.internals.maxIterations.MaxIterationsBox;
import adda.item.tab.internals.optimization.OptimizationBox;
import adda.item.tab.internals.reducedFFT.ReducedFftBox;
import adda.item.tab.internals.symmetry.SymmetryBox;
import adda.item.tab.internals.volCorrection.VolCorrectionBox;
import adda.item.tab.internals.jagged.JaggedBox;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

@BindModel
@BindView
@BindController
public class InternalsTabBox extends BoxBase {

    public InternalsTabBox() {
        needInitSelf = false;
        children = new ArrayList<>();
        addChild(new FormulationBox());
        addChild(new IterativeSolverBox());
        addChild(new JaggedBox());
        addChild(new VolCorrectionBox());
        addChild(new AccuracyBox());
        addChild(new MaxIterationsBox());
        addChild(new OptimizationBox());
        addChild(new SymmetryBox());
        addChild(new InitialFieldBox());
        addChild(new ReducedFftBox());

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
