package adda.item.tab.shape.selector.params.egg;

import adda.base.annotation.Viewable;
import adda.base.models.ModelBase;
import adda.item.tab.shape.selector.params.ModelShapeParam;
import adda.utils.StringHelper;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.vecmath.AxisAngle4f;
import org.jogamp.vecmath.Vector3d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EggModel extends ModelShapeParam {


    @Viewable(value = "\u03B5")
    protected double firstParam = 0.6;
    private double egeps;
    private double egnu;
    private double ad2;
    private double ad;
    private double zcenter;
    private double zx_ratio;

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
    protected double secondParam = 0.3;

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

    public double getInitialScale() {
        initParams();
        final double scale = 4.5 - (zx_ratio*0.6);
        return scale < 1 ? 1.0 : scale;
    }

    public void createSurfaceShape(TransformGroup tg) {
        initParams();
        TransformGroup repairRotation = new TransformGroup();
        Transform3D t3d = new Transform3D();
        repairRotation.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        t3d.setTranslation(new Vector3d(0, 0, zcenter));
        t3d.setScale(1.0);
        repairRotation.setTransform(t3d);
        super.createSurfaceShape(repairRotation);
        tg.addChild(repairRotation);

    }

    @Override
    public List<Double> getSurfacePoints() {
        int countOfStep = 180;

        //double step = 1.0 / countOfStep;
        double step = 2*Math.PI / countOfStep;

        ArrayList<Double> x = new ArrayList<>();
        ArrayList<Double> y = new ArrayList<>();
        ArrayList<Double> z = new ArrayList<>();



        //final double d = 0.5;
        //final double cutoff = 0.5 - hdratio;
        double currentZ, r2;
        double thetaSnapshot, alphaSnapshot;
        for (double theta = 0; theta < Math.PI; theta += step) {
            for (double alpha = 0; alpha < 2*Math.PI; alpha += step) {
                double[] buffer = new double[12];
                thetaSnapshot = theta;
                alphaSnapshot = alpha;
                for (int inner = 0; inner < 4; inner++) {
                    if (inner == 1) theta += step;
                    if (inner == 2) alpha += step;
                    if (inner == 3) theta -= step;

                    double cosTheta = Math.cos(theta);
                    double sinTheta = Math.sin(theta);
                    double cos2Theta = cosTheta*cosTheta;
                    //(a/r)^2=1+nu*cos(theta)-(1-eps)cos^2(theta)

                    final double tmp = 1 + egnu * cosTheta - (1 - egeps) * cos2Theta;

                    if (tmp < 0.001) {
                        buffer[3 * inner] = Math.cos(alpha)*sinTheta;
                        buffer[3 * inner + 1] = Math.sin(alpha)*sinTheta;
                        buffer[3 * inner + 2] = -zx_ratio*0.5;
                    } else {
                        double r = ad/Math.sqrt(tmp);
                        buffer[3 * inner] = r *Math.cos(alpha)*sinTheta;
                        buffer[3 * inner + 1] = r *sinTheta*Math.sin(alpha);
                        buffer[3 * inner + 2] = r * cosTheta;
                    }


                }
                for (int index = 0; index < 4; index++) {
                    x.add(buffer[3 * index]);
                    y.add(buffer[3 * index + 1]);
                    z.add(buffer[3 * index + 2]);
                }

                theta = thetaSnapshot;
                alpha = alphaSnapshot;
            }

        }
        ArrayList<Double> points = new ArrayList<Double>();
        for (int i = 0; i < x.size(); i++) {
            points.add(x.get(i));
            points.add(y.get(i));
            points.add(z.get(i));
        }

//        for (int i = x.size() - 1; i >= 0; i--) {
//            points.add(x.get(i));
//            points.add(y.get(i));
//            points.add(-z.get(i));
//        }

        return points;
    }

    @Override
    public void initParams() {
        double ct,ct2; // cos(theta0) and its square

        egeps=firstParam;

        egnu=secondParam;

        // egg shape is symmetric about z-axis (xz and yz planes, but generally NOT xy plane)

        /* cos(theta0): ct=-nu/[eps+sqrt(eps^2-nu^2)]; this expression for root of the quadratic equation is used
         * for numerical stability (i.e. when nu=0); at this theta0 the diameter (maximum width perpendicular to
         * z-axis) d=Dx is located
         */
        ct=-egnu/(egeps+Math.sqrt(egeps*egeps-egnu*egnu));
        ct2=ct*ct;
        // Determine ad=(a/d) and its square
        ad2=(1+egnu*ct-(1-egeps)*ct2)/(4*(1-ct2));
        ad=Math.sqrt(ad2);
        double tmp1=1/Math.sqrt(egeps+egnu);
        double tmp2=1/Math.sqrt(egeps-egnu);
        double tmp3=2*(1-egeps);
        /* Center of the computational box (z coordinate): z0=(a/d)*[1/sqrt(eps+nu)+1/sqrt(eps-nu)]/2; but more
         * numerically stable expression is used (for nu->0). Although it may overflow faster for nu->eps,
         * volume_ratio (below) will overflow even faster. It is used to shift coordinates from the computational
         * reference frame (centered at z0) to the natural one
         */
        zcenter=ad*egnu*(tmp1*tmp1*tmp2*tmp2)/(tmp1+tmp2);
        // (V/d^3)=(4*pi/3)*(a/d)^3*{[2(1-eps)-nu]/sqrt(eps+nu)+[2(1-eps)+nu]/sqrt(eps-nu)}/[nu^2+4(1-eps)]
        //volume_ratio=FOUR_PI_OVER_THREE*ad2*ad*((tmp3-egnu)*tmp1+(tmp3+egnu)*tmp2)/(egnu*egnu+2*tmp3);


        zx_ratio=ad*(tmp1+tmp2); // (a/d)*[1/sqrt(eps+nu)+1/sqrt(eps-nu)]
    }

    @Override
    public int getBoxY(int boxX, double rectScaleX, double rectScaleY, double rectScaleZ, int jagged) {
        return fitBox_yz(boxX * (rectScaleX / rectScaleY), jagged);
    }

    @Override
    public int getBoxZ(int boxX, double rectScaleX, double rectScaleY, double rectScaleZ, int jagged) {
        return fitBox_yz(zx_ratio * boxX * (rectScaleX / rectScaleZ), jagged);
    }

    @Override
    protected boolean isPointInsideShapeVolume(double xr, double yr, double zr) {
        double ro2=xr*xr+yr*yr;
        double zshift=zr-zcenter;
        double z2=zshift*zshift;
        return ro2+egeps*z2+egnu*zshift*Math.sqrt(ro2+z2)<=ad2;
    }
}