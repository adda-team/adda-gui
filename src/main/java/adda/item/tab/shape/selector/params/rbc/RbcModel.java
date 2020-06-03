package adda.item.tab.shape.selector.params.rbc;

import adda.base.annotation.Viewable;
import adda.base.models.ModelBase;
import adda.item.tab.shape.selector.params.ModelShapeParam;

import java.util.Arrays;
import java.util.List;

public class RbcModel extends ModelShapeParam {


    @Viewable(value = "h/d:")
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



    @Viewable(value = "b/d:")
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



    @Viewable(value = "c/d:")
    protected double thirdParam = 1;

    public double getThirdParam() {
        return thirdParam;
    }

    public void setThirdParam(double thirdParam) {
        if (this.thirdParam != thirdParam) {
            this.thirdParam = thirdParam;
            notifyObservers("thirdParam", thirdParam);
        }
    }


    public List<String> getParamsList() {
        return Arrays.asList(Double.toString(firstParam), Double.toString(secondParam), Double.toString(thirdParam));
    }
}