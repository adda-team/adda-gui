package adda.item.tab.shape.granules;

import adda.application.controls.CustomOkCancelModalDialog;
import adda.base.controllers.ControllerDialogBase;
import adda.base.models.IModel;
import adda.item.tab.base.propagation.PropagationDialog;
import adda.item.tab.base.propagation.PropagationModel;

public class GranulesController extends ControllerDialogBase {
    @Override
    protected boolean needOpenDialog(String fieldName, Object fieldValue) {
        if (GranulesModel.IS_USE_GRANUL_FIELD_NAME.equals(fieldName)) {
            return (boolean) fieldValue;
        }
        return false;
    }

    @Override
    protected CustomOkCancelModalDialog getDialog(IModel dialogModel) {
        if (dialogModel instanceof GranulesModel) {
            return new GranulesDialog(dialogModel);
        }
        return null;
    }

}
