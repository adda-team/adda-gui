package adda.item.tab.shape.selector.params.rbc;

import adda.base.annotation.Viewable;
import adda.item.tab.shape.selector.params.ModelShapeParam;
import adda.utils.StringHelper;
import org.jogamp.java3d.utils.geometry.GeometryInfo;
import org.jogamp.java3d.utils.geometry.NormalGenerator;


import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Point3d;
import java.awt.*;
import java.util.*;
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
                validationErrors.put(FIFTH_PARAM, StringHelper.toDisplayString("n must be greater than 0"));
                isValid = false;
            }

            if (secondParam > 0) {
                validationErrors.put(SECOND_PARAM, "");
            } else {
                validationErrors.put(SECOND_PARAM, StringHelper.toDisplayString("n must be greater than 0"));
                isValid = false;
            }
        } else {
            validationErrors.put(FIRST_PARAM, StringHelper.toDisplayString("h/d must be greater than b/d"));
            validationErrors.put(SECOND_PARAM, StringHelper.toDisplayString("h/d must be greater than b/d"));
            isValid = false;
        }


        if (thirdParam > 0 && thirdParam < 1) {
            validationErrors.put(THIRD_PARAM, "");
        } else {
            validationErrors.put(THIRD_PARAM, StringHelper.toDisplayString("c/d must be in (0; 1)"));
            isValid = false;
        }


        return isValid;
    }


    double D = 1.0;
    double c = thirdParam;
    double h = firstParam;
    double b = secondParam;

    double P;
    double Q;
    double R;
    double S;

    @Override
    public void initParams() {
        D = 1.0;//7.65;
        c = thirdParam;
        h = firstParam;
        b = secondParam;

//        P = -D^2/4 - b^2*h^2/4/D^2+ b^2*c^4/4/D^2/(h^2 - b^2);
//        Q = (D^4 + 4*D^2*P -b^4)/4/b^2;
//        R = -D^2/16*(D^2+4*P);
//        S = -(c^2 + 2*P)/h^2;

        P = -D * D / 4 - b * b * h * h / 4 / (D * D) + b * b * c * c * c * c / 4 / (D * D) / (h * h - b * b);
        Q = (D * D * D * D + 4 * D * D * P - b * b * b * b) / 4 / (b * b);
        R = -D * D / 16 * (D * D + 4 * P);
        S = -(c * c + 2 * P) / (h * h);
    }

    @Override
    public List<Double> getSurfacePoints() {
        int countOfStep = 301;

        double step = D / countOfStep;
        ArrayList<Double> x = new ArrayList<>();
        ArrayList<Double> y = new ArrayList<>();
        ArrayList<Double> z = new ArrayList<>();


        final double d = D * 0.5;
        double currentZ, r2, temp;
        double currentXSnapshot, currentYSnapshot;
        for (double currentX = -d; currentX < d; currentX += step) {
            for (double currentY = -d; currentY < d; currentY += step) {
                double[] buffer = new double[12];
                currentXSnapshot = currentX;
                currentYSnapshot = currentY;
                for (int inner = 0; inner < 4; inner++) {
                    if (inner == 1) currentX += step;
                    if (inner == 2) currentY += step;
                    if (inner == 3) currentX -= step;
                    r2 = currentX * currentX + currentY * currentY;
                    temp = 2 * S * r2 + Q;
                    if (Math.sqrt(r2) < d) {
                        currentZ = Math.sqrt(0.5 * (Math.sqrt(temp * temp - 4 * (R + P * r2 + r2 * r2)) - 2 * S * r2 - Q));

                        buffer[3 * inner] = currentX;
                        buffer[3 * inner + 1] = currentY;
                        buffer[3 * inner + 2] = currentZ;

                    } else {
                        double alpha = Math.atan(Math.abs(currentY / currentX));
                        buffer[3 * inner] = Math.signum(currentX) * d * Math.cos(alpha);
                        buffer[3 * inner + 1] = Math.signum(currentY) * d * Math.sin(alpha);
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
    public int getBoxY(int boxX, double rectScaleX, double rectScaleY, double rectScaleZ, int jagged) {
        return fitBox_yz(boxX * (rectScaleX / rectScaleY), jagged);
    }

    @Override
    public int getBoxZ(int boxX, double rectScaleX, double rectScaleY, double rectScaleZ, int jagged) {
        return fitBox_yz(h * boxX * (rectScaleX / rectScaleZ), jagged);
    }

    @Override
    protected boolean isPointInsideShapeVolume(double xr, double yr, double zr) {
        double ro2;
        double z2;
        double value;
        ro2 = xr * xr + yr * yr;
        z2 = zr * zr;
        value = ro2 * ro2 + 2 * S * ro2 * z2 + z2 * z2 + P * ro2 + Q * z2 + R;
        return value <= 0;
    }

}