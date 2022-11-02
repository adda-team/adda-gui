package adda.item.tab.shape.selector.params.superellipsoid;

import adda.base.annotation.Viewable;
import adda.base.models.ModelBase;
import adda.item.tab.shape.selector.params.ModelShapeParam;
import adda.utils.StringHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SuperellipsoidModel extends ModelShapeParam {


    @Viewable(value = "b/a")
    protected double firstParam = 2;


    public double getFirstParam() {
        return firstParam;
    }

    public void setFirstParam(double firstParam) {
        if (this.firstParam != firstParam) {
            this.firstParam = firstParam;
            notifyObservers(FIRST_PARAM, firstParam);
        }
    }


    @Viewable(value = "c/a")
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


    @Viewable(value = "e  ")
    protected double thirdParam = 1.8;

    public double getThirdParam() {
        return thirdParam;
    }

    public void setThirdParam(double thirdParam) {
        if (this.thirdParam != thirdParam) {
            this.thirdParam = thirdParam;
            notifyObservers(THIRD_PARAM, thirdParam);
        }
    }


    @Viewable(value = "n  ")
    protected double fourthParam = 1.8;

    public double getFourthParam() {
        return fourthParam;
    }

    public void setFourthParam(double fourthParam) {
        if (this.fourthParam != fourthParam) {
            this.fourthParam = fourthParam;
            notifyObservers(FOURTH_PARAM, fourthParam);
        }
    }


    public List<String> getParamsList() {
        return Arrays.asList(StringHelper.toDisplayString(firstParam), StringHelper.toDisplayString(secondParam), StringHelper.toDisplayString(thirdParam), StringHelper.toDisplayString(fourthParam));
    }

    @Override
    public boolean validate() {
        boolean isValid = true;

        if (firstParam > 0) {
            validationErrors.put(FIRST_PARAM, "");
        } else {
            validationErrors.put(FIRST_PARAM, StringHelper.toDisplayString("b/a must be greater than 0"));
            isValid = false;
        }

        if (secondParam > 0) {
            validationErrors.put(SECOND_PARAM, "");
        } else {
            validationErrors.put(SECOND_PARAM, StringHelper.toDisplayString("c/a must be greater than 0"));
            isValid = false;
        }

        if (thirdParam > 0) {
            validationErrors.put(THIRD_PARAM, "");
        } else {
            validationErrors.put(THIRD_PARAM, StringHelper.toDisplayString("e must be non negative"));
            isValid = false;
        }

        if (fourthParam > 0) {
            validationErrors.put(FOURTH_PARAM, "");
        } else {
            validationErrors.put(FOURTH_PARAM, StringHelper.toDisplayString("n must be non negative"));
            isValid = false;
        }

        return isValid;
    }


    private double aspectY;
    private double aspectZ;
    private double seE;
    private double seN;
    private double yx_ratio;
    private double zx_ratio;
    private double seInvR;
    private double seR;
    private double seT;
    private double seToverR;
    private double seInvT;

    public double getInitialScale() {
        initParams();
        final double scale = 4.5 - Math.max(yx_ratio, zx_ratio);
        return scale < 0 ? 1.0 : scale;
    }

    @Override
    public void initParams() {
        aspectY = firstParam;

        aspectZ = secondParam;

        seE = thirdParam;

        seN = fourthParam;


        yx_ratio = aspectY;
        zx_ratio = aspectZ;

        // set additional parameters when possible
        seInvR = seE / 2;
        if (seE != 0) seR = 2 / seE;
        if (seN != 0) {
            seT = 2 / seN;
            seInvT = seN / 2;
            seToverR = seE / seN;
        }
    }

    @Override
    public List<Double> getSurfacePoints() {
        int countOfStep = 200;

        if (seE > 1 || seN > 1) {
            countOfStep = 201;
        }

        double step = 1.0 / countOfStep;

        double fix = 0;
        if (seE > 1 || seN > 1) {
            fix = step;
        }

        ArrayList<Double> x = new ArrayList<>();
        ArrayList<Double> y = new ArrayList<>();
        ArrayList<Double> z = new ArrayList<>();


        final double d = 0.5;
        double currentZ, r2;
        double currentXSnapshot, currentYSnapshot;
        for (double currentX = -d; currentX < d - fix; currentX += step) {
            for (double currentY = -d * yx_ratio; currentY < d * yx_ratio; currentY += step * yx_ratio) {
                double[] buffer = new double[12];
                currentXSnapshot = currentX;
                currentYSnapshot = currentY;

                for (int inner = 0; inner < 4; inner++) {
                    if (inner == 1) currentX += step;
                    if (inner == 2) currentY += step * yx_ratio;
                    if (inner == 3) currentX -= step;

                    double xn = Math.abs(2 * currentX);
                    double yn = Math.abs(2 * currentY / yx_ratio);
                    double zn;//=Math.abs(2*zr/zx_ratio);
                    double tmp1;//=Math.abs(2*zr/zx_ratio);


                    /* First we consider zero e and n, then use two separate ways to separate the powers in
                     * (xn^r + yn^r)^(t/r): either, (...)^(t/r) or (...)^t. This ensures that we do not have very small
                     * value (susceptible to underflow) taken to a large power or vice versa. Moreover, the description
                     * is continuous with decreasing n and/or e. Although the cases of n=0 or e=0 still need a separate
                     * if clause, they do appear as natural limiting cases.
                     */
                    if (seE > Math.min(seN, 1)) { // implies that e!=0
                        /* n=0 => xy-sections are superellipses independent of z, while xz- and yz-sections are
                         * rectangles
                         */
                        tmp1 = Math.pow(xn, seR) + Math.pow(yn, seR);
                        zn = Math.pow((1 - Math.pow(tmp1, seToverR)), seInvT);
                    } else { // here n!=0
                        /* e=0 => xz- and yz-sections are superellipses independent of y and x, respectively, while
                         * xy-sections are rectangles
                         */
                        if (seE == 0) tmp1 = Math.max(xn, yn);
                            // in the following we ensure that the argument taken to (potentially large) power r is <=1
                        else if (xn < yn) tmp1 = yn * Math.pow(1 + Math.pow(xn / yn, seR), seInvR);
                        else if (xn > yn) tmp1 = xn * Math.pow(1 + Math.pow(yn / xn, seR), seInvR);
                        else tmp1 = yn * Math.pow(2, seInvR);
                        zn = Math.pow((1 - Math.pow(tmp1, seT)), seInvT);
                    }

                    if (zn <= 1 && zn > 0) {
                        currentZ = zx_ratio * zn * 0.5;

                        buffer[3 * inner] = currentX;
                        buffer[3 * inner + 1] = currentY;
                        buffer[3 * inner + 2] = currentZ;
                    } else {
                        yn = Math.pow((1 - Math.pow(xn, seR)), seInvR);
                        buffer[3 * inner] = currentX;
                        if (Double.isNaN(yn)) {
                            buffer[3 * inner + 1] = Math.signum(currentY)*Math.abs(currentX)/yx_ratio;//Math.abs(currentY*yx_ratio) > Math.abs(currentX) ?  : currentY;
                        } else {
                            buffer[3 * inner + 1] = Math.signum(currentY) * yn * yx_ratio * 0.5;
                        }
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
        double xn = Math.abs(2 * xr);
        double yn = Math.abs(2 * yr / yx_ratio);
        double zn = Math.abs(2 * zr / zx_ratio);
        // separate check for bounding box to avoid overflows in power functions
        if (yn <= 1 && zn <= 1) {
            /* First we consider zero e and n, then use two separate ways to separate the powers in
             * (xn^r + yn^r)^(t/r): either, (...)^(t/r) or (...)^t. This ensures that we do not have very small
             * value (susceptible to underflow) taken to a large power or vice versa. Moreover, the description
             * is continuous with decreasing n and/or e. Although the cases of n=0 or e=0 still need a separate
             * if clause, they do appear as natural limiting cases.
             */
            if (seN == 0 && seE == 0) return true; // a box
            else if (seE > Math.min(seN, 1)) { // implies that e!=0
                /* n=0 => xy-sections are superellipses independent of z, while xz- and yz-sections are
                 * rectangles
                 */
                double tmp1 = Math.pow(xn, seR) + Math.pow(yn, seR);
                if (tmp1 <= 1 && (seN == 0 || Math.pow(tmp1, seToverR) + Math.pow(zn, seT) <= 1)) return true;
            } else { // here n!=0
                /* e=0 => xz- and yz-sections are superellipses independent of y and x, respectively, while
                 * xy-sections are rectangles
                 */
                double tmp1;
                if (seE == 0) tmp1 = Math.max(xn, yn);
                    // in the following we ensure that the argument taken to (potentially large) power r is <=1
                else if (xn < yn) tmp1 = yn * Math.pow(1 + Math.pow(xn / yn, seR), seInvR);
                else if (xn > yn) tmp1 = xn * Math.pow(1 + Math.pow(yn / xn, seR), seInvR);
                else tmp1 = yn * Math.pow(2, seInvR);
                if (Math.pow(tmp1, seT) + Math.pow(zn, seT) <= 1) return true;
            }
        }
        return false;
    }


    @Override
    public int getBoxY(int boxX, double rectScaleX, double rectScaleY, double rectScaleZ, int jagged) {
        return fitBox_yz(yx_ratio * boxX * (rectScaleX / rectScaleY), jagged);
    }

    @Override
    public int getBoxZ(int boxX, double rectScaleX, double rectScaleY, double rectScaleZ, int jagged) {
        return fitBox_yz(zx_ratio * boxX * (rectScaleX / rectScaleZ), jagged);
    }

}