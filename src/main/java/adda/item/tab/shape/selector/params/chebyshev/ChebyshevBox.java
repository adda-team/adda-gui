package adda.item.tab.shape.selector.params.chebyshev;

import adda.base.annotation.*;
import adda.base.boxes.BoxBase;
import adda.item.tab.shape.selector.params.ViewParamBase;

@BindModel
@BindView(ViewParamBase.class)
@BindController
public class ChebyshevBox extends BoxBase {

    public ChebyshevBox() {
        needInitSelf = true;
    }
}
