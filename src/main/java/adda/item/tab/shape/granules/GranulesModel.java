package adda.item.tab.shape.granules;

import adda.base.AddaOption;
import adda.base.IAddaOption;
import adda.base.IAddaOptionsContainer;
import adda.base.annotation.Viewable;
import adda.base.events.IModelPropertyChangeEvent;
import adda.base.models.IModel;
import adda.base.models.IModelObserver;
import adda.base.models.ModelBase;
import adda.item.tab.base.refractiveIndex.RefractiveIndexModel;
import adda.item.tab.base.size.SizeMeasureEnum;
import adda.item.tab.base.size.SizeModel;
import adda.item.tab.output.granul.GranulSaveModel;
import adda.item.tab.shape.selector.ShapeSelectorModel;
import adda.utils.ReflectionHelper;
import adda.utils.StringHelper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GranulesModel extends RefractiveIndexModel implements IModelObserver, IAddaOptionsContainer {

    public static final String DOMAIN_DISPLAY_STRING_FIELD_NAME = "domainDisplayString";
    public static final String DOMAIN_NUMBER_FIELD_NAME = "domainNumber";
    public static final String IS_SAVE_FIELD_NAME = "isSave";
    public static final String DIAMETER_FIELD_NAME = "diameter";
    public static final String FRACTION_FIELD_NAME = "fraction";
    public static final String IS_USE_GRANUL_FIELD_NAME = "isUseGranul";
    public static final String GRANUL = "granul";
    public static final String COMMAND_LINE_VALUE_FORMAT = "%s %s %d";
    public static final String SEMICOLON_STR = ": ";
    public static final String SHAPE_DOMAIN_STR = "shape domain";
    public static final String MEASURE_FIELD_NAME = "measure";

    @Viewable
    protected boolean isUseGranul = false;

    protected boolean isSave = false;

    protected double fraction = 30;

    protected double diameter = 1.0;

    protected int domainNumber;

    protected String domainDisplayString;

    protected ShapeSelectorModel shapeModel;

    String measure = "um";//todo sync with SizeModel

    public GranulesModel() {
        setLabel("Granules");
        setEnabledAnisotrop(false);
        setAnisotrop(false);
    }

    @Override
    public boolean isDefaultState() {
        return !isUseGranul();
    }

    @Override
    public void applyDefaultState() {
        super.applyDefaultState();
        setUseGranul(false);
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {

        if((this.measure != null && !this.measure.equals(measure)) || (this.measure == null && measure != null)) {
            this.measure = measure;
            notifyObservers(MEASURE_FIELD_NAME, measure);
        }
    }

    public ShapeSelectorModel getShapeModel() {
        return shapeModel;
    }

    public void setShapeModel(ShapeSelectorModel shapeModel) {
        if (this.shapeModel != shapeModel) {

            if (this.shapeModel != null) {
                this.shapeModel.removeObserver(this);
            }
            this.shapeModel = shapeModel;
            this.shapeModel.addObserver(this);
            try {
                setDomainDisplayString(this.shapeModel.getShapeDomainInfos().get(0).getName());
                setDomainNumber(1);
            } catch (Exception ignored) {}


        }
    }


    public boolean validateFraction(StringBuilder builder) {
        if (fraction > 90 && fraction < 5) {
            return true;
        }

        builder.append("some error");
        return false;
    }


    public String getDomainDisplayString() {
        return domainDisplayString;
    }

    public void setDomainDisplayString(String domainDisplayString) {
        if(this.domainDisplayString == null || !this.domainDisplayString.equals(domainDisplayString)) {
            this.domainDisplayString = domainDisplayString;
            notifyObservers(DOMAIN_DISPLAY_STRING_FIELD_NAME, domainDisplayString);
        }
    }

    public int getDomainNumber() {
        return domainNumber;
    }

    public void setDomainNumber(int domainNumber) {
        if (this.domainNumber == 0 || this.domainNumber != domainNumber) {
            this.domainNumber = domainNumber;
            notifyObservers(DOMAIN_NUMBER_FIELD_NAME, domainNumber);
        }
    }

    public boolean isSave() {
        return isSave;
    }

    public void setSave(boolean usual) {
        if(isSave != usual) {
            this.isSave = usual;
            notifyObservers(IS_SAVE_FIELD_NAME, isSave);
        }
    }

    public double getDiameter() {
        return diameter;
    }

    public void setDiameter(double diameter) {
        if (this.diameter != diameter) {
            this.diameter = diameter;
            notifyObservers(DIAMETER_FIELD_NAME, diameter);
        }
    }

    public double getFraction() {
        return fraction;
    }

    public void setFraction(double fraction) {
        if (this.fraction != fraction) {
            this.fraction = fraction;
            notifyObservers(FRACTION_FIELD_NAME, fraction);
        }
    }

    public boolean isUseGranul() {
        return isUseGranul;
    }

    public void setUseGranul(boolean isUseGranul) {
        if (this.isUseGranul != isUseGranul) {
            this.isUseGranul = isUseGranul;
            notifyObservers(IS_USE_GRANUL_FIELD_NAME, isUseGranul);
        }
    }


    @Override
    public void modelPropertyChanged(IModel sender, IModelPropertyChangeEvent event) {
        super.modelPropertyChanged(sender, event);
        if (sender instanceof GranulSaveModel) {
            setSave(((GranulSaveModel) sender).getFlag());
        }
        if (sender.equals(shapeModel)) {

            if (this.shapeModel.getShapeDomainInfos().size() > 0) {
                if (this.shapeModel.getShapeDomainInfos().size() < getDomainNumber()) {
                    setDomainNumber(1);
                }
                setDomainDisplayString(this.shapeModel.getShapeDomainInfos().get(getDomainNumber() - 1).getName());
            }
        }
        if (sender instanceof SizeModel) {
            if (SizeMeasureEnum.k_1.equals(event.getPropertyValue())) {
                setMeasure(StringHelper.toDisplayString(event.getPropertyValue()));
            }
            if (SizeMeasureEnum.um.equals(event.getPropertyValue())) {
                setMeasure(StringHelper.toDisplayString(SizeMeasureEnum.um));
            }
        }

    }

    @Override
    protected List<IAddaOption> getAddaOptionsInner() {
        String displayString =
                new StringBuilder()
                        .append(StringHelper.toDisplayString(FRACTION_FIELD_NAME))
                        .append(SEMICOLON_STR)
                        .append(String.format("%d%%", (int)fraction))
                        .append(", ").append(StringHelper.toDisplayString(DIAMETER_FIELD_NAME))
                        .append(SEMICOLON_STR)
                        .append(diameter).append(" [").append(measure).append("]")
                        .append(", ")
                        .append(StringHelper.toDisplayString(SHAPE_DOMAIN_STR))
                        .append(SEMICOLON_STR)
                        .append(getDomainDisplayString())
                        .toString();

        String value = String.format(COMMAND_LINE_VALUE_FORMAT, StringHelper.toDisplayString(fraction * 0.01), StringHelper.toDisplayString(diameter), getDomainNumber());

        return Arrays.asList(new AddaOption(GRANUL, value, displayString));


    }
}