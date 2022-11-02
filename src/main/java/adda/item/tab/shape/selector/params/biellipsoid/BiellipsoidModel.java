package adda.item.tab.shape.selector.params.biellipsoid;

import adda.base.annotation.Viewable;
import adda.item.tab.shape.selector.params.ModelShapeParamTwoDomains;
import adda.utils.StringHelper;
import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.geometry.GeometryInfo;
import org.jogamp.java3d.utils.geometry.NormalGenerator;
import org.jogamp.java3d.utils.geometry.Sphere;


import org.jogamp.vecmath.AxisAngle4f;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Vector3d;
import java.awt.*;
import java.util.*;
import java.util.List;

public class BiellipsoidModel extends ModelShapeParamTwoDomains {

    @Viewable(value = "<html>y<sub>1</sub>/x<sub>1</sub></html>")
    protected double firstParam = 0.5;


    private double aspectY;
    private double aspectZ;
    private double aspectXs;
    private double aspectY2;
    private double aspectZ2;
    private double invsqY;
    private double invsqZ;
    private double invsqY2;
    private double invsqZ2;
    private double invmaxX;
    private double ell_x1;
    private double ell_x2;
    private double ell_rsq1;
    private double ell_rsq2;
    private double aspectY2sc;
    private double aspectZ2sc;
    private double zcenter1;
    private double zcenter2;
    private double boundZ;
    private double volume_ratio;
    private double yx_ratio;
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


    @Viewable(value = "<html>z<sub>1</sub>/x<sub>1</sub></html>")
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
    protected double fourthParam = 2;

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
    protected double fifthParam = 0.5;

    public double getFifthParam() {
        return fifthParam;
    }

    public void setFifthParam(double fifthParam) {
        if (this.fifthParam != fifthParam) {
            this.fifthParam = fifthParam;
            notifyObservers(FIFTH_PARAM, fifthParam);
        }
    }

