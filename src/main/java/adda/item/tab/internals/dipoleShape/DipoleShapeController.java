package adda.item.tab.internals.dipoleShape;

import adda.application.controls.CustomOkCancelModalDialog;
import adda.base.controllers.ControllerBase;
import adda.base.controllers.ControllerDialogBase;
import adda.base.models.IModel;
import adda.item.tab.base.propagation.PropagationDialog;
import adda.item.tab.base.propagation.PropagationModel;

public class DipoleShapeController extends ControllerDialogBase {
    @Override
    protected boolean needOpenDialog(String fieldName, Object fieldValue) {
        return DipoleShapeEnum.Rect.equals(fieldValue);
    }

    @Override
    protected CustomOkCancelModalDialog getDialog(IModel dialogModel) {
        if (dialogModel instanceof DipoleShapeModel) {
            return new DipoleShapeDialog(dialogModel);
        }
        return null;
    }

}