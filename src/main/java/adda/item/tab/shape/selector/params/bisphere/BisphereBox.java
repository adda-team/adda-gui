package adda.item.tab.shape.selector.params.bisphere;

import adda.base.annotation.*;
import adda.base.boxes.BoxBase;
import adda.item.tab.shape.selector.params.ViewParamBase;

@BindModel
@BindView(ViewParamBase.class)
@BindController
public class BisphereBox extends BoxBase {

    public BisphereBox() {
        needInitSelf = true;
    }
}
