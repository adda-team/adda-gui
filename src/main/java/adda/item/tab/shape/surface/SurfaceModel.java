package adda.item.tab.shape.surface;

import adda.Context;
import adda.base.AddaOption;
import adda.base.IAddaOption;
import adda.base.annotation.BindEnableFrom;
import adda.base.annotation.Viewable;
import adda.base.events.IModelPropertyChangeEvent;
import adda.base.models.IModel;
import adda.base.models.IModelObserver;
import adda.base.models.ModelBaseAddaOptionsContainer;
import adda.item.tab.base.beam.BeamEnum;
import adda.item.tab.base.beam.BeamModel;
import adda.item.tab.base.size.SizeMeasureEnum;
import adda.item.tab.base.size.SizeModel;
import adda.item.tab.internals.initialField.InitialFieldEnum;
import adda.item.tab.internals.initialField.InitialFieldModel;
import adda.item.tab.output.radiationForce.RadiationForceSaveModel;
import adda.utils.StringHelper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SurfaceModel extends ModelBaseAddaOptionsContainer implements IModelObserver {

    public SurfaceModel() {
        isVisibleIfDisabled = true;
    }

    public static final String MEASURE_FIELD_NAME = "measure";
    public static final String IS_USE_SURFACE_FIELD_NAME = "isUseSurface";
    public static final String IS_USE_SURFACE_ENABLED_FIELD_NAME = "isUseSurfaceEnabled";
    public static final String DISTANCE_FIELD_NAME = "distance";
    public static final String IS_INFINITE_FIELD_NAME = "isInfinite";
    public static final String REAL_PART_FIELD_NAME = "realPart";
    public static final String IMAG_PART_FIELD_NAME = "imagPart";
    public static final String SEMICOLON_STR = ": ";
    public static final String COMPLEX_FORMAT = "%s + i%s";
    public static final String SURF = "surf";
    public static final String DELIMITER = " ";
    public static final String INF = "inf";

    @BindEnableFrom("isUseSurfaceEnabled")
    @Viewable
    protected boolean isUseSurface = false;

    protected boolean isUseSurfaceEnabled = true;
    protected boolean isInfinite = true;
    
    protected double distance = 1;
    protected double realPart = 1.5;
    protected double imagPart = 0.00001;


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

    public boolean isUseSurface() {
        return isUseSurface;
    }

    public void setUseSurface(boolean isUseSurface) {
        if (this.isUseSurface != isUseSurface) {
            this.isUseSurface = isUseSurface;
            notifyObservers(IS_USE_SURFACE_FIELD_NAME, isUseSurface);
        }
    }

    public boolean isUseSurfaceEnabled() {
        return isUseSurfaceEnabled;
    }

    public void setUseSurfaceEnabled(boolean isUseSurfaceEnabled) {
        if (this.isUseSurfaceEnabled != isUseSurfaceEnabled) {
            this.isUseSurfaceEnabled = isUseSurfaceEnabled;
            notifyObservers(IS_USE_SURFACE_ENABLED_FIELD_NAME, isUseSurfaceEnabled);
        }
    }


    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        if (this.distance != distance) {
            this.distance = distance;
            notifyObservers(DISTANCE_FIELD_NAME, distance);
        }
    }



    public boolean isInfinite() {
        return isInfinite;
    }

    public void setInfinite(boolean isInfinite) {
        if (this.isInfinite != isInfinite) {
            this.isInfinite = isInfinite;
            notifyObservers(IS_INFINITE_FIELD_NAME, isInfinite);
        }
    }



    public double getRealPart() {
        return realPart;
    }

    public void setRealPart(double realPart) {
        if (this.realPart != realPart) {
            this.realPart = realPart;
            notifyObservers(REAL_PART_FIELD_NAME, realPart);
        }
    }


    public double getImagPart() {
        return imagPart;
    }

    public void setImagPart(double imagPart) {
        if (this.imagPart != imagPart) {
            this.imagPart = imagPart;
            notifyObservers(IMAG_PART_FIELD_NAME, imagPart);
        }
    }

    @Override
    public boolean isDefaultState() {
        return !isUseSurface;
    }

    @Override
    public void applyDefaultState() {
        super.applyDefaultState();
        setUseSurface(false);
    }

    public List<String> getParamsList() {
        if (isInfinite) {
            return Arrays.asList(StringHelper.toDisplayString(distance), INF);
        }
        return Arrays.asList(StringHelper.toDisplayString(distance), StringHelper.toDisplayString(realPart), StringHelper.toDisplayString(imagPart));
    }

    @Override
    protected List<IAddaOption> getAddaOptionsInner() {

        String displayString =
                new StringBuilder()
                        .append(StringHelper.toDisplayString("Distance from particle center"))
                        .append(SEMICOLON_STR)
                        .append(StringHelper.toDisplayString(distance)).append(measure)
                        .append(", ")
                        .append(StringHelper.toDisplayString("Refractive index"))
                        .append(SEMICOLON_STR)
                        .append(isInfinite ? StringHelper.toDisplayString("Infinite") : String.format(COMPLEX_FORMAT, StringHelper.toDisplayString(realPart), StringHelper.toDisplayString(imagPart) ))
                        .toString();

        return Collections.singletonList(new AddaOption(SURF, String.join(DELIMITER, getParamsList()), displayString));
    }


    @Override
    public boolean validate() {
        RadiationForceSaveModel radiationForceSaveModel = (RadiationForceSaveModel) Context.getInstance().getChildModelFromSelectedBox(RadiationForceSaveModel.class);
        BeamModel beamModel = (BeamModel) Context.getInstance().getChildModelFromSelectedBox(BeamModel.class);
        InitialFieldModel initialFieldModel = (InitialFieldModel) Context.getInstance().getChildModelFromSelectedBox(InitialFieldModel.class);

        String error = "<html>";
        boolean isValid = true;
        if (radiationForceSaveModel.getFlag()) {
            isValid = false;
            error += "<br>" + StringHelper.toDisplayString("Radiation Force does`t compatible with 'surface' option");
        }
        if (beamModel.getEnumValue() != BeamEnum.plane) {
            isValid = false;
            error += "<br>" + StringHelper.toDisplayString("Non plane beam does`t compatible with 'surface' option");
        }
        if (initialFieldModel.getEnumValue() == InitialFieldEnum.wkb) {
            isValid = false;
            error += "<br>" + StringHelper.toDisplayString("WKB initial field for the iterative solver  does`t compatible with 'surface' option");
        }
        error += "</html>";

        error = error.replaceAll("<html></html>", "");
        error = error.replaceFirst("<br>", "");

        validationErrors.put(IS_USE_SURFACE_ENABLED_FIELD_NAME, error);

        return isValid;
    }

    @Override
    public void modelPropertyChanged(IModel sender, IModelPropertyChangeEvent event) {
        if (sender instanceof RadiationForceSaveModel || sender instanceof BeamModel || sender instanceof InitialFieldModel) {
            setUseSurfaceEnabled(validate() || isUseSurface());
        }
        if (sender instanceof SizeModel) {
            setMeasure(StringHelper.toDisplayString(((SizeModel) sender).getMeasure()));
        }
    }
}