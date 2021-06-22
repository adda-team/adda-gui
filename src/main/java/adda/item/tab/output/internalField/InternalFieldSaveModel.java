package adda.item.tab.output.internalField;

import adda.Context;
import adda.base.AddaOption;
import adda.base.IAddaOption;
import adda.base.IAddaOptionsContainer;
import adda.base.events.IModelPropertyChangeEvent;
import adda.base.models.IModel;
import adda.base.models.IModelObserver;
import adda.item.tab.BooleanFlagModel;
import adda.item.tab.shape.orientation.OrientationEnum;
import adda.item.tab.shape.orientation.OrientationModel;
import adda.item.tab.shape.surface.SurfaceModel;
import adda.utils.StringHelper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InternalFieldSaveModel extends BooleanFlagModel implements IModelObserver {

    public static final String STORE_INT_FIELD = "store_int_field";

    public InternalFieldSaveModel() {
        this.setLabel("Internal field");//todo localization
        isVisibleIfDisabled = true;
    }


    @Override
    protected String getAddaCommand() {
        return STORE_INT_FIELD;
    }

    @Override
    public boolean validate() {
        OrientationModel orientationModel = (OrientationModel) Context.getInstance().getChildModelFromSelectedBox(OrientationModel.class);
        boolean isValid = true;
        String error = "";
        if (orientationModel != null && orientationModel.getEnumValue() == OrientationEnum.Average) {
            isValid = false;
            error = "<html>" + StringHelper.toDisplayString("Internal field saving does`t compatible<br>with orientation average") + "</html>";
        }
        validationErrors.put(IS_ENABLED_FIELD_NAME, error);
        return isValid;
    }

    @Override
    public void modelPropertyChanged(IModel sender, IModelPropertyChangeEvent event) {
        if (sender instanceof OrientationModel) {
            setEnabled(validate());
        }
    }
}