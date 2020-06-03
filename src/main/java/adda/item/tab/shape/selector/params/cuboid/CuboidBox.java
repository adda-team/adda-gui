package adda.item.tab.shape.selector.params.cuboid;

import adda.base.annotation.*;
import adda.base.boxes.BoxBase;
import adda.item.tab.shape.selector.params.ViewParamBase;

@BindModel
@BindView(ViewParamBase.class)
@BindController
public class CuboidBox extends BoxBase {

    public CuboidBox() {
        needInitSelf = true;
    }
}
