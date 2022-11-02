package adda.item.tab.shape.selector.params.bicoated;

import adda.base.annotation.Viewable;
import adda.base.models.ModelBase;
import adda.item.tab.shape.selector.params.ModelShapeParam;
import adda.item.tab.shape.selector.params.ModelShapeParamTwoDomains;
import adda.utils.StringHelper;
import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.geometry.Sphere;


import org.jogamp.vecmath.AxisAngle4f;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Vector3d;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class BicoatedModel extends ModelShapeParamTwoDomains {

    @Viewable(value = "<html>R<sub>cc</sub>/d</html>")
    protected double firstParam = 1;
    private double diskratio;
    private double coat_ratio;
    private double coat_r2;
    private double hdratio;
    private int yx_ratio;
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


    @Viewable(value = "<html>d<sub>in</sub>/d</html>")
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


    public List<String> getParamsList() {
        return Arrays.asList(StringHelper.toDisplayString(firstParam), StringHelper.toDisplayString(secondParam));
    }

    @Override
    public boolean validate() {
        boolean isValid = true;
        if (secondParam <= 0 || secondParam > 1) {
            validationErrors.put(SECOND_PARAM, StringHelper.toDisplayString("d<sub>in</sub>/d must be from 0 to 1"));
            isValid = false;
        } else {
            validationErrors.put(SECOND_PARAM, "");
        }

        if (firstParam < 1) {
            validationErrors.put(FIRST_PARAM, StringHelper.toDisplayString("R<sub>cc</sub>/d must be greater than or equal 1"));
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

    @Override
    public void initParams() {
        diskratio =firstParam;
        coat_ratio =secondParam;
        coat_r2 =0.25* coat_ratio * coat_ratio;

        coat_r2 =0.25* coat_ratio * coat_ratio;
        hdratio = diskratio /2.0;
        //if (diskratio >=1) volume_ratio = 2*PI_OVER_SIX;
        //else volume_ratio = PI_OVER_SIX*(2- diskratio)*(1+ diskratio)*(1+ diskratio)/2;
        yx_ratio =1;
        zx_ratio = diskratio +1;
    }

    public void createSurfaceShape(TransformGroup tg) {

        for (int ind=-1;ind<2;ind+=2) {
            TransformGroup tgCoated1 = new TransformGroup();
            Transform3D t3d = new Transform3D();
            tgCoated1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            t3d.setTranslation(new Vector3d(0, 0, ind*firstParam*0.5));
            t3d.setRotation(new AxisAngle4f(0f, 0f, 0f, 0f));
            t3d.setScale(1.0);
            tgCoated1.setTransform(t3d);
            genarateCoated(tgCoated1);
            tg.addChild(tgCoated1);
        }
    }

    private void genarateCoated(TransformGroup tg) {

        Appearance ap = new Appearance();

        PolygonAttributes polyAttrbutes = new PolygonAttributes();
        polyAttrbutes.setPolygonMode(PolygonAttributes.POLYGON_FILL);
        polyAttrbutes.setCullFace(PolygonAttributes.CULL_NONE);
        ap.setPolygonAttributes(polyAttrbutes);

        Material material = new Material();
        material.setShininess(0f);
        Color color = Color.magenta;

        Color3f c = new Color3f(0.75f,0f,0.75f);
        material.setDiffuseColor(c);
        material.setAmbientColor(c);

        ap.setMaterial(material);
        TransparencyAttributes trans = new TransparencyAttributes();
        trans.setTransparencyMode(TransparencyAttributes.BLENDED);
        trans.setTransparency(0.6f);
        ap.setTransparencyAttributes(trans);
        ap.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_READ);
        ap.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
        ap.setCapability(Appearance.ALLOW_MATERIAL_READ);
        ap.setCapability(Appearance.ALLOW_MATERIAL_WRITE);
        ap.setCapability(Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_READ);
        ap.setCapability(Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_WRITE);

        Sphere sphere = new Sphere(0.5f, Sphere.GENERATE_NORMALS | Sphere.GENERATE_NORMALS_INWARD, 100);
        sphere.setAppearance(ap);

        tg.addChild(sphere);


        ap = new Appearance();

        polyAttrbutes = new PolygonAttributes();
        polyAttrbutes.setPolygonMode(PolygonAttributes.POLYGON_FILL);
        polyAttrbutes.setCullFace(PolygonAttributes.CULL_NONE);
        ap.setPolygonAttributes(polyAttrbutes);

        material = new Material();
        material.setShininess(0.5f);

        material.setDiffuseColor(new Color3f(0f, 0.75f, 0f));
        material.setAmbientColor(new Color3f(0f, 0.75f, 0f));

        ap.setMaterial(material);


        Sphere sphere2 = new Sphere((float)(0.5f*secondParam), Sphere.GENERATE_NORMALS, 100);
        sphere2.setAppearance(ap);

        tg.addChild(sphere2);
    }

    @Override
    public int getBoxY(int boxX, double rectScaleX, double rectScaleY, double rectScaleZ, int jagged) {
        return fitBox_yz(boxX * (rectScaleX / rectScaleY), jagged);
    }

    @Override
    public int getBoxZ(int boxX, double rectScaleX, double rectScaleY, double rectScaleZ, int jagged) {
        return fitBox_yz((firstParam + 1) * boxX * (rectScaleX / rectScaleZ), jagged);
    }

//    @Override
//    protected boolean isPointInsideShapeVolume(double xr, double yr, double zr) {
//        double ro2=xr*xr+yr*yr;
//        if (ro2<=0.25) {
//            double tmp1= Math.abs(zr)-firstParam*0.5;
//            if (tmp1*tmp1+ro2<=0.25) {
////                if (tmp1*tmp1+ro2<=coat_r2) mat=1;
////                else mat=0;
//                return true;
//            }
//        }
//        return false;
//    }

    @Override
    protected boolean isPointInsideShapeVolumeFirst(double xr, double yr, double zr) {
        double ro2=xr*xr+yr*yr;
        if (ro2<=0.25) {
            double tmp1=Math.abs(zr)-hdratio;
            if (tmp1*tmp1+ro2<=0.25) {
                return true;
            }
        }
        return false;
    }



    @Override
    protected boolean isPointInsideShapeVolumeSecond(double xr, double yr, double zr) {
        double ro2=xr*xr+yr*yr;
        if (ro2<=0.25) {
            double tmp1=Math.abs(zr)-hdratio;
            if (tmp1*tmp1+ro2<=0.25) {
                if (tmp1*tmp1+ro2<=coat_r2) return true;

            }
        }
        return false;
    }


}