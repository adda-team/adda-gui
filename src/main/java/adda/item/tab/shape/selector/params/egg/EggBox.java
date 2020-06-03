package adda.item.tab.shape.selector.params.egg;

import adda.base.annotation.*;
import adda.base.boxes.BoxBase;
import adda.item.tab.shape.selector.params.ViewParamBase;

@BindModel
@BindView(ViewParamBase.class)
@BindController
public class EggBox extends BoxBase {

    public EggBox() {
        needInitSelf = true;
    }
}
