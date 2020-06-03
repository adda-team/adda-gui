package adda.item.tab.output.qabs;

import adda.base.models.IModel;
import adda.base.views.ViewBase;

import javax.swing.*;

public class QabsSaveView extends ViewBase {
    @Override
    protected void initFromModelInner(IModel model) {
        super.initFromModelInner(model);
        components.entrySet()
                .stream()
                .filter(component -> component.getValue() instanceof JCheckBox)
                .forEach(component -> component.getValue().setEnabled(false));
    }
}