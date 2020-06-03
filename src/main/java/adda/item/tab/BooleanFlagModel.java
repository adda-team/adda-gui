package adda.item.tab;

import adda.base.AddaOption;
import adda.base.IAddaOption;
import adda.base.IAddaOptionsContainer;
import adda.base.annotation.Viewable;
import adda.base.models.ModelBase;
import adda.base.models.ModelBaseAddaOptionsContainer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class BooleanFlagModel extends ModelBaseAddaOptionsContainer {

    public static final String FLAG_FIELD_NAME = "flag";
    protected volatile boolean isNeedCaching = false;
    protected List<IAddaOption> cachedList;

    protected abstract String getAddaCommand();
    protected String getAddaValue() {
        return null;
    }
    protected String getAddaDescription() {
        return String.valueOf(getFlag());
    }

    @Viewable
    boolean flag = false;

    public boolean getFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        if(this.flag != flag) {
            this.flag = flag;
            notifyObservers(FLAG_FIELD_NAME, this.flag);
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
