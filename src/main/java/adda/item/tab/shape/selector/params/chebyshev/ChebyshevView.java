package adda.item.tab.shape.selector.params.chebyshev;

import adda.application.controls.JNumericField;
import adda.base.models.IModel;
import adda.base.views.ViewBase;
import adda.item.tab.shape.selector.params.ViewParamBase;

public class ChebyshevView extends ViewParamBase {

    protected void initFromModelInner(IModel model) {
        super.initFromModelInner(model);
        ((JNumericField) components.get("firstParam")).setAllowNegative(true);
    }
}