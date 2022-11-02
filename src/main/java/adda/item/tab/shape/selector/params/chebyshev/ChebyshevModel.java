package adda.item.tab.shape.selector.params.chebyshev;

import adda.base.annotation.Viewable;
import adda.base.models.ModelBase;
import adda.item.tab.shape.selector.params.ModelShapeParam;
import adda.utils.StringHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;


public class ChebyshevModel extends ModelShapeParam {


    @Viewable(value = "\u03B5")
    protected double firstParam = 0.5;
    private double chebeps;
    private int chebn;
    private int yx_ratio;
    private double zx_ratio;
    private double zcenter;
    private double r0_2;
    private double rc_2;
    private double ri_2;
    private double dz;
    private double dx;


    public double getFirstParam() {
        return firstParam;
    }

    public void setFirstParam(double firstParam) {
        if (this.firstParam != firstParam) {
            this.firstParam = firstParam;
            notifyObservers(FIRST_PARAM, firstParam);
        }
    }



    @Viewable(value = "n")
    protected int secondParam = 5;

    public int getSecondParam() {
        return secondParam;
    }

    public void setSecondParam(int secondParam) {
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

        if (firstParam <= 1 && firstParam >= -1) {
            validationErrors.put(FIRST_PARAM, "");
        } else {
            validationErrors.put(FIRST_PARAM,  StringHelper.toDisplayString("\u03B5 must be in [-1; 1]"));
            isValid = false;
        }

        if (secondParam > 0) {
            validationErrors.put(SECOND_PARAM, "");
        } else {
            validationErrors.put(SECOND_PARAM,  StringHelper.toDisplayString("n must be greater than 0"));
            isValid = false;
        }

        return isValid;
    }

    double Dx,Dz; // grid sizes along x and z (in units of scale parameter r0)
    double sz;    // coordinate of center of Dz relative to sphere origin (in units of r0)
    double ae;    // |eps|
    double volume_ratio;    // |eps|

//    @Override
//    public double getInitialScale() {
//        return 2;
//    }

    @Override
    public void initParams() {


        chebeps =firstParam;

        chebn =secondParam;

        ChebyshevParams();

        yx_ratio =1;
        zx_ratio =Dz/Dx;

        zcenter =-sz/Dx;
        r0_2 =1/(Dx*Dx);
        ae=Math.abs(chebeps);
        rc_2 =(1+ae)*(1+ae)* r0_2;
        ri_2 =(1-ae)*(1-ae)* r0_2;

    }

