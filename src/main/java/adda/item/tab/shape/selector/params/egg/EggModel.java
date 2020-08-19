package adda.item.tab.shape.selector.params.egg;

import adda.base.annotation.Viewable;
import adda.base.models.ModelBase;
import adda.item.tab.shape.selector.params.ModelShapeParam;
import adda.utils.StringHelper;

import java.util.Arrays;
import java.util.List;

public class EggModel extends ModelShapeParam {


    @Viewable(value = "ε:")
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



    @Viewable(value = "ν:")
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
        return Arrays.asList(StringHelper.toDisplayString(firstParam), StringHelper.toDisplayString(secondParam));
    }
}