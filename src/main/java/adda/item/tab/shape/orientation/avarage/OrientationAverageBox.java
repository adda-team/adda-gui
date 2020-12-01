package adda.item.tab.shape.orientation.avarage;

import adda.base.annotation.BindController;
import adda.base.annotation.BindModel;
import adda.base.annotation.BindView;
import adda.base.boxes.BoxBase;
import adda.base.events.IModelPropertyChangeEvent;
import adda.base.models.IModel;
import adda.base.models.IModelObserver;
import adda.item.tab.shape.orientation.avarage.alpha.AlphaOrientationAverageBox;
import adda.item.tab.shape.orientation.avarage.alpha.AlphaOrientationAverageModel;
import adda.item.tab.shape.orientation.avarage.gamma.GammaOrientationAverageBox;
import adda.item.tab.shape.orientation.avarage.gamma.GammaOrientationAverageModel;
import adda.utils.StringHelper;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

@BindModel
@BindView
@BindController
public class OrientationAverageBox extends BoxBase implements Serializable {

    AlphaOrientationAverageBox alphaBox;
    AlphaOrientationAverageBox betaBox;
    AlphaOrientationAverageBox gammaBox;


    public OrientationAverageBox() {
        needInitSelf = true;
        children = new ArrayList<>();
        addChild(alphaBox = new AlphaOrientationAverageBox());
        addChild(betaBox = new AlphaOrientationAverageBox());
        addChild(gammaBox = new AlphaOrientationAverageBox());
    }

    @Override
    protected void initChildren() {
        super.initChildren();
        if (model instanceof OrientationAverageModel) {
            ((OrientationAverageModel)model).setAlphaModel((AlphaOrientationAverageModel) alphaBox.getModel());
            ((OrientationAverageModel)model).setBetaModel((AlphaOrientationAverageModel) betaBox.getModel());
            ((OrientationAverageModel)model).setGammaModel((AlphaOrientationAverageModel) gammaBox.getModel());
        }

    }

    @Override
    protected void initLayout() {
        JPanel panel = getPanel();

        JPanel wrap = new JPanel(new GridLayout(0, 3));
        final JPanel alphaBoxLayout = alphaBox.getLayout();
        if (alphaBoxLayout != null) {
            wrap.add(alphaBoxLayout);
        }

        final JPanel betaBoxLayout = betaBox.getLayout();
        if (betaBoxLayout != null) {
            wrap.add(betaBoxLayout);
        }

        final JPanel gammaBoxLayout = gammaBox.getLayout();
        if (gammaBoxLayout != null) {
            wrap.add(gammaBoxLayout);
        }


        if (needInitSelf && panel != null) {
            JPanel alfaBetaGamma = new JPanel(){
                @Override
                public boolean isOptimizedDrawingEnabled() {
                    return false;
                }
            };
            alfaBetaGamma.setLayout(new OverlayLayout(alfaBetaGamma));

            JPanel overlay = new JPanel(new BorderLayout());
            overlay.setBackground(new Color(255, 255, 255, 255));
            final JLabel label = new JLabel("From file");
            //label.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 20));
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setVerticalAlignment(SwingConstants.CENTER);
            overlay.add(label);
            overlay.setVisible(!StringHelper.isEmpty(((OrientationAverageModel)model).getAverageFile()));

            alfaBetaGamma.add(overlay);
            alfaBetaGamma.add(wrap);

            model.addObserver(new IModelObserver() {
                @Override
                public void modelPropertyChanged(IModel sender, IModelPropertyChangeEvent event) {
                    if (sender instanceof OrientationAverageModel) {
                        overlay.setVisible(!StringHelper.isEmpty(((OrientationAverageModel)model).getAverageFile()));
                    }
                }
            });

            panel.add(alfaBetaGamma);
            panel.add(view.getRootComponent());
        }

        //panel.add(Box.createGlue());

        this.panel = panel;
    }
}
