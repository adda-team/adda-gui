package adda.item.tab.base.lambda;

import adda.base.AddaOption;
import adda.base.IAddaOption;
import adda.base.annotation.Viewable;
import adda.base.events.IModelPropertyChangeEvent;
import adda.base.models.IModel;
import adda.base.models.IModelObserver;
import adda.base.models.ModelBase;
import adda.base.models.ModelBaseAddaOptionsContainer;
import adda.item.tab.base.size.SizeMeasureEnum;
import adda.item.tab.base.size.SizeModel;
import adda.utils.StringHelper;

import java.util.Arrays;
import java.util.List;

public class LambdaModel extends ModelBaseAddaOptionsContainer implements IModelObserver {

    private static final double DEFAULT_LAMBDA = 3.14159 * 2; // 2pi
    public static final String LAMBDA_FIELD_NAME = "lambda";
    public static final String MEASURE_FIELD_NAME = "measure";
    public static final String IS_ENABLED_FIELD_NAME = "isEnabled";
    public static final String FORMAT = "%s %s";
    @Viewable
    double lambda = DEFAULT_LAMBDA;

    double previousLambda = DEFAULT_LAMBDA;

    String measure =  StringHelper.toDisplayString(SizeMeasureEnum.um);//todo sync with SizeModel

    boolean isEnabled = true;//todo sync with SizeModel

    public LambdaModel() {
        this.setLabel("Lambda");//todo localization
    }

    public double getLambda() {
        return lambda;
    }

    public void setLambda(double lambda) {
        if (this.lambda != lambda) {
            this.lambda = lambda;
            notifyObservers(LAMBDA_FIELD_NAME, lambda);
        }
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
    public void modelPropertyChanged(IModel sender, IModelPropertyChangeEvent event) {
        if (sender instanceof SizeModel) {
            if (SizeMeasureEnum.k_1.equals(event.getPropertyValue())) {
                setMeasure(StringHelper.toDisplayString(SizeMeasureEnum.k_1));
                setEnabled(false);
                previousLambda = lambda;
                setLambda(DEFAULT_LAMBDA);
            }
            if (SizeMeasureEnum.um.equals(event.getPropertyValue())) {
                setMeasure(StringHelper.toDisplayString(SizeMeasureEnum.um));
                setEnabled(true);
                setLambda(previousLambda);
            }
        }
    }

    @Override
    public boolean isDefaultState() {
        return lambda == DEFAULT_LAMBDA;
    }

    @Override
    public void applyDefaultState() {
        setLambda(DEFAULT_LAMBDA);// 2pi
    }

    @Override
    protected List<IAddaOption> getAddaOptionsInner() {
        final String displayLambda = StringHelper.toDisplayString(lambda);
        IAddaOption addaOption = new AddaOption(LAMBDA_FIELD_NAME, displayLambda, String.format(FORMAT, displayLambda, measure));
        return Arrays.asList(addaOption);
    }
}