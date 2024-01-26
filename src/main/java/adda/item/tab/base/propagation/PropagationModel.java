package adda.item.tab.base.propagation;

import adda.Context;
import adda.base.AddaOption;
import adda.base.IAddaOption;
import adda.base.events.IModelPropertyChangeEvent;
import adda.base.models.IModel;
import adda.base.models.IModelObserver;
import adda.base.models.ModelBase;
import adda.item.tab.TabEnumModel;
import adda.item.tab.base.beam.BeamEnum;
import adda.item.tab.base.size.SizeModel;
import adda.item.tab.internals.initialField.InitialFieldEnum;
import adda.item.tab.internals.initialField.InitialFieldModel;
import adda.item.tab.shape.surface.SurfaceModel;
import adda.utils.StringHelper;

import java.util.Arrays;
import java.util.List;

public class PropagationModel extends TabEnumModel<PropagationEnum> implements IModelObserver {

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
                notifyObservers(X_FIELD_NAME, x); //only one notification is enough for UI redraw
                break;
            case oy:
                x = 0;
                y = 1;
                z = 0;
                notifyObservers(Y_FIELD_NAME, x);
                break;
            case oz:
                x = 0;
                y = 0;
                z = 1;
                notifyObservers(Z_FIELD_NAME, x);
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
        return Arrays.asList(StringHelper.toDisplayString(x), StringHelper.toDisplayString(y), StringHelper.toDisplayString(z));
    }

    @Override
    protected List<IAddaOption> getAddaOptionsInner() {
        return Arrays.asList(new AddaOption("prop", String.join(DELIMITER, getParamsList()), StringHelper.toDisplayString(enumValue) + DELIMITER + String.format("[%s; %s; %s]", getParamsList().toArray())));
    }

    @Override
    public boolean validate() {
        //return true;
        SurfaceModel surfaceModel = (SurfaceModel) Context.getInstance().getChildModelFromSelectedBox(SurfaceModel.class);

        String error = "";
        boolean isValid = true;
        if (surfaceModel != null && surfaceModel.isUseSurface() && z >=0) {
            isValid = false;
            error = "<html>" + StringHelper.toDisplayString("Please use the 'Custom' propagation and  <br>a negative 'z' with the surface mode.") + "</html>";
        }

        validationErrors.put(ENUM_VALUE_FIELD_NAME, error);
        validationErrors.put(Z_FIELD_NAME, error);
        return isValid;
    }


    @Override
    public void modelPropertyChanged(IModel sender, IModelPropertyChangeEvent event) {
        if (sender instanceof SurfaceModel) {
            notifyObservers(ENUM_VALUE_FIELD_NAME, enumValue);
        }
    }
}