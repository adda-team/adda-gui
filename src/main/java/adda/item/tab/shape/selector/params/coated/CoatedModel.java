package adda.item.tab.shape.selector.params.coated;

import adda.base.annotation.BindEnableFrom;
import adda.base.annotation.Viewable;
import adda.base.models.ModelBase;
import adda.item.tab.shape.selector.params.ModelShapeParam;
import adda.item.tab.shape.selector.params.ModelShapeParamTwoDomains;
import adda.item.tab.shape.selector.params.sphere.SphereModel;
import adda.utils.StringHelper;
import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.geometry.Sphere;


import org.jogamp.vecmath.AxisAngle4f;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Color4f;
import org.jogamp.vecmath.Vector3d;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class CoatedModel extends ModelShapeParamTwoDomains {


    @Viewable(value = "<html>d<sub>in</sub>/d</html>")
    protected double firstParam = 0.5;
    private double coat_ratio;
    private double coat_x;
    private double coat_y;
    private double coat_z;
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


    @Viewable(value = "concentric")
    protected boolean isConcentric = true;

    public boolean isConcentric() {
        return isConcentric;
    }

    public void setConcentric(boolean isConcentric) {
        if (this.isConcentric != isConcentric) {
            this.isConcentric = isConcentric;
            notifyObservers("isConcentric", isConcentric);
            setShowSecondParam(!isConcentric);
            setShowThirdParam(!isConcentric);
            setShowFourthParam(!isConcentric);
        }
    }

    //@Viewable(value = "use [x/d]")
    protected boolean isShowSecondParam = false;

    public boolean isShowSecondParam() {
        return isShowSecondParam;
    }

    public void setShowSecondParam(boolean isShowSecondParam) {
        if (this.isShowSecondParam != isShowSecondParam) {
            this.isShowSecondParam = isShowSecondParam;
            notifyObservers("isShowSecondParam", isShowSecondParam);
        }
    }

    @BindEnableFrom("isShowSecondParam")
    @Viewable(value = "x/d")
    protected double secondParam = 0;

    public double getSecondParam() {
        return secondParam;
    }

    public void setSecondParam(double secondParam) {
        if (this.secondParam != secondParam) {
            this.secondParam = secondParam;
            notifyObservers(SECOND_PARAM, secondParam);
        }
    }



    //@Viewable(value = "use [y/d]:")
    protected boolean isShowThirdParam = false;

    public boolean isShowThirdParam() {
        return isShowThirdParam;
    }

    public void setShowThirdParam(boolean isShowThirdParam) {
        if (this.isShowThirdParam != isShowThirdParam) {
            this.isShowThirdParam = isShowThirdParam;
            notifyObservers("isShowThirdParam", isShowThirdParam);
        }
    }

    @BindEnableFrom("isShowThirdParam")
    @Viewable(value = "y/d")
    protected double thirdParam = 0;

    public double getThirdParam() {
        return thirdParam;
    }

    public void setThirdParam(double thirdParam) {
        if (this.thirdParam != thirdParam) {
            this.thirdParam = thirdParam;
            notifyObservers(THIRD_PARAM, thirdParam);
        }
    }



    //@Viewable(value = "use [z/d]")
    protected boolean isShowFourthParam = false;

    public boolean isShowFourthParam() {
        return isShowFourthParam;
    }

    public void setShowFourthParam(boolean isShowFourthParam) {
        if (this.isShowFourthParam != isShowFourthParam) {
            this.isShowFourthParam = isShowFourthParam;
            notifyObservers("isShowFourthParam", isShowFourthParam);
        }
    }

    @BindEnableFrom("isShowFourthParam")
    @Viewable(value = "z/d")
    protected double fourthParam = 0;

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
        if (isShowSecondParam || isShowThirdParam || isShowFourthParam) {
            return Arrays.asList(StringHelper.toDisplayString(firstParam), StringHelper.toDisplayString(secondParam), StringHelper.toDisplayString(thirdParam), StringHelper.toDisplayString(fourthParam));
        }
        return Arrays.asList(StringHelper.toDisplayString(firstParam));
    }

    @Override
    public boolean validate() {
        boolean isValid = true;

        if (firstParam >= 0 && firstParam <= 1) {
            validationErrors.put(FIRST_PARAM, "");
        } else {
            validationErrors.put(FIRST_PARAM,  StringHelper.toDisplayString("d<sub>in</sub>/d must be in [0; 1]"));
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
        material.setShininess(0f);
        Color color = Color.magenta;

        Color3f c = new Color3f(255, 0, 255);
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


        TransformGroup tgCoated = new TransformGroup();
        Transform3D t3d = new Transform3D();

        tgCoated.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);


        if (isConcentric()) {
            t3d.setTranslation(new Vector3d(0, 0, 0));
        } else {
            t3d.setTranslation(new Vector3d(secondParam, thirdParam, fourthParam));
        }
        t3d.setRotation(new AxisAngle4f(0f, 0f, 0f, 0f));
        t3d.setScale(1.0);

        tgCoated.setTransform(t3d);

        Sphere sphere2 = new Sphere((float)(0.5f*firstParam), Sphere.GENERATE_NORMALS, 100);
        sphere2.setAppearance(ap);

        tgCoated.addChild(sphere2);

        tg.addChild(tgCoated);
    }

    @Override
    public void initParams() {
        coat_ratio =firstParam;
        coat_x=secondParam;
        coat_y =thirdParam;
        coat_z =fourthParam;
        if (isConcentric()) {
            coat_x=0;
            coat_y =0;
            coat_z =0;
        }
        coat_r2 =0.25*coat_ratio*coat_ratio;
    }

    @Override
    protected boolean isPointInsideShapeVolumeFirst(double xr, double yr, double zr) {
        if (xr*xr+yr*yr+zr*zr<=0.25) { // first test to skip some dipoles immediately)
            return true;
        }
        return false;
    }



    @Override
    protected boolean isPointInsideShapeVolumeSecond(double xr, double yr, double zr) {
        if (xr*xr+yr*yr+zr*zr<=0.25) { // first test to skip some dipoles immediately)
            double xcoat=xr-coat_x;
            double ycoat=yr- coat_y;
            double zcoat=zr- coat_z;
            if (xcoat*xcoat+ycoat*ycoat+zcoat*zcoat<= coat_r2) return true;

        }

        return false;
    }

}