package adda.item.tab.base.propagation;

import adda.application.controls.JNumericField;
import adda.base.events.IModelPropertyChangeEvent;
import adda.base.models.IModel;
import adda.base.views.ViewDialogBase;

import javax.swing.*;

public class PropagationView extends ViewDialogBase {
    @Override
    protected boolean isShowAdditionalPanel(IModel model) {
        if (model instanceof PropagationModel) {
            return PropagationEnum.custom.equals(((PropagationModel) model).getEnumValue());
        }
        return false;
    }

    @Override
    protected JPanel getOverview(IModel model) {
        JPanel panel = new JPanel();
        if (model instanceof PropagationModel) {
            PropagationModel propagationModel = (PropagationModel) model;
            panel.add(new JLabel(String.format("[%s; %s; %s]", propagationModel.getParamsList().toArray())));//todo localization

        }
        return panel;
    }

    protected void initFromModelInner(IModel model) {
        super.initFromModelInner(model);
    }

    @Override
    public void modelPropertyChanged(IModel sender, IModelPropertyChangeEvent event) {
        super.modelPropertyChanged(sender, event);
        if (PropagationModel.ENUM_VALUE_FIELD_NAME.equals(event.getPropertyName()) || PropagationModel.Z_FIELD_NAME.equals(event.getPropertyName())) {
            final JComboBox comboBox = (JComboBox) components.get(PropagationModel.ENUM_VALUE_FIELD_NAME);
            comboBox.getInputVerifier().verify(comboBox);
        }

    }
}