package adda.item.tab.output.polarization;

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
import adda.utils.StringHelper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PolarizationSaveModel extends BooleanFlagModel implements IModelObserver {

    public static final String STORE_DIP_POL = "store_dip_pol";

    public PolarizationSaveModel() {
        this.setLabel("Dipoles polarization");//todo localization
        isVisibleIfDisabled = true;
    }


    @Override
    protected String getAddaCommand() {
        return STORE_DIP_POL;
    }

    @Override
    public boolean validate() {
        OrientationModel orientationModel = (OrientationModel) Context.getInstance().getChildModelFromSelectedBox(OrientationModel.class);
        boolean isValid = true;
        String error = "";
        if (orientationModel.getEnumValue() == OrientationEnum.Average) {
            isValid = false;
            error =  "<html>" + StringHelper.toDisplayString("Dipole polarization saving does`t compatible<br>with orientation average") + "</html>";
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