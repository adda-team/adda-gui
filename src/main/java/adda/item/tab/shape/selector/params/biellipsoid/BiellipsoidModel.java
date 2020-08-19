package adda.item.tab.shape.selector.params.biellipsoid;

import adda.base.annotation.Viewable;
import adda.base.models.ModelBase;
import adda.item.tab.shape.selector.params.ModelShapeParam;
import adda.utils.StringHelper;

import java.util.Arrays;
import java.util.List;

public class BiellipsoidModel extends ModelShapeParam {

    @Viewable(value = "y1/x1:")
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



    @Viewable(value = "z1/x1:")
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



    @Viewable(value = " x2/x1:")
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



    @Viewable(value = "y2/x2:")
    protected double fourthParam = 1;

    public double getFourthParam() {
        return fourthParam;
    }

    public void setFourthParam(double fourthParam) {
        if (this.fourthParam != fourthParam) {
            this.fourthParam = fourthParam;
            notifyObservers("fourthParam", fourthParam);
        }
    }



    @Viewable(value = " z2/x2:")
    protected double fifthParam = 1;

    public double getFifthParam() {
        return fifthParam;
    }

    public void setFifthParam(double fifthParam) {
        if (this.fifthParam != fifthParam) {
            this.fifthParam = fifthParam;
            notifyObservers("fifthParam", fifthParam);
        }
    }



    public List<String> getParamsList() {
        return Arrays.asList(StringHelper.toDisplayString(firstParam), StringHelper.toDisplayString(secondParam), StringHelper.toDisplayString(thirdParam), StringHelper.toDisplayString(fourthParam), StringHelper.toDisplayString(fifthParam));
    }

}