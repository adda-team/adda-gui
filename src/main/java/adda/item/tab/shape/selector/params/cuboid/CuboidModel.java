package adda.item.tab.shape.selector.params.cuboid;

import adda.base.annotation.BindEnableFrom;
import adda.base.annotation.Viewable;
import adda.base.models.ModelBase;
import adda.item.tab.shape.selector.params.ModelShapeParam;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CuboidModel extends ModelShapeParam {


    @Viewable(value = "use [y/x]")
    protected boolean isShowFirstParam = false;

    public boolean isShowFirstParam() {
        return isShowFirstParam;
    }

    public void setShowFirstParam(boolean isShowFirstParam) {
        if (this.isShowFirstParam != isShowFirstParam) {
            this.isShowFirstParam = isShowFirstParam;
            notifyObservers("isShowFirstParam", isShowFirstParam);
//            if (!isShowFirstParam) {
//                setShowSecondParam(false);
//            }
        }
    }

    @BindEnableFrom("isShowFirstParam")
    @Viewable(value = "y/x:")
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


//    @BindEnableFrom("isShowFirstParam")
    @Viewable(value = "use [z/x]")
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
    @Viewable(value = "z/x:")
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
        if (isShowFirstParam || isShowSecondParam) {
            return Arrays.asList(Double.toString(firstParam), Double.toString(secondParam));
        }
        return Collections.emptyList();
    }
}