package adda.item.tab.shape.orientation.avarage;

import adda.base.annotation.BindController;
import adda.base.annotation.BindModel;
import adda.base.annotation.BindView;
import adda.base.boxes.BoxBase;
import adda.item.tab.shape.orientation.avarage.alpha.AlphaOrientationAverageBox;
import adda.item.tab.shape.orientation.avarage.alpha.AlphaOrientationAverageModel;
import adda.item.tab.shape.orientation.avarage.gamma.GammaOrientationAverageBox;
import adda.item.tab.shape.orientation.avarage.gamma.GammaOrientationAverageModel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

@BindModel
@BindView
@BindController
public class OrientationAverageBox extends BoxBase {

    AlphaOrientationAverageBox alphaBox;
    GammaOrientationAverageBox gammaBox;


    public OrientationAverageBox() {
        needInitSelf = true;
        children = new ArrayList<>();
        addChild(alphaBox = new AlphaOrientationAverageBox());
        addChild(gammaBox = new GammaOrientationAverageBox());
    }

    @Override
    protected void initChildren() {
        super.initChildren();
        if (model instanceof OrientationAverageModel) {
            ((OrientationAverageModel)model).setAlphaModel((AlphaOrientationAverageModel) alphaBox.getModel());
            ((OrientationAverageModel)model).setGammaModel((GammaOrientationAverageModel) gammaBox.getModel());
        }

    }

    @Override
    protected void initLayout() {
        JPanel panel = getPanel();

        JPanel wrap = new JPanel(new GridLayout(0, 2));
        wrap.add(alphaBox.getLayout());
        wrap.add(gammaBox.getLayout());
        panel.add(wrap);

        if (needInitSelf) {
            panel.add(view.getRootComponent());
        }

        //panel.add(Box.createGlue());

        this.panel = panel;
    }
}
