package adda.item.tab.base.dplGrid;

import adda.base.AddaOption;
import adda.base.IAddaOption;
import adda.base.annotation.Viewable;
import adda.item.tab.TabEnumModel;
import adda.utils.StringHelper;

import java.util.Arrays;
import java.util.List;

public class DplGridModel extends TabEnumModel<DplGridEnum> {

    private static final String VALUE_FIELD_NAME = "value";
    @Viewable(order = 9)
    double value = 15;

    public DplGridModel() {
        setEnumValue(DplGridEnum.dpl);
        setDefaultEnumValue(DplGridEnum.dpl);
    }

    @Override
    public String getLabel() {
        return StringHelper.toDisplayString(enumValue);
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        if (this.value != value) {
            this.value = value;
            notifyObservers(VALUE_FIELD_NAME, value);
        }
    }

    @Override
    public boolean isDefaultState() {
        return super.isDefaultState() && value == 15;
    }

    @Override
    public void applyDefaultState() {
        setValue(15);
        setEnumValue(defaultEnumValue);
    }

    @Override
    protected List<IAddaOption> getAddaOptionsInner() {
        final String value = DplGridEnum.grid.equals(enumValue) ? StringHelper.toDisplayString((int) Math.round(this.value)) : StringHelper.toDisplayString(this.value);
        IAddaOption addaOption = new AddaOption(enumValue.toString(), value, value);
        return Arrays.asList(addaOption);
    }
}