package adda.item.tab.shape.selector.params.egg;

import adda.base.annotation.Viewable;
import adda.base.models.ModelBase;
import adda.item.tab.shape.selector.params.ModelShapeParam;
import adda.utils.StringHelper;

import java.util.Arrays;
import java.util.List;

public class EggModel extends ModelShapeParam {


    @Viewable(value = "\u03B5")
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



    @Viewable(value = "\u03BD")
    protected double secondParam = 0.5;

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


        if (firstParam > secondParam) {
            if (firstParam >= 0 && firstParam <= 1) {
                validationErrors.put(FIRST_PARAM, "");
            } else {
                validationErrors.put(FIRST_PARAM,  StringHelper.toDisplayString("\u03B5 must be in [0; 1]"));
                isValid = false;
            }

            if (secondParam >= 0 && secondParam <= 1) {
                validationErrors.put(SECOND_PARAM, "");
            } else {
                validationErrors.put(SECOND_PARAM,  StringHelper.toDisplayString("\u03BD must be in [0; 1]"));
                isValid = false;
            }
        } else {
            validationErrors.put(FIRST_PARAM,  StringHelper.toDisplayString("\u03BD must be less than \u03B5"));
            validationErrors.put(SECOND_PARAM,  StringHelper.toDisplayString("\u03BD must be less than \u03B5"));
            isValid = false;
        }




        return isValid;
    }
}