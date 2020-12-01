package adda.item.tab.shape.dipoleShape;

import adda.application.controls.CustomOkCancelModalDialog;
import adda.base.controllers.ControllerDialogBase;
import adda.base.models.IModel;

public class DipoleShapeController extends ControllerDialogBase {
    @Override
    protected boolean needOpenDialog(String fieldName, Object fieldValue) {
        return !model.isUnderCopy() && DipoleShapeEnum.Rect.equals(fieldValue);
    }

    @Override
    protected CustomOkCancelModalDialog getDialog(IModel dialogModel) {
        if (dialogModel instanceof DipoleShapeModel) {
            return new DipoleShapeDialog(dialogModel);
        }
        return null;
    }

}