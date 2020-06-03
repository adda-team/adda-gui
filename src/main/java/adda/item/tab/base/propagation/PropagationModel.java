package adda.item.tab.base.propagation;

import adda.base.AddaOption;
import adda.base.IAddaOption;
import adda.base.models.ModelBase;
import adda.item.tab.TabEnumModel;
import adda.utils.StringHelper;

import java.util.Arrays;
import java.util.List;

public class PropagationModel extends TabEnumModel<PropagationEnum> {

    public static final String X_FIELD_NAME = "x";
    public static final String Y_FIELD_NAME = "y";
    public static final String Z_FIELD_NAME = "z";
    public static final String DELIMITER = " ";
    protected double x = 0;
    protected double y = 0;
    protected double z = 1;

    public PropagationModel() {
        setLabel(StringHelper.toDisplayString("Propagation"));
        setEnumValue(PropagationEnum.oz);
        setDefaultEnumValue(PropagationEnum.oz);
    }

    @Override
    public void setEnumValue(PropagationEnum enumValue) {
        super.setEnumValue(enumValue);

        switch (enumValue) {
            case ox:
                x = 1;
                y = 0;
                z = 0;
                break;
            case oy:
                x = 0;
                y = 1;
                z = 0;
                break;
            case oz:
                x = 0;
                y = 0;
                z = 1;
                break;
        }
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        if (this.x != x) {
            this.x = x;
            notifyObservers(X_FIELD_NAME, x);
        }
    }


    public double getY() {
        return y;
    }

    public void setY(double y) {
        if (this.y != y) {
            this.y = y;
            notifyObservers(Y_FIELD_NAME, y);
        }
    }


    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        if (this.z != z) {
            this.z = z;
            notifyObservers(Z_FIELD_NAME, z);
        }
    }


    public List<String> getParamsList() {
        return Arrays.asList(Double.toString(x), Double.toString(y), Double.toString(z));
    }

    @Override
    protected List<IAddaOption> getAddaOptionsInner() {
        return Arrays.asList(new AddaOption("prop", String.join(DELIMITER, getParamsList()), StringHelper.toDisplayString(enumValue) + DELIMITER + String.format("[%s; %s; %s]", getX(), getY(), getZ())));
    }
}