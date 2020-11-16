package adda.item.tab.output.geometry;

import adda.base.AddaOption;
import adda.base.IAddaOption;
import adda.base.IAddaOptionsContainer;
import adda.base.annotation.BindEnableFrom;
import adda.base.annotation.Viewable;
import adda.base.models.ModelBase;
import adda.item.tab.BooleanFlagModel;
import adda.utils.StringHelper;

import java.util.*;

public class GeometrySaveModel extends BooleanFlagModel {

    public static final String GEOMETRY_SAVE_ENUM_FIELD_NAME = "geometrySaveEnum";
    public static final String SAVE_GEOM = "save_geom";
    public static final String SG_FORMAT = "sg_format";
    public static final String SAVE = "save";
    @BindEnableFrom("flag")
    @Viewable(order = 100)
    GeometrySaveEnum geometrySaveEnum = GeometrySaveEnum.default_geometry;

    public GeometrySaveModel() {
        this.setLabel("Geometry");//todo localization
    }

    public GeometrySaveEnum getGeometrySaveEnum() {
        return geometrySaveEnum;
    }

    public void setGeometrySaveEnum(GeometrySaveEnum geometrySaveEnum) {
        if (!this.geometrySaveEnum.equals(geometrySaveEnum)) {
            this.geometrySaveEnum = geometrySaveEnum;
            notifyObservers(GEOMETRY_SAVE_ENUM_FIELD_NAME, geometrySaveEnum);
        }
    }


    @Override
    protected String getAddaCommand() {
        return SAVE_GEOM;
    }

    @Override
    public void applyDefaultState() {
        setFlag(false);
        setGeometrySaveEnum(GeometrySaveEnum.default_geometry);
    }

    @Override
    protected List<IAddaOption> getAddaOptionsInner() {

        if (geometrySaveEnum == GeometrySaveEnum.default_geometry) {
            return Arrays.asList(
                    new AddaOption(getAddaCommand(), null, StringHelper.toDisplayString(SAVE))
            );
        } else {
            return Arrays.asList(
                    new AddaOption(getAddaCommand(), null, StringHelper.toDisplayString(SAVE)),
                    new AddaOption(SG_FORMAT, geometrySaveEnum.toString(), StringHelper.toDisplayString(geometrySaveEnum))
            );
        }
    }


}