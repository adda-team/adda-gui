package adda.item.tab.shape.orientation.avarage.gamma;

import adda.base.annotation.*;
import adda.base.boxes.BoxBase;

import javax.swing.*;
import java.io.Serializable;

@BindModel
@BindView
@BindController
public class GammaOrientationAverageBox extends BoxBase implements Serializable {

    public GammaOrientationAverageBox() {
        needInitSelf = true;
    }

    @Override
    public JPanel getLayout() {
        if (view != null && view.getRootComponent() instanceof JPanel) {
            return (JPanel) view.getRootComponent();
        }
        return panel;
    }
}
