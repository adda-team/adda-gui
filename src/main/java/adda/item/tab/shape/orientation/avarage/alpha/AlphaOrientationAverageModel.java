package adda.item.tab.shape.orientation.avarage.alpha;

import adda.base.annotation.Viewable;
import adda.item.tab.shape.orientation.avarage.gamma.GammaOrientationAverageModel;

public class AlphaOrientationAverageModel extends GammaOrientationAverageModel {


    public static final String JMIN_FIELD_NAME = "jmin";
    public static final String JMAX_FIELD_NAME = "jmax";
    public static final String EPS_FIELD_NAME = "eps";
    public static final String IS_EQUIVALENT_FIELD_NAME = "isEquivalent";
    public static final String IS_PERIODIC_FIELD_NAME = "isPeriodic";
    @Viewable(value = "jmin:", order = 11)
    protected double jmin = 5;

    @Viewable(value = "jmax:", order = 12)
    protected double jmax = 5;

    @Viewable(value = "eps:", order = 13)
    protected double eps = 5;

    @Viewable(value = "equivalent", order = 14)
    protected boolean isEquivalent = false;

    @Viewable(value = "periodic", order = 15)
    protected boolean isPeriodic = false;

    public AlphaOrientationAverageModel() {
        setLabel("alpha");
    }


    public double getJmin() {
        return jmin;
    }

    public void setJmin(double jmin) {
        if (this.jmin != jmin) {
            this.jmin = jmin;
            notifyObservers(JMIN_FIELD_NAME, jmin);
        }
    }


    public double getJmax() {
        return jmax;
    }

    public void setJmax(double jmax) {
        if (this.jmax != jmax) {
            this.jmax = jmax;
            notifyObservers(JMAX_FIELD_NAME, jmax);
        }
    }


    public double getEps() {
        return eps;
    }

    public void setEps(double eps) {
        if (this.eps != eps) {
            this.eps = eps;
            notifyObservers(EPS_FIELD_NAME, eps);
        }
    }




    public boolean isEquivalent() {
        return isEquivalent;
    }

    public void setEquivalent(boolean isEquivalent) {
        if (this.isEquivalent != isEquivalent) {
            this.isEquivalent = isEquivalent;
            notifyObservers(IS_EQUIVALENT_FIELD_NAME, isEquivalent);
        }
    }



    public boolean isPeriodic() {
        return isPeriodic;
    }

    public void setPeriodic(boolean isPeriodic) {
        if (this.isPeriodic != isPeriodic) {
            this.isPeriodic = isPeriodic;
            notifyObservers(IS_PERIODIC_FIELD_NAME, isPeriodic);
        }
    }


//
//    public List<String> getParamsList() {
//        return Arrays.asList(Double.toString(min), Double.toString(max), Double.toString(jmin), Double.toString(jmax), Double.toString(eps));
//    }

}