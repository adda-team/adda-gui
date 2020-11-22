package adda.item.tab.base;

import adda.application.controls.VerticalLayout;
import adda.base.annotation.*;
import adda.base.boxes.BoxBase;
import adda.item.tab.base.beam.BeamBox;
import adda.item.tab.base.dplGrid.DplGridBox;
import adda.item.tab.base.lambda.LambdaBox;
import adda.item.tab.base.propagation.PropagationBox;
import adda.item.tab.base.refractiveIndexAggregator.RefractiveIndexAggregatorBox;
import adda.item.tab.base.size.SizeBox;
import adda.utils.Binder;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

@BindModel
@BindView
@BindController
public class BaseTabBox extends BoxBase {
    public BaseTabBox() {
        needInitSelf = false;
        children = new ArrayList<>();

        addChild(new SizeBox());
        addChild(new LambdaBox());
        addChild(new DplGridBox());
        addChild(new RefractiveIndexAggregatorBox());
        addChild(new PropagationBox());
        addChild(new BeamBox());


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

    @Override
    protected void initChildren() {
        super.initChildren();

        SizeBox sizeBox = (SizeBox) children.stream()
                .filter(entity -> entity instanceof SizeBox)
                .findFirst()
                .get();


        LambdaBox lambdaBox = (LambdaBox) children.stream()
                .filter(entity -> entity instanceof LambdaBox)
                .findFirst()
                .get();

        Binder.bind(sizeBox, lambdaBox);
    }
}
