package adda.item.tab.shape.selector.params.prism;

import adda.base.annotation.Viewable;
import adda.base.models.ModelBase;
import adda.item.tab.shape.selector.params.ModelShapeParam;

import java.util.Arrays;
import java.util.List;

public class PrismModel extends ModelShapeParam {

    @Viewable(value = "n:")
    protected double firstParam = 3;

    public double getFirstParam() {
        return firstParam;
    }

    public void setFirstParam(double firstParam) {
        if (this.firstParam != firstParam) {
            this.firstParam = firstParam;
            notifyObservers("firstParam", firstParam);
        }
    }



    @Viewable(value = "h/Dx:")
    protected double secondParam = 1;

    public double getSecondParam() {
        return secondParam;
    }

    public void setSecondParam(double secondParam) {
        if (this.secondParam != secondParam) {
            this.secondParam = secondParam;
            notifyObservers("secondParam", secondParam);
        }
    }



    public List<String> getParamsList() {
        return Arrays.asList(Double.toString(firstParam), Double.toString(secondParam));
    }
}