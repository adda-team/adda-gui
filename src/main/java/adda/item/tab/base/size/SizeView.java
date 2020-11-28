package adda.item.tab.base.size;

import adda.base.models.IModel;
import adda.base.views.ViewBase;
import adda.item.tab.TabEnumModel;

import javax.swing.*;
import java.awt.*;

public class SizeView extends ViewBase {
    @Override
    protected void initLabel(IModel model) {

    }

    @Override
    protected void initFromModelInner(IModel model) {
        super.initFromModelInner(model);
        if (components.containsKey(SizeModel.TYPE_FIELD_NAME)) {
            final Component component = components.get(SizeModel.TYPE_FIELD_NAME);
            component.setPreferredSize(new Dimension(120, 20));
            setHelpTooltip(model, (JComponent) component);
        }
    }
}