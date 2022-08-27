package adda.item.tab.shape.selector.params.sphere;

import adda.base.annotation.*;
import adda.base.boxes.BoxBase;
import adda.item.tab.shape.selector.params.ViewParamBase;

@BindModel
@BindView(ViewParamBase.class)
@BindController
public class SphereBox extends BoxBase {

    public SphereBox() {
        needInitSelf = true;
    }
}
