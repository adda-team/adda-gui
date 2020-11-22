package adda.item.tab.base.beam;

import adda.Context;
import adda.base.AddaOption;
import adda.base.IAddaOption;
import adda.base.annotation.BindEnableFrom;
import adda.base.annotation.Viewable;
import adda.base.events.IModelPropertyChangeEvent;
import adda.base.models.IModel;
import adda.base.models.IModelObserver;
import adda.item.tab.TabEnumModel;
import adda.item.tab.base.size.SizeMeasureEnum;
import adda.item.tab.base.size.SizeModel;
import adda.item.tab.shape.surface.SurfaceModel;
import adda.utils.StringHelper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BeamModel extends TabEnumModel<BeamEnum> implements IModelObserver {

    public static final String BEAM = "beam";
    public static final String WIDTH_FIELD_NAME = "width";
    public static final String X_FIELD_NAME = "x";
    public static final String Y_FIELD_NAME = "y";
    public static final String Z_FIELD_NAME = "z";

    public BeamModel() {
        setLabel("Beam");//todo localization
        setEnumValue(BeamEnum.plane);
        setDefaultEnumValue(BeamEnum.plane);
    }

    @Override
    public void setEnumValue(BeamEnum enumValue) {
        super.setEnumValue(enumValue);

        if (enumValue == BeamEnum.plane) {
            setShowX(false);
            setShowY(false);
            setShowZ(false);
            setShowWidth(false);
            setShowIsShifted(false);
        } else if (enumValue == BeamEnum.dipole) {
            setShowX(true);
            setShowY(true);
            setShowZ(true);
            setShowWidth(false);
            setShowIsShifted(false);
        } else {
            setShowX(isShifted());
            setShowY(isShifted());
            setShowZ(isShifted());
            setShowWidth(true);
            setShowIsShifted(true);
        }

    }

    protected boolean isShowWidth = true;

    public boolean isShowWidth() {
        return isShowWidth;
    }



    public void setShowWidth(boolean isShowWidth) {
        if (this.isShowWidth != isShowWidth) {
            this.isShowWidth = isShowWidth;
            notifyObservers("isShowWidth", isShowWidth);
        }
    }

    @BindEnableFrom("isShowWidth")
    @Viewable(value = WIDTH_FIELD_NAME)
    protected double width = 1;

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        if (this.width != width) {
            this.width = width;
            notifyObservers(WIDTH_FIELD_NAME, width);
        }
    }


    protected boolean isShowIsShifted = true;

    public boolean isShowIsShifted() {
        return isShowIsShifted;
    }

    public void setShowIsShifted(boolean isShowIsShifted) {
        if (this.isShowIsShifted != isShowIsShifted) {
            this.isShowIsShifted = isShowIsShifted;
            notifyObservers("isShowIsShifted", isShowIsShifted);
        }
    }

    @BindEnableFrom("isShowIsShifted")
    @Viewable(value = "shifted")
    protected boolean isShifted = false;

    public boolean isShifted() {
        return isShifted;
    }

    public void setShifted(boolean isShifted) {
        if (this.isShifted != isShifted) {
            this.isShifted = isShifted;

            notifyObservers("isShifted", isShifted);

            setShowX(isShifted);
            setShowY(isShifted);
            setShowZ(isShifted);
        }
    }


    protected boolean isShowX = false;

    public boolean isShowX() {
        return isShowX;
    }

    public void setShowX(boolean isShowX) {
        if (this.isShowX != isShowX) {
            this.isShowX = isShowX;
            notifyObservers("isShowX", isShowX);
        }
    }

    @BindEnableFrom("isShowX")
    @Viewable(value = "x")
    protected double x = 0; 

    public double getX() {
        return x;
    }

    public void setX(double x) {
        if (this.x != x) {
            this.x = x;
            notifyObservers(X_FIELD_NAME, x);
        }
    }



    protected boolean isShowY = false;

    public boolean isShowY() {
        return isShowY;
    }

    public void setShowY(boolean isShowY) {
        if (this.isShowY != isShowY) {
            this.isShowY = isShowY;
            notifyObservers("isShowY", isShowY);
        }
    }

    @BindEnableFrom("isShowY")
    @Viewable(value = "y")
    protected double y = 0; 

    public double getY() {
        return y;
    }

    public void setY(double y) {
        if (this.y != y) {
            this.y = y;
            notifyObservers(Y_FIELD_NAME, y);
        }
    }




    protected boolean isShowZ = false;

    public boolean isShowZ() {
        return isShowZ;
    }

    public void setShowZ(boolean isShowZ) {
        if (this.isShowZ != isShowZ) {
            this.isShowZ = isShowZ;
            notifyObservers("isShowZ", isShowZ);
        }
    }

    @BindEnableFrom("isShowZ")
    @Viewable(value = "z")
    protected double z = 0; 

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        if (this.z != z) {
            this.z = z;
            notifyObservers(Z_FIELD_NAME, z);
        }
    }


//return Arrays.asList(new String[]{ StringHelper.toDisplayString(width),});
    public List<String> getParamsList() {
        if (enumValue == BeamEnum.plane) {
            return Collections.emptyList();
        } else if (enumValue == BeamEnum.dipole) {
            return Arrays.asList(StringHelper.toDisplayString(x), StringHelper.toDisplayString(y), StringHelper.toDisplayString(z));
        } else {
            if (isShifted) {
                return Arrays.asList(StringHelper.toDisplayString(width), StringHelper.toDisplayString(x), StringHelper.toDisplayString(y), StringHelper.toDisplayString(z));
            } else {
                return Collections.singletonList(StringHelper.toDisplayString(width));
            }
        }
    }


    @Override
    protected List<IAddaOption> getAddaOptionsInner() {
        final StringBuilder params = new StringBuilder().append(enumValue.toString());
        StringBuilder desc = new StringBuilder().append(StringHelper.toDisplayString(enumValue));
        if (enumValue == BeamEnum.dipole) {
            desc.append("; x=")
                            .append(StringHelper.toDisplayString(x)).append(measure)
                            .append(", y=")
                            .append(StringHelper.toDisplayString(y)).append(measure)
                            .append(", z=")
                            .append(StringHelper.toDisplayString(z)).append(measure);


            params.append(" ").append(String.join(" ", getParamsList()));

        } else if (enumValue != BeamEnum.plane) {
            desc.append("; width=")
                    .append(StringHelper.toDisplayString(width)).append(measure);
            if (isShifted()) {
                desc.append(", x=")
                        .append(StringHelper.toDisplayString(x)).append(measure)
                        .append(", y=")
                        .append(StringHelper.toDisplayString(y)).append(measure)
                        .append(", z=")
                        .append(StringHelper.toDisplayString(z)).append(measure);


            }
            params.append(" ").append(String.join(" ", getParamsList()));
        }
        return Arrays.asList(new AddaOption(BEAM, params.toString(), desc.toString()));
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

    public static final String MEASURE_FIELD_NAME = "measure";

    String measure = StringHelper.toDisplayString(SizeMeasureEnum.um);//todo sync with SizeModel


    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {

        if((this.measure != null && !this.measure.equals(measure)) || (this.measure == null && measure != null)) {
            this.measure = measure;
            notifyObservers(MEASURE_FIELD_NAME, measure);
        }
    }

    @Override
    public void modelPropertyChanged(IModel sender, IModelPropertyChangeEvent event) {
        if (sender instanceof SizeModel) {
            setMeasure(StringHelper.toDisplayString(((SizeModel) sender).getMeasure()));
        }
    }
}