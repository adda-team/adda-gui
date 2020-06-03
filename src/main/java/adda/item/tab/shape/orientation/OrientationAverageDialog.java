package adda.item.tab.shape.orientation;

import adda.application.controls.CustomOkCancelModalDialog;
import adda.base.events.IModelPropertyChangeEvent;
import adda.base.models.IModel;

public class OrientationAverageDialog  extends CustomOkCancelModalDialog {



    public OrientationAverageDialog(IModel model) {
        super(model, "Orientation average");//todo localization

        if (!(model instanceof OrientationModel)) return;
        //todo localization

        OrientationModel orientationModel = (OrientationModel) model;

        getDialogContentPanel().add(orientationModel.getOrientationAverageBox().getLayout());

    }

        @Override
    public void modelPropertyChanged(IModel sender, IModelPropertyChangeEvent event) {

    }
}
