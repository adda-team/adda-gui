package adda.item.tab.shape.surface;

import adda.application.controls.CustomOkCancelModalDialog;
import adda.base.controllers.ControllerBase;
import adda.base.controllers.ControllerDialogBase;
import adda.base.models.IModel;
import adda.item.tab.shape.granules.GranulesDialog;
import adda.item.tab.shape.granules.GranulesModel;

public class SurfaceController extends ControllerDialogBase {
    @Override
    protected boolean needOpenDialog(String fieldName, Object fieldValue) {
        if (SurfaceModel.IS_USE_SURFACE_FIELD_NAME.equals(fieldName)) {
            return (boolean) fieldValue;
        }
        return false;
    }

    @Override
    protected CustomOkCancelModalDialog getDialog(IModel dialogModel) {
        if (dialogModel instanceof SurfaceModel) {
            return new SurfaceDialog(dialogModel);
        }
        return null;
    }
}