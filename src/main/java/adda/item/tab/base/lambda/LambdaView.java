package adda.item.tab.base.lambda;

import adda.base.events.IModelPropertyChangeEvent;
import adda.base.models.IModel;
import adda.base.views.ViewBase;

import javax.swing.*;
import java.awt.*;

public class LambdaView extends ViewBase {

    private JLabel measureLabel = new JLabel();

    @Override
    protected void initFromModelInner(IModel model) {
        super.initFromModelInner(model);
        measureLabel.setText(((LambdaModel)model).measure);
        panel.add(measureLabel);
    }

    @Override
    public void modelPropertyChanged(IModel sender, IModelPropertyChangeEvent event) {
        super.modelPropertyChanged(sender, event);
        if (event.getPropertyName().equals(LambdaModel.IS_ENABLED_FIELD_NAME)) {
            boolean enabled = (boolean) event.getPropertyValue();
            panel.setEnabled(enabled);
            components.entrySet()
                    .forEach(item -> item.getValue().setEnabled(enabled));
        }
        if (event.getPropertyName().equals(LambdaModel.MEASURE_FIELD_NAME)) {
            measureLabel.setText(((LambdaModel)sender).measure);
        }
    }
}
