package adda.item.tab.shape.orientation.avarage.gamma;

import adda.base.annotation.Viewable;
import adda.base.models.ModelBase;

public class GammaOrientationAverageModel extends ModelBase {

    public static final String MIN_FIELD_NAME = "min";
    public static final String MAX_FIELD_NAME = "max";
    @Viewable(value = "min:")
    protected double min = 5;

    @Viewable(value = "max:")
    protected double max = 5;


    public GammaOrientationAverageModel() {
        setLabel("gamma");
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        if (this.min != min) {
            this.min = min;
            notifyObservers(MIN_FIELD_NAME, min);
        }
    }


    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        if (this.max != max) {
            this.max = max;
            notifyObservers(MAX_FIELD_NAME, max);
        }
    }

}