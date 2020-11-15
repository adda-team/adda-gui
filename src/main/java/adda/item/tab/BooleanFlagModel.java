package adda.item.tab;

import adda.base.AddaOption;
import adda.base.IAddaOption;
import adda.base.IAddaOptionsContainer;
import adda.base.annotation.BindEnableFrom;
import adda.base.annotation.Viewable;
import adda.base.models.ModelBase;
import adda.base.models.ModelBaseAddaOptionsContainer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class BooleanFlagModel extends ModelBaseAddaOptionsContainer {

    public static final String FLAG_FIELD_NAME = "flag";
    public static final String IS_ENABLED_FIELD_NAME = "isEnabled";

    protected abstract String getAddaCommand();
    protected String getAddaValue() {
        return null;
    }
    protected String getAddaDescription() {
        return String.valueOf(getFlag());
    }

    @BindEnableFrom("isEnabled")
    @Viewable
    boolean flag = false;

    boolean isEnabled = true;

    public boolean getFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        if(this.flag != flag) {
            this.flag = flag;
            notifyObservers(FLAG_FIELD_NAME, this.flag);
        }

    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        if(this.isEnabled != enabled) {
            isEnabled = enabled;
            notifyObservers(IS_ENABLED_FIELD_NAME, isEnabled);
        }

    }

    @Override
    public boolean isDefaultState() {
        return !getFlag();
    }

    @Override
    public void applyDefaultState() {
        setFlag(false);
    }


    @Override
    protected List<IAddaOption> getAddaOptionsInner() {
        return Arrays.asList(new AddaOption(getAddaCommand(), getAddaValue(), getAddaDescription()));
    }
}
