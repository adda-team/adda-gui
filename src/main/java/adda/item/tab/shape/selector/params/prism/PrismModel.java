package adda.item.tab.shape.selector.params.prism;

import adda.base.annotation.Viewable;
import adda.base.models.ModelBase;
import adda.item.tab.shape.selector.params.ModelShapeParam;
import adda.utils.StringHelper;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.utils.geometry.GeometryInfo;


import org.jogamp.vecmath.AxisAngle4f;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3d;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PrismModel extends ModelShapeParam {

    @Viewable(value = "n")
    protected int firstParam = 6;
    private double prang;
    private double xcenter;
    private double yx_ratio;
    private double zx_ratio;
    private double hdratio;
    private double rc_2;
    private double ri_2;

    public int getFirstParam() {
        return firstParam;
    }

    public void setFirstParam(int firstParam) {
        if (this.firstParam != firstParam) {
            this.firstParam = firstParam;
            notifyObservers(FIRST_PARAM, firstParam);
        }
    }



    @Viewable(value = "h/d")
    protected double secondParam = 2;

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

        if (firstParam > 2) {
            validationErrors.put(FIRST_PARAM, "");
        } else {
            validationErrors.put(FIRST_PARAM,  StringHelper.toDisplayString("n must be greater than 2"));
            isValid = false;
        }

        if (secondParam > 0) {
            validationErrors.put(SECOND_PARAM, "");
        } else {
            validationErrors.put(SECOND_PARAM,  StringHelper.toDisplayString("h/d must be greater than 0"));
            isValid = false;
        }

        return isValid;
    }

    public double getInitialScale() {
        final double scale = 4.5 - secondParam;
        return scale < 0 ? 1.0 : scale;
    }

    protected int getSurfaceCoordinatesArrayType() {
        return GeometryInfo.TRIANGLE_ARRAY;
    }
    double Dx,Dy; // grid sizes along x and y (in units of a - side length)
    double Ri,Rc; // radii of inscribed and circumscribed circles (in units of a)
    double sx;    // distance between center of circles and center of Dx (in units of a)
    double diskratio; // ratio of height to Dx
    int Nsides; // number of sides
    @Override
    public void initParams() {
        Nsides = firstParam;

        diskratio=secondParam;

        prang =Math.PI/Nsides;
        Rc=0.5/Math.sin(prang);
        Ri=Rc*Math.cos(prang);
        // define grid sizes along x and y, shift of the center along x, and symmetries. Here we assume a=1.
        if (Nsides % 2 == 1) { // N=2k+1
            Dx=Rc+Ri;

            sx=(Rc-Ri)/2;
            Dy=2*Rc*Math.sin((Nsides-1)* prang /2);
        }
        else { // N=2k
            Dx=2*Ri;
            sx=0;
            if (Nsides % 4 == 2) { // N=4k+2
                Dy=2*Rc;
            }
            else Dy=Dx; // N=4k
        }

        xcenter=sx/Dx;
        yx_ratio=Dy/Dx;
        zx_ratio=diskratio;
        hdratio=zx_ratio/2;
        rc_2=Rc*Rc/(Dx*Dx);
        ri_2=Ri*Ri/(Dx*Dx);
    }
    public void createSurfaceShape(TransformGroup tg) {
        initParams();
        TransformGroup repairRotation = new TransformGroup();
        Transform3D t3d = new Transform3D();
        repairRotation.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        t3d.setTranslation(new Vector3d(0, 0, 0));
        t3d.setRotation(new AxisAngle4f(0f, 0f, 1f, (float)(Nsides % 2 == 1 ? 0 : prang)));
        t3d.setScale(1.0);
        repairRotation.setTransform(t3d);
        super.createSurfaceShape(repairRotation);
        tg.addChild(repairRotation);

    }

    @Override
    public List<Double> getSurfacePoints() {
        double radius = 0.5 + sx;
        Point3d center = new Point3d(sx, 0, diskratio*0.5);

        List<Point3d> verteces = new ArrayList<>();

        for (int vertexIndex = 0; vertexIndex < Nsides; vertexIndex++) {

            double factor = 1;
            if (Nsides % 2 == 0) {
                factor = 1/Math.cos(prang);
            }
            Point3d vertex = new Point3d((radius*Math.cos(-2*prang*vertexIndex + Math.PI) + sx)*factor, radius*Math.sin(-2*prang*vertexIndex + Math.PI)*factor, diskratio*0.5);
            verteces.add(vertex);
        }

        ArrayList<Double> points = new ArrayList<Double>();
        for (int vertexIndex = 0; vertexIndex < Nsides; vertexIndex++) {
            Point3d current = verteces.get(vertexIndex);
            int index = vertexIndex - 1;
            if (index < 0) index = Nsides - 1;
            Point3d previous = verteces.get(index);

            //// top
            points.add(previous.x);
            points.add(previous.y);
            points.add(previous.z);


            points.add(center.x);
            points.add(center.y);
            points.add(center.z);

            points.add(current.x);
            points.add(current.y);
            points.add(current.z);


            //// bottom
            points.add(current.x);
            points.add(current.y);
            points.add(-current.z);

            points.add(center.x);
            points.add(center.y);
            points.add(-center.z);

            points.add(previous.x);
            points.add(previous.y);
            points.add(-previous.z);

            //// side1
            points.add(previous.x);
            points.add(previous.y);
            points.add(-previous.z);

            points.add(current.x);
            points.add(current.y);
            points.add(current.z);

            points.add(current.x);
            points.add(current.y);
            points.add(-current.z);

            //// side2
            points.add(previous.x);
            points.add(previous.y);
            points.add(previous.z);

            points.add(current.x);
            points.add(current.y);
            points.add(current.z);

            points.add(previous.x);
            points.add(previous.y);
            points.add(-previous.z);

        }


        return points;
    }

    @Override
    protected boolean isPointInsideShapeVolume(double xr, double yr, double zr) {
        double xshift=xr-xcenter;
        double ro2=xshift*xshift+yr*yr;
        if (ro2<=rc_2 && Math.abs(zr)<=hdratio) {
            if (ro2<=ri_2) return true;
                /* this can be optimized considering special cases for small N. For larger N the relevant
                 * fraction of dipoles decrease as N^-2, so this part is less problematic.
                 */
            else {
                double tmp1=Math.cos((Math.abs(Math.atan2(yr,xshift))+prang) % (2*prang)-prang);
                return tmp1 * tmp1 * ro2 <= ri_2;
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