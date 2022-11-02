package adda.item.tab.shape.selector.params.cuboid;

import adda.base.annotation.BindEnableFrom;
import adda.base.annotation.Viewable;
import adda.base.models.ModelBase;
import adda.item.tab.shape.selector.params.ModelShapeParam;
import adda.utils.StringHelper;
import org.jogamp.java3d.Appearance;
import org.jogamp.java3d.Material;
import org.jogamp.java3d.PolygonAttributes;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.utils.geometry.Box;
import org.jogamp.java3d.utils.geometry.Sphere;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CuboidModel extends ModelShapeParam {


    @Viewable(value = "not cube")
    protected boolean isShowFirstParam = false;

    public boolean isShowFirstParam() {
        return isShowFirstParam;
    }

    public void setShowFirstParam(boolean isShowFirstParam) {
        if (this.isShowFirstParam != isShowFirstParam) {
            this.isShowFirstParam = isShowFirstParam;
            notifyObservers("isShowFirstParam", isShowFirstParam);
//            if (!isShowFirstParam) {
//                setShowSecondParam(false);
//            }
        }
        setShowSecondParam(isShowFirstParam);
    }

    @BindEnableFrom("isShowFirstParam")
    @Viewable(value = "y/x")
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


//    @BindEnableFrom("isShowFirstParam")
    //@Viewable(value = "use [z/x]")
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
    @Viewable(value = "z/x")
    protected double secondParam = 1;

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
        if (isShowFirstParam || isShowSecondParam) {
            return Arrays.asList(StringHelper.toDisplayString(firstParam), StringHelper.toDisplayString(secondParam));
        }
        return Collections.emptyList();
    }

    @Override
    public boolean validate() {
        boolean isValid = true;

        if (firstParam > 0) {
            validationErrors.put(FIRST_PARAM, "");
        } else {
            validationErrors.put(FIRST_PARAM,  StringHelper.toDisplayString("y/x must be greater than 0"));
            isValid = false;
        }

        if (secondParam > 0) {
            validationErrors.put(SECOND_PARAM, "");
        } else {
            validationErrors.put(SECOND_PARAM,  StringHelper.toDisplayString("z/x must be greater than 0"));
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
        material.setShininess(0.5f);
        material.setDiffuseColor(getSurfaceColor());
        material.setAmbientColor(getSurfaceColor());
        ap.setMaterial(material);


        Box box = new Box(0.5f, (float) firstParam*0.5f, (float) secondParam*0.5f, ap);
        box.setAppearance(ap);

        tg.addChild(box);
    }

    @Override
    public int getBoxY(int boxX, double rectScaleX, double rectScaleY, double rectScaleZ, int jagged) {
        return fitBox_yz(firstParam * boxX * (rectScaleX / rectScaleY), jagged);
    }

    @Override
    public int getBoxZ(int boxX, double rectScaleX, double rectScaleY, double rectScaleZ, int jagged) {
        return fitBox_yz(secondParam * boxX * (rectScaleX / rectScaleZ), jagged);
    }
}