package adda.item.tab.output.beam;

import adda.Context;
import adda.base.AddaOption;
import adda.base.IAddaOption;
import adda.base.IAddaOptionsContainer;
import adda.base.events.IModelPropertyChangeEvent;
import adda.base.models.IModel;
import adda.base.models.IModelObserver;
import adda.item.tab.BooleanFlagModel;
import adda.item.tab.output.geometry.GeometrySaveEnum;
import adda.item.tab.shape.orientation.OrientationEnum;
import adda.item.tab.shape.orientation.OrientationModel;
import adda.utils.StringHelper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BeamSaveModel extends BooleanFlagModel implements IModelObserver {

    public static final String SAVE_BEAM = "store_beam";

    public BeamSaveModel() {
        this.setLabel("Beam");//todo localization
        isVisibleIfDisabled = true;
    }

    @Override
    protected String getAddaCommand() {
        return SAVE_BEAM;
    }

    @Override
    public boolean validate() {
        OrientationModel orientationModel = (OrientationModel) Context.getInstance().getChildModelFromSelectedBox(OrientationModel.class);
        boolean isValid = true;
        String error = "";
        if (orientationModel.getEnumValue() == OrientationEnum.Average) {
            isValid = false;
            error = "<html>" + StringHelper.toDisplayString("Beam saving does`t compatible<br>with orientation average") + "</html>";
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
