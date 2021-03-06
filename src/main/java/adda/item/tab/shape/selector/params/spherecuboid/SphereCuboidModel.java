package adda.item.tab.shape.selector.params.spherecuboid;

import adda.base.annotation.Viewable;
import adda.base.models.ModelBase;
import adda.item.tab.shape.selector.params.ModelShapeParam;
import adda.utils.StringHelper;

import java.util.Arrays;
import java.util.List;

public class SphereCuboidModel extends ModelShapeParam {

    @Viewable(value = "<html>d<sub>sph</sub>/d")
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

    public List<String> getParamsList() {
        return Arrays.asList(StringHelper.toDisplayString(firstParam));
    }


    @Override
    public boolean validate() {
        boolean isValid = true;

        if (firstParam >= 0 && firstParam <= 1) {
            validationErrors.put(FIRST_PARAM, "");
        } else {
            validationErrors.put(FIRST_PARAM, StringHelper.toDisplayString("d<sub>sph</sub>/d must be in [0; 1]"));
            isValid = false;
        }

        return isValid;
    }
}