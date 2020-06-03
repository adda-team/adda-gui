package adda.item.tab.shape.selector.params.prism;

import adda.base.annotation.*;
import adda.base.boxes.BoxBase;
import adda.item.tab.shape.selector.params.ViewParamBase;

@BindModel
@BindView(ViewParamBase.class)
@BindController
public class PrismBox extends BoxBase {

    public PrismBox() {
        needInitSelf = true;
    }
}
