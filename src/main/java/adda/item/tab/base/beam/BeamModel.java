package adda.item.tab.base.beam;

import adda.Context;
import adda.base.AddaOption;
import adda.base.IAddaOption;
import adda.base.models.ModelBase;
import adda.item.tab.TabEnumModel;
import adda.item.tab.base.dplGrid.DplGridEnum;
import adda.item.tab.internals.initialField.InitialFieldEnum;
import adda.item.tab.shape.surface.SurfaceModel;
import adda.utils.StringHelper;

import java.util.Arrays;
import java.util.List;

public class BeamModel extends TabEnumModel<BeamEnum> {

    public static final String BEAM = "beam";

    public BeamModel() {
        setLabel("Beam");//todo localization
        setEnumValue(BeamEnum.plane);
        setDefaultEnumValue(BeamEnum.plane);
    }

    @Override
    protected List<IAddaOption> getAddaOptionsInner() {
        return Arrays.asList(new AddaOption(BEAM, enumValue.toString(), StringHelper.toDisplayString(enumValue)));
    }

    @Override
    public boolean validate() {

        boolean isValid = true;
        String error = "";
        if (enumValue != BeamEnum.plane) {
            SurfaceModel surfaceModel = (SurfaceModel) Context.getInstance().getChildModelFromSelectedBox(SurfaceModel.class);
            if (surfaceModel.isUseSurface()) {
                isValid = false;
                error = StringHelper.toDisplayString("Non plane beam does`t compatible with 'surface' option");
            }
        }

        validationErrors.put(ENUM_VALUE_FIELD_NAME, error);
        return isValid;
    }
}