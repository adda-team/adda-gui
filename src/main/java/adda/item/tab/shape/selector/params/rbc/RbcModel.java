package adda.item.tab.shape.selector.params.rbc;

import adda.base.annotation.Viewable;
import adda.base.models.ModelBase;
import adda.item.tab.shape.selector.params.ModelShapeParam;
import adda.utils.StringHelper;

import java.util.Arrays;
import java.util.List;

public class RbcModel extends ModelShapeParam {


    @Viewable(value = "h/d")
    protected double firstParam = 0.35;

    public double getFirstParam() {
        return firstParam;
    }

    public void setFirstParam(double firstParam) {
        if (this.firstParam != firstParam) {
            this.firstParam = firstParam;
            notifyObservers(FIRST_PARAM, firstParam);
        }
    }



    @Viewable(value = "b/d")
    protected double secondParam = 0.2;

    public double getSecondParam() {
        return secondParam;
    }

    public void setSecondParam(double secondParam) {
        if (this.secondParam != secondParam) {
            this.secondParam = secondParam;
            notifyObservers(SECOND_PARAM, secondParam);
        }
    }



    @Viewable(value = "c/d")
    protected double thirdParam = 0.65;

    public double getThirdParam() {
        return thirdParam;
    }

    public void setThirdParam(double thirdParam) {
        if (this.thirdParam != thirdParam) {
            this.thirdParam = thirdParam;
            notifyObservers(THIRD_PARAM, thirdParam);
        }
    }


    public List<String> getParamsList() {
        return Arrays.asList(StringHelper.toDisplayString(firstParam), StringHelper.toDisplayString(secondParam), StringHelper.toDisplayString(thirdParam));
    }


    @Override
    public boolean validate() {
        boolean isValid = true;

        if (firstParam > secondParam) {
            if (firstParam > 0) {
                validationErrors.put(FIFTH_PARAM, "");
            } else {
                validationErrors.put(FIFTH_PARAM,  StringHelper.toDisplayString("n must be greater than 0"));
                isValid = false;
            }

            if (secondParam > 0) {
                validationErrors.put(SECOND_PARAM, "");
            } else {
                validationErrors.put(SECOND_PARAM,  StringHelper.toDisplayString("n must be greater than 0"));
                isValid = false;
            }
        } else {
            validationErrors.put(FIRST_PARAM,  StringHelper.toDisplayString("h/d must be greater than b/d"));
            validationErrors.put(SECOND_PARAM,  StringHelper.toDisplayString("h/d must be greater than b/d"));
            isValid = false;
        }


        if (thirdParam > 0 && thirdParam < 1) {
            validationErrors.put(THIRD_PARAM, "");
        } else {
            validationErrors.put(THIRD_PARAM,  StringHelper.toDisplayString("c/d must be in (0; 1)"));
            isValid = false;
        }



        return isValid;
    }
}