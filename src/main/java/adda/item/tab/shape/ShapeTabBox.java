package adda.item.tab.shape;

import adda.application.controls.VerticalLayout;
import adda.base.annotation.*;
import adda.base.boxes.BoxBase;
import adda.item.tab.shape.dipoleShape.DipoleShapeBox;
import adda.item.tab.shape.granules.GranulesBox;
import adda.item.tab.shape.orientation.OrientationBox;
import adda.item.tab.shape.selector.ShapeSelectorBox;
import adda.item.tab.shape.surface.SurfaceBox;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

@BindModel
@BindView
@BindController
public class ShapeTabBox extends BoxBase {

    public ShapeTabBox() {
        needInitSelf = false;
        needInitSelf = false;
        children = new ArrayList<>();
        addChild(new ShapeSelectorBox());
        addChild(new OrientationBox());
        addChild(new GranulesBox());
        addChild(new SurfaceBox());
        addChild(new DipoleShapeBox());

    }

    @Override
    protected JPanel getPanel() {
        JPanel panel = new JPanel();
        // panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setLayout(new VerticalLayout());
//        panel.setBorder(BorderFactory.createLineBorder(Color.black));;
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setAlignmentY(Component.TOP_ALIGNMENT);
        return panel;
    }

    @Override
    protected void initLayout() {
        super.initLayout();
        //panel.add(Box.createVerticalGlue());

    }
}
