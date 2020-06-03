package adda.item.tab.shape.selector.params.rbc;

import adda.base.annotation.*;
import adda.base.boxes.BoxBase;
import adda.item.tab.shape.selector.params.ViewParamBase;

@BindModel
@BindView(ViewParamBase.class)
@BindController
public class RbcBox extends BoxBase {

    public RbcBox() {
        needInitSelf = true;
    }
}
