package adda.item.tab.shape.orientation.avarage.alpha;

import adda.base.annotation.*;
import adda.item.tab.shape.orientation.avarage.gamma.GammaOrientationAverageBox;
import adda.item.tab.shape.orientation.avarage.gamma.GammaOrientationAverageView;

@BindModel
@BindView(GammaOrientationAverageView.class)
@BindController
public class AlphaOrientationAverageBox extends GammaOrientationAverageBox {

    public AlphaOrientationAverageBox() {
        needInitSelf = true;
    }
}
