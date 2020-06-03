package adda.item.tab.shape.selector.params.bisphere;

import adda.base.annotation.Viewable;
import adda.base.models.ModelBase;
import adda.item.tab.shape.selector.params.ModelShapeParam;

import java.util.Arrays;
import java.util.List;

public class BisphereModel extends ModelShapeParam {


    @Viewable(value = "Rcc/d:")
    protected double firstParam = 1;

    public double getFirstParam() {
        return firstParam;
    }

    public void setFirstParam(double firstParam) {
        if (this.firstParam != firstParam) {
            this.firstParam = firstParam;
            notifyObservers("firstParam", firstParam);
        }
    }



    public List<String> getParamsList() {
        return Arrays.asList(Double.toString(firstParam));
    }
}