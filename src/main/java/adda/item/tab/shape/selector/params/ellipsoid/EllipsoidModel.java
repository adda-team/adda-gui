package adda.item.tab.shape.selector.params.ellipsoid;

import adda.base.annotation.Viewable;
import adda.base.models.ModelBase;
import adda.item.tab.shape.selector.params.ModelShapeParam;
import adda.utils.StringHelper;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EllipsoidModel extends ModelShapeParam {


    @Viewable(value = "y/x")
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

    double invsqY;
    double invsqZ;

    @Override
    public void initParams() {
        invsqY = 1/(firstParam*firstParam);
        invsqZ = 1/(secondParam*secondParam);
    }

    @Override
    public int getBoxY(int boxX, double rectScaleX, double rectScaleY, double rectScaleZ, int jagged) {
        return fitBox_yz(firstParam*boxX * (rectScaleX / rectScaleY), jagged);
    }

    @Override
    public int getBoxZ(int boxX, double rectScaleX, double rectScaleY, double rectScaleZ, int jagged) {
        return fitBox_yz(secondParam * boxX * (rectScaleX / rectScaleZ), jagged);
    }

    @Override
    public List<Double> getSurfacePoints() {
        int countOfStep = 301;

        double step = 1.0 / countOfStep;

        ArrayList<Double> x = new ArrayList<>();
        ArrayList<Double> y = new ArrayList<>();
        ArrayList<Double> z = new ArrayList<>();


        final double d = 0.5;
        double currentZ, r2;
        double currentXSnapshot, currentYSnapshot;
        for (double currentX = -d; currentX < d; currentX += step) {
            for (double currentY = -d*firstParam; currentY < d*firstParam; currentY += step) {
                double[] buffer = new double[12];
                currentXSnapshot = currentX;
                currentYSnapshot = currentY;
                for (int inner = 0; inner < 4; inner++) {
                    if (inner == 1) currentX += step;
                    if (inner == 2) currentY += step;
                    if (inner == 3) currentX -= step;
                    r2 = currentX * currentX + currentY * currentY*invsqY;

                    if (Math.sqrt(r2) < d) {
                        currentZ = Math.sqrt((0.25 - r2)/invsqZ);

                        buffer[3 * inner] = currentX;
                        buffer[3 * inner + 1] = currentY;
                        buffer[3 * inner + 2] = currentZ;

                    } else {
//                        double alpha = Math.atan(Math.abs(currentY / currentX));
//                        double r = Math.sqrt(r2);
                        buffer[3 * inner] = currentX;
                        buffer[3 * inner + 1] = currentX * currentX > 0.25 ? 0 : Math.signum(currentY) * Math.sqrt((0.25 - currentX * currentX)/invsqY);
                        buffer[3 * inner + 2] = 0;
                    }
                }
                for (int index = 0; index < 4; index++) {

                    x.add(buffer[3 * index]);
                    y.add(buffer[3 * index + 1]);
                    z.add(buffer[3 * index + 2]);
                }

                currentX = currentXSnapshot;
                currentY = currentYSnapshot;
            }

        }
        ArrayList<Double> points = new ArrayList<Double>();
        for (int i = 0; i < x.size(); i++) {
            points.add(x.get(i));
            points.add(y.get(i));
            points.add(z.get(i));
        }

        for (int i = x.size() - 1; i >= 0; i--) {
            points.add(x.get(i));
            points.add(y.get(i));
            points.add(-z.get(i));
        }

        return points;
    }






    @Override
    protected boolean isPointInsideShapeVolume(double xr, double yr, double zr) {
        return xr*xr+yr*yr*invsqY+zr*zr*invsqZ<=0.25;
    }
}