package adda.item.tab.internals.formulation;

import adda.application.controls.CustomOkCancelModalDialog;
import adda.base.controllers.ControllerBase;
import adda.base.controllers.ControllerDialogBase;
import adda.base.models.IModel;
import adda.item.tab.internals.dipoleShape.DipoleShapeDialog;
import adda.item.tab.internals.dipoleShape.DipoleShapeModel;

public class FormulationController extends ControllerDialogBase {
    @Override
    protected boolean needOpenDialog(String fieldName, Object fieldValue) {
        if (fieldValue instanceof FormulationEnum) {
            return fieldValue.equals(FormulationEnum.Custom);
        }
        return false;
    }

    @Override
    protected CustomOkCancelModalDialog getDialog(IModel dialogModel) {
        if (dialogModel instanceof FormulationModel) {
            return new FormulationDialog(dialogModel);
        }
        return null;
    }
}