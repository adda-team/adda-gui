package adda.item.tab.shape.selector.params.capsule;

import adda.base.annotation.*;
import adda.base.boxes.BoxBase;
import adda.item.tab.shape.selector.params.ViewParamBase;

@BindModel
@BindView(ViewParamBase.class)
@BindController
public class CapsuleBox extends BoxBase {

    public CapsuleBox() {
        needInitSelf = true;
    }
}
