package adda.item.tab.shape.selector.params.cylinder;

import adda.base.annotation.*;
import adda.base.boxes.BoxBase;
import adda.item.tab.shape.selector.params.ViewParamBase;

@BindModel
@BindView(ViewParamBase.class)
@BindController
public class CylinderBox extends BoxBase {

    public CylinderBox() {
        needInitSelf = true;
    }
}
