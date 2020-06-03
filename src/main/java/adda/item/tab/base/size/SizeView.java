package adda.item.tab.base.size;

import adda.base.models.IModel;
import adda.base.views.ViewBase;

import java.awt.*;

public class SizeView extends ViewBase {
    @Override
    protected void initLabel(IModel model) {

    }

    @Override
    protected void initFromModelInner(IModel model) {
        super.initFromModelInner(model);
        if (components.containsKey(SizeModel.TYPE_FIELD_NAME)) {
            components.get(SizeModel.TYPE_FIELD_NAME).setPreferredSize(new Dimension(120, 20));
        }
    }
}