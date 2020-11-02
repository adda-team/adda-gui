package adda.item.tab.shape.selector.params.biellipsoid;

import adda.base.annotation.Viewable;
import adda.base.models.ModelBase;
import adda.item.tab.shape.selector.params.ModelShapeParam;
import adda.utils.StringHelper;

import java.util.Arrays;
import java.util.List;

public class BiellipsoidModel extends ModelShapeParam {


    @Viewable(value = "<html>y<sub>1</sub>/x<sub>1</sub></html>")
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



    @Viewable(value = "<html>z<sub>1</sub>/x<sub>1</sub></html>")
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



    @Viewable(value = "<html>x<sub>2</sub>/x<sub>1</sub></html>")
    protected double thirdParam = 1;

    public double getThirdParam() {
        return thirdParam;
    }

    public void setThirdParam(double thirdParam) {
        if (this.thirdParam != thirdParam) {
            this.thirdParam = thirdParam;
            notifyObservers(THIRD_PARAM, thirdParam);
        }
    }



    @Viewable(value = "<html>y<sub>2</sub>/x<sub>2</sub></html>")
    protected double fourthParam = 1;

    public double getFourthParam() {
        return fourthParam;
    }

    public void setFourthParam(double fourthParam) {
        if (this.fourthParam != fourthParam) {
            this.fourthParam = fourthParam;
            notifyObservers(FOURTH_PARAM, fourthParam);
        }
    }



    @Viewable(value = "<html>z<sub>2</sub>/x<sub>2</sub></html>")
    protected double fifthParam = 1;

    public double getFifthParam() {
        return fifthParam;
    }

    public void setFifthParam(double fifthParam) {
        if (this.fifthParam != fifthParam) {
            this.fifthParam = fifthParam;
            notifyObservers(FIFTH_PARAM, fifthParam);
        }
    }



    public List<String> getParamsList() {
        return Arrays.asList(StringHelper.toDisplayString(firstParam), StringHelper.toDisplayString(secondParam), StringHelper.toDisplayString(thirdParam), StringHelper.toDisplayString(fourthParam), StringHelper.toDisplayString(fifthParam));
    }

    @Override
    public boolean validate() {
        boolean isValid = true;

        if (firstParam > 0) {
            validationErrors.put(FIRST_PARAM, "");
        } else {
            validationErrors.put(FIRST_PARAM, StringHelper.toDisplayString("y<sub>1</sub>/x<sub>1</sub> must be greater than 0"));
            isValid = false;
        }

        if (secondParam > 0) {
            validationErrors.put(SECOND_PARAM, "");
        } else {
            validationErrors.put(SECOND_PARAM, StringHelper.toDisplayString("z<sub>1</sub>/x<sub>1</sub> must be greater than 0"));
            isValid = false;
        }

        if (thirdParam > 0) {
            validationErrors.put(THIRD_PARAM, "");
        } else {
            validationErrors.put(THIRD_PARAM, StringHelper.toDisplayString("x<sub>2</sub>/x<sub>1</sub> must be greater than 0"));
            isValid = false;
        }

        if (fourthParam > 0) {
            validationErrors.put(FOURTH_PARAM, "");
        } else {
            validationErrors.put(FOURTH_PARAM, StringHelper.toDisplayString("y<sub>2</sub>/x<sub>2</sub> must be greater than 0"));
            isValid = false;
        }

        if (fifthParam > 0) {
            validationErrors.put(FIFTH_PARAM, "");
        } else {
            validationErrors.put(FIFTH_PARAM, StringHelper.toDisplayString("z<sub>2</sub>/x<sub>2</sub> must be greater than 0"));
            isValid = false;
        }

        return isValid;
    }

}