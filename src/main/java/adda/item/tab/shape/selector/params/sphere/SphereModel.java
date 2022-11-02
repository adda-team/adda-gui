package adda.item.tab.shape.selector.params.sphere;

import adda.base.models.ModelBase;
import adda.item.tab.shape.selector.params.ModelShapeParam;
import org.jogamp.java3d.Appearance;
import org.jogamp.java3d.Material;
import org.jogamp.java3d.PolygonAttributes;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.utils.geometry.GeometryInfo;
import org.jogamp.java3d.utils.geometry.NormalGenerator;
import org.jogamp.java3d.utils.geometry.Sphere;



import java.util.ArrayList;
import java.util.List;

public class SphereModel extends ModelShapeParam {
    @Override
    public List<String> getParamsList() {
        return new ArrayList<>();
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


        Sphere sphere = new Sphere(0.5f, Sphere.GENERATE_NORMALS, 100);
        sphere.setAppearance(ap);

        tg.addChild(sphere);
    }

    @Override
    public List<Double> getSurfacePoints() {


        int countOfStep = 301;

        double step = 1.0 / countOfStep;

        ArrayList<Double> x = new ArrayList<>();
        ArrayList<Double> y = new ArrayList<>();
        ArrayList<Double> z = new ArrayList<>();


        final double d = 0.5;
        double currentZ, r2;
        double currentXSnapshot, currentYSnapshot;
        for (double currentX = -d; currentX < d; currentX += step) {
            for (double currentY = -d; currentY < d; currentY += step) {
                double[] buffer = new double[12];
                currentXSnapshot = currentX;
                currentYSnapshot = currentY;
                for (int inner = 0; inner < 4; inner++) {
                    if (inner == 1) currentX += step;
                    if (inner == 2) currentY += step;
                    if (inner == 3) currentX -= step;
                    r2 = currentX * currentX + currentY * currentY;

                    if (Math.sqrt(r2) < d) {
                        currentZ = Math.sqrt(0.25 - r2);

                        buffer[3 * inner] = currentX;
                        buffer[3 * inner + 1] = currentY;
                        buffer[3 * inner + 2] = currentZ;

                    } else {
                        double alpha = Math.atan(Math.abs(currentY / currentX));
                        buffer[3 * inner] = Math.signum(currentX) * d * Math.cos(alpha);
                        buffer[3 * inner + 1] = Math.signum(currentY) * d * Math.sin(alpha);
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
    protected boolean isPointInsideShapeVolume(double xr, double yr, double zr) {
        return xr*xr+yr*yr+zr*zr<=0.25;
    }

}