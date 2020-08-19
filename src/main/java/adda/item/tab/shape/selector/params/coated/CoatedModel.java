package adda.item.tab.shape.selector.params.coated;

import adda.base.annotation.BindEnableFrom;
import adda.base.annotation.Viewable;
import adda.base.models.ModelBase;
import adda.item.tab.shape.selector.params.ModelShapeParam;
import adda.utils.StringHelper;

import java.util.Arrays;
import java.util.List;

public class CoatedModel extends ModelShapeParam {


    @Viewable(value = "Din/d:")
    protected double firstParam = 0.5;

    public double getFirstParam() {
        return firstParam;
    }

    public void setFirstParam(double firstParam) {
        if (this.firstParam != firstParam) {
            this.firstParam = firstParam;
            notifyObservers("firstParam", firstParam);
        }
    }



    @Viewable(value = "use [x/d]")
    protected boolean isShowSecondParam = false;

    public boolean isShowSecondParam() {
        return isShowSecondParam;
    }

    public void setShowSecondParam(boolean isShowSecondParam) {
        if (this.isShowSecondParam != isShowSecondParam) {
            this.isShowSecondParam = isShowSecondParam;
            notifyObservers("isShowSecondParam", isShowSecondParam);
        }
    }

    @BindEnableFrom("isShowSecondParam")
    @Viewable(value = "x/d:")
    protected double secondParam = 0;

    public double getSecondParam() {
        return secondParam;
    }

    public void setSecondParam(double secondParam) {
        if (this.secondParam != secondParam) {
            this.secondParam = secondParam;
            notifyObservers("secondParam", secondParam);
        }
    }



    @Viewable(value = "use [y/d]:")
    protected boolean isShowThirdParam = false;

    public boolean isShowThirdParam() {
        return isShowThirdParam;
    }

    public void setShowThirdParam(boolean isShowThirdParam) {
        if (this.isShowThirdParam != isShowThirdParam) {
            this.isShowThirdParam = isShowThirdParam;
            notifyObservers("isShowThirdParam", isShowThirdParam);
        }
    }

    @BindEnableFrom("isShowThirdParam")
    @Viewable(value = "y/d:")
    protected double thirdParam = 0;

    public double getThirdParam() {
        return thirdParam;
    }

    public void setThirdParam(double thirdParam) {
        if (this.thirdParam != thirdParam) {
            this.thirdParam = thirdParam;
            notifyObservers("thirdParam", thirdParam);
        }
    }



    @Viewable(value = "use [z/d]")
    protected boolean isShowFourthParam = false;

    public boolean isShowFourthParam() {
        return isShowFourthParam;
    }

    public void setShowFourthParam(boolean isShowFourthParam) {
        if (this.isShowFourthParam != isShowFourthParam) {
            this.isShowFourthParam = isShowFourthParam;
            notifyObservers("isShowFourthParam", isShowFourthParam);
        }
    }

    @BindEnableFrom("isShowFourthParam")
    @Viewable(value = "z/d:")
    protected double fourthParam = 0;

    public double getFourthParam() {
        return fourthParam;
    }

    public void setFourthParam(double fourthParam) {
        if (this.fourthParam != fourthParam) {
            this.fourthParam = fourthParam;
            notifyObservers("fourthParam", fourthParam);
        }
    }



    public List<String> getParamsList() {
        if (isShowSecondParam || isShowThirdParam || isShowFourthParam) {
            return Arrays.asList(StringHelper.toDisplayString(firstParam), StringHelper.toDisplayString(secondParam), StringHelper.toDisplayString(thirdParam), StringHelper.toDisplayString(fourthParam));
        }
        return Arrays.asList(StringHelper.toDisplayString(firstParam));
    }
}