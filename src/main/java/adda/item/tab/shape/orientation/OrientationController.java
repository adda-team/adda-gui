package adda.item.tab.shape.orientation;

import adda.application.controls.CustomOkCancelModalDialog;
import adda.base.controllers.ControllerBase;
import adda.base.controllers.ControllerDialogBase;
import adda.base.models.IModel;

public class OrientationController extends ControllerDialogBase {

    @Override
    protected boolean needOpenDialog(String fieldName, Object fieldValue) {
        if (fieldValue instanceof OrientationEnum && (OrientationEnum.Rotation.equals(fieldValue) || OrientationEnum.Average.equals(fieldValue))) {
            return true;
        }
        return false;
    }

    @Override
    protected CustomOkCancelModalDialog getDialog(IModel dialogModel) {
        if (!(dialogModel instanceof OrientationModel)) return null;

        OrientationModel orientationModel = (OrientationModel) dialogModel;

        if (OrientationEnum.Rotation.equals(orientationModel.getEnumValue())) {
            return new OrientationDialog(orientationModel);
        }

        if (OrientationEnum.Average.equals(orientationModel.getEnumValue())) {
            return new OrientationAverageDialog(orientationModel);

        }

        return null;
    }

}