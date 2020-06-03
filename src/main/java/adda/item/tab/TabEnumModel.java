package adda.item.tab;

import adda.base.IAddaOption;
import adda.base.annotation.Viewable;
import adda.base.models.ModelBase;
import adda.base.models.ModelBaseAddaOptionsContainer;
import adda.item.tab.output.plane.PlaneSaveEnum;

import java.util.List;

public abstract class TabEnumModel<T extends Enum<T>>  extends ModelBaseAddaOptionsContainer {

    public static final String ENUM_VALUE_FIELD_NAME = "enumValue";
    @Viewable
    protected T enumValue;

    protected T defaultEnumValue;

    public T getEnumValue() {
        return enumValue;
    }

    public void setEnumValue(T enumValue) {
        if((this.enumValue != null && !this.enumValue.equals(enumValue)) || (this.enumValue == null && enumValue != null)) {
            this.enumValue = enumValue;
            notifyObservers(ENUM_VALUE_FIELD_NAME, enumValue);
        }
    }

    public void setDefaultEnumValue(T defaultEnumValue) {
        this.defaultEnumValue = defaultEnumValue;
    }

    @Override
    public boolean isDefaultState() {
        return defaultEnumValue != null && defaultEnumValue.equals(enumValue);
    }

    @Override
    public void applyDefaultState() {
        if (defaultEnumValue != null) {
            setEnumValue(defaultEnumValue);
        }
    }
}
