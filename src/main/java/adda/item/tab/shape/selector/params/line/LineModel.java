package adda.item.tab.shape.selector.params.line;

import adda.base.models.ModelBase;
import adda.item.tab.shape.selector.params.ModelShapeParam;
import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.geometry.Cylinder;


import org.jogamp.vecmath.AxisAngle4f;
import org.jogamp.vecmath.Vector3d;
import java.util.ArrayList;
import java.util.List;

public class LineModel extends ModelShapeParam {
    @Override
    public List<String> getParamsList() {
        return new ArrayList<>();
    }
    @Override
    public double getInitialScale() {
        return 5;
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


        Cylinder cylinder = new Cylinder(0.03f, 1.f, Cylinder.GENERATE_NORMALS, 100,100, ap);
        cylinder.setAppearance(ap);

        TransformGroup transformGroup = new TransformGroup();
        Transform3D t3d = new Transform3D();
        transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        t3d.setTranslation(new Vector3d(0, 0, 0));
        t3d.setRotation(new AxisAngle4f(0f, 0f, 1f, 3.1415f*0.5f));
        t3d.setScale(1.0);
        transformGroup.setTransform(t3d);

        transformGroup.addChild(cylinder);

        tg.addChild(transformGroup);
    }

    @Override
    public int getBoxY(int boxX, double rectScaleX, double rectScaleY, double rectScaleZ, int jagged) {
        return fitBox_yz(jagged * (rectScaleX / rectScaleY), jagged);
    }

    @Override
    public int getBoxZ(int boxX, double rectScaleX, double rectScaleY, double rectScaleZ, int jagged) {
        return fitBox_yz( jagged * (rectScaleX / rectScaleZ), jagged);
    }

    @Override
    protected boolean isPointInsideShapeVolume(double xr, double yr, double zr) {
        return ((yr==0 || yr==-jagged) && (zr==0 || zr==-jagged));///TODO WRONG!! zr must be transform to zj see adda code (make particle [line shape])
    }
}