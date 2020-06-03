package adda.item.tab.shape.selector.params.plate;

import adda.base.annotation.*;
import adda.base.boxes.BoxBase;
import adda.item.tab.shape.selector.params.ViewParamBase;
import adda.item.tab.shape.selector.params.capsule.CapsuleModel;

@BindModel(CapsuleModel.class)
@BindView(ViewParamBase.class)
@BindController
public class PlateBox extends BoxBase {

    public PlateBox() {
        needInitSelf = true;
    }
}
