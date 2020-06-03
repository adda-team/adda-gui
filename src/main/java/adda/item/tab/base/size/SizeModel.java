package adda.item.tab.base.size;

import adda.base.AddaOption;
import adda.base.IAddaOption;
import adda.base.annotation.Viewable;
import adda.base.models.ModelBase;
import adda.base.models.ModelBaseAddaOptionsContainer;
import adda.utils.StringHelper;

import java.util.Arrays;
import java.util.List;

public class SizeModel extends ModelBaseAddaOptionsContainer {

    public static final String TYPE_FIELD_NAME = "type";
    public static final String MEASURE_FIELD_NAME = "measure";
    public static final String VALUE_FIELD_NAME = "value";
    public static final String SIZE = "size";
    public static final String EQ_RAD = "eq_rad";
    public static final String SPACE_STR = " ";
    @Viewable
    SizeEnum type = SizeEnum.AlongOX;

    @Viewable
    double value = 1.0;

    @Viewable
    SizeMeasureEnum measure = SizeMeasureEnum.um;//LambdaModel is depended

    @Override
    public String getLabel() {
        return StringHelper.toDisplayString(type);
    }

    public SizeEnum getType() {
        return type;
    }

    public void setType(SizeEnum type) {
        if (!this.type.equals(type)) {
            this.type = type;
            notifyObservers(TYPE_FIELD_NAME, type);
        }

    }

    public SizeMeasureEnum getMeasure() {
        return measure;
    }

    public void setMeasure(SizeMeasureEnum measure) {
        if (!this.measure.equals(measure)) {
            this.measure = measure;
            notifyObservers(MEASURE_FIELD_NAME, measure);
        }
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
        return false;
    }

    @Override
    public void applyDefaultState() {
        setMeasure(SizeMeasureEnum.um);
        setType(SizeEnum.AlongOX);
        setValue(1);
    }

    @Override
    protected List<IAddaOption> getAddaOptionsInner() {
        String command = SizeEnum.AlongOX.equals(type) ? SIZE : EQ_RAD;
        IAddaOption addaOption = new AddaOption(command, String.valueOf(value),  value + SPACE_STR + StringHelper.toDisplayString(measure));
        return Arrays.asList(addaOption);
    }
}