    public double getInitialScale() {
        initParams();
        final double scale = 4.5 - Math.max(yx_ratio, zx_ratio);
        return scale < 0 ? 1.0 : scale;
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

    @Override
    protected boolean isOpacityForFirst() {
        return false;
    }


    @Override
    public void initParams() {

        aspectY = firstParam;
        aspectZ = secondParam;
        aspectXs = thirdParam;
        aspectY2 = fourthParam;
        aspectZ2 = fifthParam;

        invsqY = 1 / (aspectY * aspectY);
        invsqZ = 1 / (aspectZ * aspectZ);
        invsqY2 = 1 / (aspectY2 * aspectY2);
        invsqZ2 = 1 / (aspectZ2 * aspectZ2);
        // determine scale to be used for variables below; and "radii" of ellipsoids
        invmaxX = 1 / Math.max(aspectXs, 1);
        ell_x1 = 0.5 * invmaxX;
        ell_x2 = ell_x1 * aspectXs;
        ell_rsq1 = ell_x1 * ell_x1;
        ell_rsq2 = ell_x2 * ell_x2;
        // rescale aspect ratios for second ellipsoid
        aspectY2sc = aspectY2 * aspectXs;
        aspectZ2sc = aspectZ2 * aspectXs;
        // set z positions of centers and intersection
        zcenter1 = -aspectZ2sc * invmaxX / 2;
        zcenter2 = aspectZ * invmaxX / 2;
        boundZ = zcenter1 + zcenter2;
        // set box and volume ratios
        volume_ratio = Math.PI / 6 * (aspectY * aspectZ + aspectY2sc * aspectZ2sc * aspectXs) * invmaxX * invmaxX * invmaxX;
        yx_ratio = Math.max(aspectY, aspectY2sc) * invmaxX;
        zx_ratio = (aspectZ + aspectZ2sc) * invmaxX;
    }


    public void createSurfaceShape(TransformGroup tg) {
        initParams();

        TransformGroup tgEllipsoid1 = new TransformGroup();
        Transform3D t3d = new Transform3D();
        tgEllipsoid1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        t3d.setTranslation(new Vector3d(0, 0, zcenter1));
        t3d.setRotation(new AxisAngle4f(0f, 0f, 0f, 0f));
        t3d.setScale(1.0);
        tgEllipsoid1.setTransform(t3d);
        genarateEllipsoid(tgEllipsoid1, 2*ell_x1, 2*aspectY*ell_x1, invsqY, invsqZ, ell_rsq1, getVoxelColor());
        tg.addChild(tgEllipsoid1);

        TransformGroup tgEllipsoid2 = new TransformGroup();
        t3d = new Transform3D();
        tgEllipsoid2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        t3d.setTranslation(new Vector3d(0, 0, zcenter2));
        t3d.setRotation(new AxisAngle4f(0f, 0f, 0f, 0f));
        t3d.setScale(1.0);
        tgEllipsoid2.setTransform(t3d);
        genarateEllipsoid(tgEllipsoid2, 2*ell_x2, 2*ell_x2*aspectY2, invsqY2, invsqZ2, ell_rsq2, getVoxelColorSecond());
        tg.addChild(tgEllipsoid2);

    }

    private void genarateEllipsoid(TransformGroup tg, double sizeX, double sizeY, double invsqY, double invsqZ, double ell_r2, Color3f color) {

        List<Double> points = getEllipsoidPoints(sizeX, sizeY, invsqY, invsqZ, ell_r2);

        if (points.size() < 1) {
            return;
        }

        GeometryInfo gi = new GeometryInfo(GeometryInfo.QUAD_ARRAY);
        gi.setCoordinates(points.stream().mapToDouble(i -> i).toArray());

        NormalGenerator normalGenerator = new NormalGenerator();
        normalGenerator.generateNormals(gi);

        Appearance ap = new Appearance();

        PolygonAttributes polyAttrbutes = new PolygonAttributes();
        polyAttrbutes.setPolygonMode(PolygonAttributes.POLYGON_FILL);
        polyAttrbutes.setCullFace(PolygonAttributes.CULL_NONE);
        ap.setPolygonAttributes(polyAttrbutes);

        Material material = new Material();
        material.setShininess(0.5f);
        material.setDiffuseColor(color);
        material.setAmbientColor(color);

        ap.setMaterial(material);

        Shape3D shape1 = new Shape3D(gi.getGeometryArray(), ap);
        tg.addChild(shape1);

    }


    public List<Double> getEllipsoidPoints(double sizeX, double sizeY, double invsqY, double invsqZ, double ell_r2) {
        int countOfStep = 101;

        double step = sizeX / countOfStep;

        ArrayList<Double> x = new ArrayList<>();
        ArrayList<Double> y = new ArrayList<>();
        ArrayList<Double> z = new ArrayList<>();


        final double dx = 0.5 * sizeX;
        final double dy = 0.5 * sizeY;
        double currentZ, r2;
        double currentXSnapshot, currentYSnapshot;
        for (double currentX = -dx; currentX < dx; currentX += step) {
            for (double currentY = -dy; currentY < dy; currentY += step) {
                double[] buffer = new double[12];
                currentXSnapshot = currentX;
                currentYSnapshot = currentY;
                for (int inner = 0; inner < 4; inner++) {
                    if (inner == 1) currentX += step;
                    if (inner == 2) currentY += step;
                    if (inner == 3) currentX -= step;
                    r2 = currentX * currentX + currentY * currentY * invsqY;

                    if (Math.sqrt(r2) < dx) {
                        currentZ = Math.sqrt((ell_r2 - r2) / invsqZ);

                        buffer[3 * inner] = currentX;
                        buffer[3 * inner + 1] = currentY;
                        buffer[3 * inner + 2] = currentZ;

                    } else {
//                        double alpha = Math.atan(Math.abs(currentY / currentX));
//                        double r = Math.sqrt(r2);
                        buffer[3 * inner] = currentX;
                        buffer[3 * inner + 1] = currentX * currentX > ell_r2 ? 0 : Math.signum(currentY) * Math.sqrt((ell_r2 - currentX * currentX) / invsqY);
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
        return fitBox_yz(yx_ratio * boxX * (rectScaleX / rectScaleY), jagged);
    }

    @Override
    public int getBoxZ(int boxX, double rectScaleX, double rectScaleY, double rectScaleZ, int jagged) {
        return fitBox_yz(zx_ratio * boxX * (rectScaleX / rectScaleZ), jagged);
    }

    @Override
    protected boolean isPointInsideShapeVolumeSecond(double xr, double yr, double zr) {
        if (zr > boundZ)  // upper ellipsoid
            if (Math.abs(xr) <= ell_x2) {
                double zshift = zr - zcenter2;
                if (xr * xr + yr * yr * invsqY2 + zshift * zshift * invsqZ2 <= ell_rsq2) return true;
            }


        return false;
    }

    @Override
    protected boolean isPointInsideShapeVolumeFirst(double xr, double yr, double zr) {
        if (zr <= boundZ) { // lower ellipsoid
            if (Math.abs(xr) <= ell_x1) {
                double zshift = zr - zcenter1;
                if (xr * xr + yr * yr * invsqY + zshift * zshift * invsqZ <= ell_rsq1) return true;
            }
        }

        return false;
    }


}