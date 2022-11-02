package adda.item.tab.shape.selector.params.bisphere;

import adda.base.annotation.Viewable;
import adda.base.models.ModelBase;
import adda.item.tab.shape.selector.params.ModelShapeParam;
import adda.utils.StringHelper;
import org.jogamp.java3d.utils.geometry.Sphere;

import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.geometry.GeometryInfo;
import org.jogamp.java3d.utils.geometry.NormalGenerator;


import org.jogamp.vecmath.*;
import org.jogamp.vecmath.AxisAngle4f;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Vector3d;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class BisphereModel extends ModelShapeParam {


    @Viewable(value = "<html>R<sub>cc</sub>/d</html>")
    protected double firstParam = 1;

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
        if (firstParam < 1) {
            validationErrors.put(FIRST_PARAM,  StringHelper.toDisplayString("R<sub>cc</sub>/d must be greater than or equal 1"));
            isValid = false;
        } else {
            validationErrors.put(FIRST_PARAM, "");
        }
        return isValid;
    }

    public double getInitialScale() {
        final double scale = 4 - firstParam;
        return scale < 0 ? 1.0 : scale;
    }


    public void createSurfaceShape(TransformGroup tg) {

        for (int ind=-1;ind<2;ind+=2) {
            TransformGroup transformGroup = new TransformGroup();
            Transform3D t3d = new Transform3D();
            transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            t3d.setTranslation(new Vector3d(0, 0, ind*firstParam*0.5));
            t3d.setRotation(new AxisAngle4f(0f, 0f, 0f, 0f));
            t3d.setScale(1.0);
            transformGroup.setTransform(t3d);
            genarateCoated(transformGroup);
            tg.addChild(transformGroup);
        }
    }

    private void genarateCoated(TransformGroup tg) {

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


        Sphere sphere = new Sphere(0.5f, Sphere.GENERATE_NORMALS, 100);
        sphere.setAppearance(ap);

        tg.addChild(sphere);


    }

    @Override
    public int getBoxY(int boxX, double rectScaleX, double rectScaleY, double rectScaleZ, int jagged) {
        return fitBox_yz(boxX * (rectScaleX / rectScaleY), jagged);
    }

    @Override
    public int getBoxZ(int boxX, double rectScaleX, double rectScaleY, double rectScaleZ, int jagged) {
        return fitBox_yz((firstParam + 1) * boxX * (rectScaleX / rectScaleZ), jagged);
    }

    @Override
    protected boolean isPointInsideShapeVolume(double xr, double yr, double zr) {
        double ro2=xr*xr+yr*yr;
        if (ro2<=0.25) {
            double tmp1= Math.abs(zr)-firstParam*0.5;
            if (tmp1*tmp1+ro2<=0.25) {
//                if (tmp1*tmp1+ro2<=coat_r2) mat=1;
//                else mat=0;
                return true;
            }
        }
        return false;
    }
}