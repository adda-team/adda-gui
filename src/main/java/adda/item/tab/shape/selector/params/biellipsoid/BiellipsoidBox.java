package adda.item.tab.shape.selector.params.biellipsoid;

import adda.base.annotation.*;
import adda.base.boxes.BoxBase;
import adda.item.tab.shape.selector.params.ViewParamBase;

@BindModel
@BindView(ViewParamBase.class)
@BindController
public class BiellipsoidBox extends BoxBase {

    public BiellipsoidBox() {
        needInitSelf = true;
    }
}
