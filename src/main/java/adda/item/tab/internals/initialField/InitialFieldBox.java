package adda.item.tab.internals.initialField;

import adda.base.annotation.*;
import adda.base.boxes.BoxBase;

@BindModel
@BindView
@BindController
public class InitialFieldBox extends BoxBase {

    public InitialFieldBox() {
        needInitSelf = true;
    }
}
