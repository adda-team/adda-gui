package adda.item.tab.shape.selector.params.plate;

import adda.base.annotation.Viewable;
import adda.base.models.ModelBase;
import adda.item.tab.shape.selector.params.ModelShapeParam;
import adda.item.tab.shape.selector.params.capsule.CapsuleModel;
import adda.utils.StringHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlateModel extends ModelShapeParam {


    @Viewable(value = "h/d")
    protected double firstParam = 0.25;

    public double getFirstParam() {
        return firstParam;
    }

    public void setFirstParam(double firstParam) {
        if (this.firstParam != firstParam) {
            this.firstParam = firstParam;
            notifyObservers(FIRST_PARAM, firstParam);
        }
    }

    public double getInitialScale() {
        final double scale = 5 - firstParam*0.6;
        return scale < 0 ? 1.0 : scale;
    }

    public List<String> getParamsList() {
        return Arrays.asList(StringHelper.toDisplayString(firstParam));
    }

    @Override
    public boolean validate() {
        boolean isValid = true;

        if (firstParam > 0) {
            validationErrors.put(FIRST_PARAM, "");
        } else {
            validationErrors.put(FIRST_PARAM,  StringHelper.toDisplayString("h/d must be greater than 0"));
            isValid = false;
        }

        return isValid;
    }


    double zx_ratio;
    double hdratio;
    double cutoff;
    double ri_2;

    @Override
    public void initParams() {
        zx_ratio=firstParam;

        hdratio=zx_ratio/2;
        cutoff = 0.5-hdratio;
        ri_2=cutoff*cutoff;
    }

    @Override
    public List<Double> getSurfacePoints() {
        int countOfStep = 360;

        double step = 1.0 / countOfStep;
        double alphaStep = 2*Math.PI / countOfStep;

        ArrayList<Double> x = new ArrayList<>();
        ArrayList<Double> y = new ArrayList<>();
        ArrayList<Double> z = new ArrayList<>();



        final double d = 0.5;
        //final double cutoff = 0.5 - hdratio;
        double currentZ, r2;
        double currentXSnapshot, currentYSnapshot;
        for (double currentR = 0; currentR <= d; currentR += step) {
            for (double alpha = 0; alpha < 2*Math.PI; alpha += alphaStep) {
                double[] buffer = new double[12];
                currentXSnapshot = currentR;
                currentYSnapshot = alpha;
                for (int inner = 0; inner < 4; inner++) {
                    if (inner == 1) currentR += step;
                    if (inner == 2) alpha += alphaStep;
                    if (inner == 3) currentR -= step;


                    buffer[3 * inner] = currentR*Math.cos(alpha);
                    buffer[3 * inner + 1] = currentR*Math.sin(alpha);

                    if (currentR <= cutoff) {
                        buffer[3 * inner + 2] = hdratio;
                    } else {
                        double delta = currentR - cutoff;
                        double beta = Math.acos(delta/hdratio);
                        buffer[3 * inner + 2] = hdratio*Math.sin(beta);
                    }
                }
                for (int index = 0; index < 4; index++) {
                    x.add(buffer[3 * index]);
                    y.add(buffer[3 * index + 1]);
                    z.add(buffer[3 * index + 2]);
                }

                currentR = currentXSnapshot;
                alpha = currentYSnapshot;
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
//        double ro2=xr*xr+yr*yr;
//        if (ro2<=0.25 && Math.abs(zr)<=hdratio) {
//            if (ro2<=ri_2) return true;
//            else {
//                double tmp1=Math.sqrt(ro2)-0.5+hdratio; // ro-ri
//                return tmp1 * tmp1 + zr * zr <= hdratio * hdratio;
//            }
//        }
//
//
        double currentR = Math.sqrt(xr*xr+yr*yr);
        if (currentR <= cutoff) {
            return Math.abs(zr) <= hdratio;
        } else {
            double delta = currentR - cutoff;
            double beta = Math.acos(delta/hdratio);
            return Math.abs(zr) <= hdratio*Math.sin(beta);
        }
        //return false;
    }

    @Override
    public int getBoxY(int boxX, double rectScaleX, double rectScaleY, double rectScaleZ, int jagged) {
        return fitBox_yz(boxX * (rectScaleX / rectScaleY), jagged);
    }

    @Override
    public int getBoxZ(int boxX, double rectScaleX, double rectScaleY, double rectScaleZ, int jagged) {
        return fitBox_yz(firstParam * boxX * (rectScaleX / rectScaleZ), jagged);
    }

}