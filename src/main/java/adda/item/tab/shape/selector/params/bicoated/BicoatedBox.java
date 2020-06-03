package adda.item.tab.shape.selector.params.bicoated;

import adda.base.annotation.*;
import adda.base.boxes.BoxBase;
import adda.item.tab.shape.selector.params.ViewParamBase;

@BindModel
@BindView(ViewParamBase.class)
@BindController
public class BicoatedBox extends BoxBase {

    public BicoatedBox() {
        needInitSelf = true;
    }
}
