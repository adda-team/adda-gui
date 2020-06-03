package adda.item.tab.internals.volCorrection;

import adda.base.annotation.BindController;
import adda.base.annotation.BindModel;
import adda.base.annotation.BindView;
import adda.base.boxes.BoxBase;

@BindModel
@BindView
@BindController
public class VolCorrectionBox extends BoxBase {

    public VolCorrectionBox() {
        needInitSelf = true;
    }
}