package adda.item.tab.shape.selector.params.bisphere;

import adda.base.annotation.Viewable;
import adda.base.models.ModelBase;
import adda.item.tab.shape.selector.params.ModelShapeParam;
import adda.utils.StringHelper;

import java.util.Arrays;
import java.util.List;

public class BisphereModel extends ModelShapeParam {


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


    public List<String> getParamsList() {
        return Arrays.asList(StringHelper.toDisplayString(firstParam));
    }

    @Override
    public boolean validate() {
        boolean isValid = true;
        if (firstParam < 1) {
            validationErrors.put(FIRST_PARAM,  StringHelper.toDisplayString("R<sub>cc</sub>/d must be greater than or equal 1"));
            isValid = false;
        } else {
            validationErrors.put(FIRST_PARAM, "");
        }
        return isValid;
    }
}