package adda.item.tab.base.dplGrid;

import adda.base.models.IModel;
import adda.base.views.ViewBase;
import adda.item.tab.TabEnumModel;

import java.awt.*;

public class DplGridView extends ViewBase {
    @Override
    protected void initLabel(IModel model) {

    }

    @Override
    protected void initFromModelInner(IModel model) {
        super.initFromModelInner(model);
        if (components.containsKey(TabEnumModel.ENUM_VALUE_FIELD_NAME)) {
            components.get(TabEnumModel.ENUM_VALUE_FIELD_NAME).setPreferredSize(new Dimension(120, 20));
        }
    }
}