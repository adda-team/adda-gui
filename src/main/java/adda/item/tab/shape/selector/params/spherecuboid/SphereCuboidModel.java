package adda.item.tab.shape.selector.params.spherecuboid;

import adda.base.annotation.Viewable;
import adda.base.models.ModelBase;
import adda.item.tab.shape.selector.params.ModelShapeParam;
import adda.item.tab.shape.selector.params.ModelShapeParamTwoDomains;
import adda.utils.StringHelper;
import org.jogamp.java3d.utils.geometry.Box;
import org.jogamp.java3d.utils.geometry.Sphere;

import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.geometry.GeometryInfo;
import org.jogamp.java3d.utils.geometry.NormalGenerator;


import org.jogamp.vecmath.*;
import java.util.Arrays;
import java.util.List;

public class SphereCuboidModel extends ModelShapeParamTwoDomains {

    @Viewable(value = "<html>d<sub>sph</sub>/d")
    protected double firstParam = 0.6;
    private double coat_r2;

    public double getFirstParam() {
        return firstParam;
    }

    public void setFirstParam(double firstParam) {
        if (this.firstParam != firstParam) {
            this.firstParam = firstParam;
            notifyObservers(FIRST_PARAM, firstParam);
        }
    }

    public List<String> getParamsList() {
        return Arrays.asList(StringHelper.toDisplayString(firstParam));
    }


    @Override
    public boolean validate() {
        boolean isValid = true;

        if (firstParam >= 0 && firstParam <= 1) {
            validationErrors.put(FIRST_PARAM, "");
        } else {
            validationErrors.put(FIRST_PARAM, StringHelper.toDisplayString("d<sub>sph</sub>/d must be in [0; 1]"));
            isValid = false;
        }

        return isValid;
    }

    @Override
    public double getInitialScale() {
        return 3;
    }

    public void createSurfaceShape(TransformGroup tg) {

        Appearance ap = new Appearance();

        PolygonAttributes polyAttrbutes = new PolygonAttributes();
        polyAttrbutes.setPolygonMode(PolygonAttributes.POLYGON_FILL);
        polyAttrbutes.setCullFace(PolygonAttributes.CULL_NONE);
        ap.setPolygonAttributes(polyAttrbutes);

        Material material = new Material();
        material.setShininess(0f);

        material.setDiffuseColor(getVoxelColor());
        material.setAmbientColor(getVoxelColor());

        ap.setMaterial(material);
        TransparencyAttributes trans = new TransparencyAttributes();
        trans.setTransparencyMode(TransparencyAttributes.BLENDED);
        trans.setTransparency(0.8f);
        ap.setTransparencyAttributes(trans);
        ap.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_READ);
        ap.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
        ap.setCapability(Appearance.ALLOW_MATERIAL_READ);
        ap.setCapability(Appearance.ALLOW_MATERIAL_WRITE);
        ap.setCapability(Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_READ);
        ap.setCapability(Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_WRITE);

        Box box = new Box(0.5f,0.5f,0.5f, ap);
        //sphere.setAppearance(ap);

        tg.addChild(box);


        ap = new Appearance();

        polyAttrbutes = new PolygonAttributes();
        polyAttrbutes.setPolygonMode(PolygonAttributes.POLYGON_FILL);
        polyAttrbutes.setCullFace(PolygonAttributes.CULL_NONE);
        ap.setPolygonAttributes(polyAttrbutes);

        material = new Material();
        material.setShininess(0.5f);

        material.setDiffuseColor(getVoxelColorSecond());
        material.setAmbientColor(getVoxelColorSecond());

        ap.setMaterial(material);


        TransformGroup tgCoated = new TransformGroup();
        Transform3D t3d = new Transform3D();

        tgCoated.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        t3d.setTranslation(new Vector3d(0, 0, 0));
        t3d.setRotation(new AxisAngle4f(0f, 0f, 0f, 0f));
        t3d.setScale(1.0);

        tgCoated.setTransform(t3d);

        Sphere sphere2 = new Sphere((float)(0.5f*firstParam), Sphere.GENERATE_NORMALS, 100);
        sphere2.setAppearance(ap);

        tgCoated.addChild(sphere2);

        tg.addChild(tgCoated);
    }

    @Override
    protected boolean isPointInsideShapeVolumeFirst(double xr, double yr, double zr) {
        return true;
    }

    @Override
    public void initParams() {
        double coat_ratio=firstParam;
        coat_r2 =0.25*coat_ratio*coat_ratio;
    }

    @Override
    protected boolean isPointInsideShapeVolumeSecond(double xr, double yr, double zr) {
        return xr * xr + yr * yr + zr * zr <= coat_r2;
    }

}