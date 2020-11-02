package adda.item.tab.shape.selector.params.chebyshev;

import adda.base.annotation.Viewable;
import adda.base.models.ModelBase;
import adda.item.tab.shape.selector.params.ModelShapeParam;
import adda.utils.StringHelper;

import java.util.Arrays;
import java.util.List;

public class ChebyshevModel extends ModelShapeParam {


    @Viewable(value = "\u03B5")
    protected double firstParam = 0.5;

    public double getFirstParam() {
        return firstParam;
    }

    public void setFirstParam(double firstParam) {
        if (this.firstParam != firstParam) {
            this.firstParam = firstParam;
            notifyObservers(FIRST_PARAM, firstParam);
        }
    }



    @Viewable(value = "n")
    protected int secondParam = 5;

    public int getSecondParam() {
        return secondParam;
    }

    public void setSecondParam(int secondParam) {
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

        if (firstParam <= 1 && firstParam >= -1) {
            validationErrors.put(FIRST_PARAM, "");
        } else {
            validationErrors.put(FIRST_PARAM,  StringHelper.toDisplayString("\u03B5 must be in [-1; 1]"));
            isValid = false;
        }

        if (secondParam > 0) {
            validationErrors.put(SECOND_PARAM, "");
        } else {
            validationErrors.put(SECOND_PARAM,  StringHelper.toDisplayString("n must be greater than 0"));
            isValid = false;
        }

        return isValid;
    }
}