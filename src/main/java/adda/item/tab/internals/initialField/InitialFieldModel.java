package adda.item.tab.internals.initialField;

import adda.Context;
import adda.base.AddaOption;
import adda.base.IAddaOption;
import adda.base.events.IModelPropertyChangeEvent;
import adda.base.models.IModel;
import adda.base.models.IModelObserver;
import adda.item.tab.TabEnumModel;
import adda.item.tab.shape.surface.SurfaceModel;
import adda.utils.StringHelper;

import java.util.Arrays;
import java.util.List;

public class InitialFieldModel extends TabEnumModel<InitialFieldEnum> {

    public static final String INIT_FIELD = "init_field";

    public InitialFieldModel() {
        this.setLabel("Initial field");//todo localization
        setEnumValue(InitialFieldEnum.auto);
        setDefaultEnumValue(InitialFieldEnum.auto);
    }

    @Override
    protected List<IAddaOption> getAddaOptionsInner() {
        IAddaOption addaOption = new AddaOption(INIT_FIELD, enumValue.toString(), StringHelper.toDisplayString(enumValue));
        return Arrays.asList(addaOption);
    }


    @Override
    public boolean validate() {

        boolean isValid = true;
        String error = "";
        if (enumValue == InitialFieldEnum.wkb) {
            SurfaceModel surfaceModel = (SurfaceModel) Context.getInstance().getChildModelFromSelectedBox(SurfaceModel.class);
            if (surfaceModel != null && surfaceModel.isUseSurface()) {
                isValid = false;
                error = StringHelper.toDisplayString("WKB initial field for the iterative solver  does`t compatible with 'surface' option");
            }
        }

        validationErrors.put(ENUM_VALUE_FIELD_NAME, error);
        return isValid;
    }
}