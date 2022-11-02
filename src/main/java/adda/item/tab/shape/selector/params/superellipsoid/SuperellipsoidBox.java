package adda.item.tab.shape.selector.params.superellipsoid;

import adda.base.annotation.*;
import adda.base.boxes.BoxBase;
import adda.item.tab.shape.selector.params.ViewParamBase;

@BindModel
@BindView(ViewParamBase.class)
@BindController
public class SuperellipsoidBox extends BoxBase {

    public SuperellipsoidBox() {
        needInitSelf = true;
    }
}
