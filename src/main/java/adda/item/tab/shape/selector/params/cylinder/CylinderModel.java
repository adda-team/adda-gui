package adda.item.tab.shape.selector.params.cylinder;

import adda.base.annotation.Viewable;
import adda.base.models.ModelBase;
import adda.item.tab.shape.selector.params.ModelShapeParam;
import adda.utils.StringHelper;
import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.geometry.Cylinder;
import org.jogamp.java3d.utils.geometry.Sphere;


import org.jogamp.vecmath.AxisAngle4f;
import org.jogamp.vecmath.Vector3d;
import java.util.Arrays;
import java.util.List;

public class CylinderModel extends ModelShapeParam {
    @Viewable(value = "h/d")
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

    public double getInitialScale() {

        final double scale = 4.5 - firstParam;
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



    public void createSurfaceShape(TransformGroup tg) {

        Appearance ap = new Appearance();

        PolygonAttributes polyAttrbutes = new PolygonAttributes();
        polyAttrbutes.setPolygonMode(PolygonAttributes.POLYGON_FILL);
        polyAttrbutes.setCullFace(PolygonAttributes.CULL_NONE);
        ap.setPolygonAttributes(polyAttrbutes);

        Material material = new Material();
        material.setShininess(0.5f);
        material.setDiffuseColor(getSurfaceColor());
        material.setAmbientColor(getSurfaceColor());
        ap.setMaterial(material);


        Cylinder cylinder = new Cylinder(0.5f, (float) firstParam, Cylinder.GENERATE_NORMALS, 100,100, ap);
        cylinder.setAppearance(ap);

        TransformGroup transformGroup = new TransformGroup();
        Transform3D t3d = new Transform3D();
        transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        t3d.setTranslation(new Vector3d(0, 0, 0));
        t3d.setRotation(new AxisAngle4f(1f, 0f, 0f, 3.1415f*0.5f));
        t3d.setScale(1.0);
        transformGroup.setTransform(t3d);

        transformGroup.addChild(cylinder);

        tg.addChild(transformGroup);
    }

    @Override
    public int getBoxY(int boxX, double rectScaleX, double rectScaleY, double rectScaleZ, int jagged) {
        return fitBox_yz(boxX * (rectScaleX / rectScaleY), jagged);
    }

    @Override
    public int getBoxZ(int boxX, double rectScaleX, double rectScaleY, double rectScaleZ, int jagged) {
        return fitBox_yz(firstParam * boxX * (rectScaleX / rectScaleZ), jagged);
    }

    @Override
    protected boolean isPointInsideShapeVolume(double xr, double yr, double zr) {
        return xr*xr+yr*yr<=0.25 && Math.abs(zr)<=firstParam*0.5;
    }
}