    public List<Double> getSurfacePoints() {
        int countOfStep = 180;

        //double step = 1.0 / countOfStep;
        double step = 2*Math.PI / countOfStep;

        ArrayList<Double> x = new ArrayList<>();
        ArrayList<Double> y = new ArrayList<>();
        ArrayList<Double> z = new ArrayList<>();

        double r0 = 1.0/Dx;


        //final double d = 0.5;
        //final double cutoff = 0.5 - hdratio;
        double currentZ, r2;
        double thetaSnapshot, alphaSnapshot;
        for (double theta = 0; theta <= Math.PI; theta += step) {
            for (double alpha = 0; alpha <= 2*Math.PI; alpha += step) {
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

                    double r = r0*(1 + eps*Math.cos(n*theta));
                    buffer[3 * inner] = r *Math.cos(alpha)*sinTheta;
                    buffer[3 * inner + 1] = r *sinTheta*Math.sin(alpha);
                    buffer[3 * inner + 2] = r * cosTheta;

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
    protected boolean isPointInsideShapeVolume(double xr, double yr, double zr) {
        double ro2=xr*xr+yr*yr;
        double zshift=zr- zcenter;
        double r2=ro2+zshift*zshift;
        if (r2<= ri_2) return true;
        else if (r2<= rc_2) {
            /* This can be optimized using Chebyshev polynomials, but would probably be efficient only for
             * relatively small n.
             */
            double tmp1=1+ chebeps *Math.cos(chebn *Math.atan2(Math.sqrt(ro2), zshift));
            return r2 <= r0_2 * tmp1 * tmp1;
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

    private static final int MAX_NEWTON_ITERATIONS = 20;
// The following corresponds to sqrt(DBL_EPSILON)/10. It should lead to DBL_EPSILON precision in the function value
    private static final double  NEWTON_EPS = 1e-9;
    enum newt_func { // type of function to calculate in Newton's maximization
        NF_VALUE, // f(x)
        NF_DRATIO // f'(x)/f''(x)
    };


    double eps;
    int n;

    private void ChebyshevParams() {
        double n2, tmp1, tmp2, zmax, zmin, xmax;
        eps = chebeps;
        n = chebn;
        n2 = n * n;

        // In the following, Zmax is called a few times, but never with the same arguments
        zmax = Zmax(eps);
        if (n % 2 == 1) { // n=2k+1
            zmin = -Zmax(-eps);
            xmax = Xmax();
        } else { // n=2k
            zmin = -zmax; // symmetric case
            if (n % 4 == 2) xmax = Zmax(-eps); // n=4k+2
            else xmax = zmax;
        }
        Dz = zmax - zmin;
        sz = (zmax + zmin) / 2;
        Dx = 2 * xmax;
        /* determine volume fraction; the formula is self-derived but agrees with A. Mugnai and W.J. Wiscombe, �Scattering
         * of radiation by moderately nonspherical particles,� J. Atmos. Sci. 37, 1291-1307 (1980).
         */
        tmp1 = eps * eps / 4;
        tmp2 = 1 + 6 * tmp1 * (4 * n2 - 2) / (4 * n2 - 1);
        if (!(n % 2 == 1)) tmp2 -= eps * (3 * (1 + tmp1) / (n2 - 1) + tmp1 / (9 * n2 - 1)); // n=2k
        volume_ratio = 4 * Math.PI / 3 * tmp2 / ((dx) * (dx) * (dx));
    }

    double Xmax()
// finds maximum of function [1+e*sin(n*x)]cos(x); in principle should work for any n, but is called only for odd n
    {
        double res,e,s,arg;
        e=Math.abs(eps); // result does not depend on sign of eps anyway

        if (n==1) {
            s=2*e/(1+Math.sqrt(1+8*e*e)); // solution of quadratic equation on sin(x) from f'(x)=0
            res=(1+e*s)*Math.sqrt(1-s*s);
        } // a few other values of n allow for a closed-form solution, but they are cumbersome
        else {
            arg=Math.PI/(2*n);
            res=NewtonMaximum(arg-Math.tan(arg)*(1+e)/(1+e*(1+n*n)),0,arg,this::Xfunc);
        }
        return res;
    }

    double Zmax(double e)
// finds maximum of function [1+e*cos(n*x)]cos(x)
    {
        double res,n2,tmp,arg,x01,x02,x0;

        n2=n*n;
        tmp=e*(n2+1);
        if (tmp>=-1) res=1+e;
            /* two special cases for small n are based on direct solution of f'(x)=0; the formulae are self-derived but agree
             * (for n=2) with A. Mugnai and W.J. Wiscombe, �Scattering of radiation by moderately nonspherical particles,�
             * J. Atmos. Sci. 37, 1291-1307 (1980).
             */
        else if (n==1) res=-1/(4*e);
        else if (n==2) res=2*(1-e)*Math.sqrt((e-1)/(6*e))/3;
        else { // a few other values of n allow for a closed-form solution, but they are cumbersome
            arg=Math.PI/n;
            x01=arg - Math.tan(arg)*(1-e)/(1-tmp);
            x02=Math.sqrt(6*(1+tmp)/(1+e*(1+6*n2+n2*n2)));
            // this is not the most efficient empirics, but should work fine
            x0=Zfunc(x01,newt_func.NF_VALUE)>Zfunc(x02,newt_func.NF_VALUE) ? x01 : x02;
            res=NewtonMaximum(x0,0,arg,this::Zfunc);
        }
        return res;
    }

    double NewtonMaximum(double x0,double x1,double x2 , BiFunction<Double, newt_func, Double> func)
    /* finds a maximum (minimum) of a function f(x) using the Newton's method; x0 - should be a good guess, and [x1,x2] is
     * a bounding interval (preliminary analysis is usually required for this); func - is a reference to function, which
     * implements both f(x) and f'(x)/f''(x) (depending on the second argument)
     */
    {
        int i;
        double x,dx;

        x=x0;
        for (i=0;i<MAX_NEWTON_ITERATIONS;i++) {
            dx=func.apply(x,newt_func.NF_DRATIO);
            x-=dx;
            if (x<x1 || x>x2) {
                error = "Newton's method fell out of bounds";
                return 0;
            }
            if (Math.abs(dx)<=NEWTON_EPS*x) return func.apply(x,newt_func.NF_VALUE);
        }
        error = "Newton's method failed to converge in "+i+" iterations";
        return 0;
    }


    double Zfunc(double x, newt_func mode)
// computes either f(x) or f'(x)/f''(x), where f(x)=[1+e*cos(nx)]cos(x)
    {
        double e,xn,s,c,sn,cn;

        e=-Math.abs(eps); // assumes that e is negative; solution is trivial otherwise
        xn=n*x;
        switch (mode) {
            case NF_VALUE:
                c=Math.cos(x);
                cn=Math.cos(xn);
                return (1+e*cn)*c;
            case NF_DRATIO:
                s=Math.sin(x);
                c=Math.cos(x);
                sn=Math.sin(xn);
                cn=Math.cos(xn);
                return (-e*n*sn*c-(1+e*cn)*s)/(2*e*n*sn*s-(1+e*(1+n*n)*cn)*c);
        }
    return 0;
    }

//======================================================================================================================

    double Xfunc(double x, newt_func mode)
// computes either f(x) or f'(x)/f''(x), where f(x)=[1+e*sin(nx)]cos(x)
    {
        double e,xn,s,c,sn,cn;

        e=Math.abs(eps); // assumes that e is positive (maximum value is invariant to sign change)
        xn=n*x;
        switch (mode) {
            case NF_VALUE:
                c=Math.cos(x);
                sn=Math.sin(xn);
                return (1+e*sn)*c;
            case NF_DRATIO:
                s=Math.sin(x);
                c=Math.cos(x);
                sn=Math.sin(xn);
                cn=Math.cos(xn);
                return (e*n*cn*c-(1+e*sn)*s)/(-2*e*n*cn*s-(1+e*(1+n*n)*sn)*c);
        }
        return 0;
    }
}