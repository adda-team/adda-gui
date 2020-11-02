package adda.item.tab.shape.selector.params.ellipsoid;

import adda.base.annotation.Viewable;
import adda.base.models.ModelBase;
import adda.item.tab.shape.selector.params.ModelShapeParam;
import adda.utils.StringHelper;

import java.util.Arrays;
import java.util.List;

public class EllipsoidModel extends ModelShapeParam {


    @Viewable(value = "y/x")
    protected double firstParam = 1;

    public double getFirstParam() {
        return firstParam;
    }

    public void setFirstParam(double firstParam) {
        if (this.firstParam != firstParam) {
            this.firstParam = firstParam;
            notifyObservers(FIRST_PARAM, firstParam);
        }
    }



    @Viewable(value = "z/x")
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

        if (firstParam > 0) {
            validationErrors.put(FIRST_PARAM, "");
        } else {
            validationErrors.put(FIRST_PARAM,  StringHelper.toDisplayString("y/x must be greater than 0"));
            isValid = false;
        }

        if (secondParam > 0) {
            validationErrors.put(SECOND_PARAM, "");
        } else {
            validationErrors.put(SECOND_PARAM,  StringHelper.toDisplayString("z/x must be greater than 0"));
            isValid = false;
        }

        return isValid;
    }
}