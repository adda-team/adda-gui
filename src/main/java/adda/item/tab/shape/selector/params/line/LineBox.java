package adda.item.tab.shape.selector.params.line;

import adda.base.annotation.*;
import adda.base.boxes.BoxBase;
import adda.item.tab.shape.selector.params.ViewParamBase;

@BindModel
@BindView(ViewParamBase.class)
@BindController
public class LineBox extends BoxBase {

    public LineBox() {
        needInitSelf = true;
    }
}
