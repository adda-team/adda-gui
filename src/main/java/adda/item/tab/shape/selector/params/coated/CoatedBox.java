package adda.item.tab.shape.selector.params.coated;

import adda.base.annotation.*;
import adda.base.boxes.BoxBase;
import adda.item.tab.shape.selector.params.ViewParamBase;

@BindModel
@BindView(ViewParamBase.class)
@BindController
public class CoatedBox extends BoxBase {

    public CoatedBox() {
        needInitSelf = true;
    }
}
