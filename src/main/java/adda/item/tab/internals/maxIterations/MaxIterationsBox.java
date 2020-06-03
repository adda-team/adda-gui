package adda.item.tab.internals.maxIterations;

import adda.base.annotation.BindController;
import adda.base.annotation.BindModel;
import adda.base.annotation.BindView;
import adda.base.boxes.BoxBase;

@BindModel
@BindView
@BindController
public class MaxIterationsBox extends BoxBase {

    public MaxIterationsBox() {
        needInitSelf = true;
    }
}