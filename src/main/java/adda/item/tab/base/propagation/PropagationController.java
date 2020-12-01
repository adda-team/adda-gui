package adda.item.tab.base.propagation;

import adda.application.controls.CustomOkCancelModalDialog;
import adda.base.controllers.ControllerDialogBase;
import adda.base.models.IModel;
import adda.item.tab.internals.formulation.FormulationEnum;
import adda.item.tab.internals.formulation.FormulationModel;

public class PropagationController extends ControllerDialogBase {
    @Override
    protected boolean needOpenDialog(String fieldName, Object fieldValue) {
        if (PropagationModel.ENUM_VALUE_FIELD_NAME.equals(fieldName) && fieldValue instanceof PropagationEnum) {
            return PropagationEnum.custom.equals(fieldValue);
        }
        return false;

    }

    @Override
    protected CustomOkCancelModalDialog getDialog(IModel dialogModel) {
        if (dialogModel instanceof PropagationModel) {
            return new PropagationDialog(dialogModel);
        }
        return null;
    }
}