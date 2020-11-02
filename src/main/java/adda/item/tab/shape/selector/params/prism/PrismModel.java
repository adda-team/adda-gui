package adda.item.tab.shape.selector.params.prism;

import adda.base.annotation.Viewable;
import adda.base.models.ModelBase;
import adda.item.tab.shape.selector.params.ModelShapeParam;
import adda.utils.StringHelper;

import java.util.Arrays;
import java.util.List;

public class PrismModel extends ModelShapeParam {

    @Viewable(value = "n")
    protected int firstParam = 3;

    public int getFirstParam() {
        return firstParam;
    }

    public void setFirstParam(int firstParam) {
        if (this.firstParam != firstParam) {
            this.firstParam = firstParam;
            notifyObservers(FIRST_PARAM, firstParam);
        }
    }



    @Viewable(value = "h/d")
    protected double secondParam = 1;

    public double getSecondParam() {
        return secondParam;
    }

    public void setSecondParam(double secondParam) {
        if (this.secondParam != secondParam) {
            this.secondParam = secondParam;
            notifyObservers(SECOND_PARAM, secondParam);
        }
    }



    public List<String> getParamsList() {
        return Arrays.asList(StringHelper.toDisplayString(firstParam), StringHelper.toDisplayString(secondParam));
    }

    @Override
    public boolean validate() {
        boolean isValid = true;

        if (secondParam > 0) {
            validationErrors.put(SECOND_PARAM, "");
        } else {
            validationErrors.put(SECOND_PARAM,  StringHelper.toDisplayString("h/d must be greater than 0"));
            isValid = false;
        }

        return isValid;
    }
}