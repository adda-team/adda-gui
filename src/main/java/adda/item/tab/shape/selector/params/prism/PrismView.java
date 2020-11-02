package adda.item.tab.shape.selector.params.prism;

import adda.application.controls.JNumericField;
import adda.base.models.IModel;
import adda.base.views.ViewBase;
import adda.item.tab.shape.selector.params.ViewParamBase;

import javax.swing.*;

public class PrismView extends ViewParamBase {

    protected void initFromModelInner(IModel model) {
        super.initFromModelInner(model);
        ((JSpinner) components.get("firstParam")).setModel(new SpinnerNumberModel(((PrismModel) model).getFirstParam(), 2, Integer.MAX_VALUE, 1));
    }

}