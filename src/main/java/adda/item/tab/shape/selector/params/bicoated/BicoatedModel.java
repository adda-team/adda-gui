package adda.item.tab.shape.selector.params.bicoated;

import adda.base.annotation.Viewable;
import adda.base.models.ModelBase;
import adda.item.tab.shape.selector.params.ModelShapeParam;
import adda.utils.StringHelper;

import java.util.Arrays;
import java.util.List;

public class BicoatedModel extends ModelShapeParam {

    @Viewable(value = "<html>R<sub>cc</sub>/d</html>")
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


    @Viewable(value = "<html>d<sub>in</sub>/d</html>")
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
        if (secondParam <= 0 || secondParam > 1) {
            validationErrors.put(SECOND_PARAM, StringHelper.toDisplayString("d<sub>in</sub>/d must be from 0 to 1"));
            isValid = false;
        } else {
            validationErrors.put(SECOND_PARAM, "");
        }

        if (firstParam < 1) {
            validationErrors.put(FIRST_PARAM, StringHelper.toDisplayString("R<sub>cc</sub>/d must be greater than or equal 1"));
            isValid = false;
        } else {
            validationErrors.put(FIRST_PARAM, "");
        }

        return isValid;
    }
}