package adda.item.tab.base.propagation;

import adda.application.controls.CustomOkCancelModalDialog;
import adda.base.controllers.ControllerDialogBase;
import adda.base.models.IModel;

public class PropagationController extends ControllerDialogBase {
    @Override
    protected boolean needOpenDialog(String fieldName, Object fieldValue) {
        return PropagationEnum.custom.equals(fieldValue);
    }

    @Override
    protected CustomOkCancelModalDialog getDialog(IModel dialogModel) {
        if (dialogModel instanceof PropagationModel) {
            return new PropagationDialog(dialogModel);
        }
        return null;
    }
}