package adda.item.tab.output.radiationForce;

import adda.Context;
import adda.base.events.IModelPropertyChangeEvent;
import adda.base.models.IModel;
import adda.base.models.IModelObserver;
import adda.item.tab.BooleanFlagModel;
import adda.item.tab.shape.surface.SurfaceModel;
import adda.utils.StringHelper;

public class RadiationForceSaveModel extends BooleanFlagModel implements IModelObserver {

    public static final String STORE_FORCE = "store_force";

    public RadiationForceSaveModel() {
        this.setLabel("Total radiation force");//todo localization
        isVisibleIfDisabled = true;
    }

    @Override
    protected String getAddaCommand() {
        return STORE_FORCE;
    }


    @Override
    public boolean validate() {
        SurfaceModel surfaceModel = (SurfaceModel) Context.getInstance().getChildModelFromSelectedBox(SurfaceModel.class);
        boolean isValid = true;
        String error = "";
        if (surfaceModel.isUseSurface()) {
            isValid = false;
            error = StringHelper.toDisplayString("Radiation Force does`t compatible with 'surface' option");
        }
        validationErrors.put(IS_ENABLED_FIELD_NAME, error);
        return isValid;
    }

    @Override
    public void modelPropertyChanged(IModel sender, IModelPropertyChangeEvent event) {
        if (sender instanceof SurfaceModel) {
            setEnabled(validate());
        }
    }
}