package adda.item.tab.shape.selector.params.rbc;

import adda.base.annotation.Viewable;
import adda.base.models.ModelBase;
import adda.item.tab.shape.selector.params.ModelShapeParam;
import adda.utils.StringHelper;
import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;

import javax.media.j3d.*;
import javax.vecmath.Color3f;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RbcModel extends ModelShapeParam {


    @Viewable(value = "h/d")
    protected double firstParam = 0.35;

    public double getFirstParam() {
        return firstParam;
    }

    public void setFirstParam(double firstParam) {
        if (this.firstParam != firstParam) {
            this.firstParam = firstParam;
            notifyObservers(FIRST_PARAM, firstParam);
        }
    }



    @Viewable(value = "b/d")
    protected double secondParam = 0.2;

    public double getSecondParam() {
        return secondParam;
    }

    public void setSecondParam(double secondParam) {
        if (this.secondParam != secondParam) {
            this.secondParam = secondParam;
            notifyObservers(SECOND_PARAM, secondParam);
        }
    }



    @Viewable(value = "c/d")
    protected double thirdParam = 0.65;

    public double getThirdParam() {
        return thirdParam;
    }

    public void setThirdParam(double thirdParam) {
        if (this.thirdParam != thirdParam) {
            this.thirdParam = thirdParam;
            notifyObservers(THIRD_PARAM, thirdParam);
        }
    }


    public List<String> getParamsList() {
        return Arrays.asList(StringHelper.toDisplayString(firstParam), StringHelper.toDisplayString(secondParam), StringHelper.toDisplayString(thirdParam));
    }


    @Override
    public boolean validate() {
        boolean isValid = true;

        if (firstParam > secondParam) {
            if (firstParam > 0) {
                validationErrors.put(FIFTH_PARAM, "");
            } else {
                validationErrors.put(FIFTH_PARAM,  StringHelper.toDisplayString("n must be greater than 0"));
                isValid = false;
            }

            if (secondParam > 0) {
                validationErrors.put(SECOND_PARAM, "");
            } else {
                validationErrors.put(SECOND_PARAM,  StringHelper.toDisplayString("n must be greater than 0"));
                isValid = false;
            }
        } else {
            validationErrors.put(FIRST_PARAM,  StringHelper.toDisplayString("h/d must be greater than b/d"));
            validationErrors.put(SECOND_PARAM,  StringHelper.toDisplayString("h/d must be greater than b/d"));
            isValid = false;
        }


        if (thirdParam > 0 && thirdParam < 1) {
            validationErrors.put(THIRD_PARAM, "");
        } else {
            validationErrors.put(THIRD_PARAM,  StringHelper.toDisplayString("c/d must be in (0; 1)"));
            isValid = false;
        }



        return isValid;
    }


    @Override
    public void createSurfaceShape(TransformGroup tg) {
        double D = 1.0;//7.65;
        double c = thirdParam;
        double h = firstParam;
        double b = secondParam;

//        P = -D^2/4 - b^2*h^2/4/D^2+ b^2*c^4/4/D^2/(h^2 - b^2);
//        Q = (D^4 + 4*D^2*P -b^4)/4/b^2;
//        R = -D^2/16*(D^2+4*P);
//        S = -(c^2 + 2*P)/h^2;

        double P = -D * D / 4 - b * b * h * h / 4 / (D * D) + b * b * c * c * c * c / 4 / (D * D) / (h * h - b * b);
        double Q = (D * D * D * D + 4 * D * D * P - b * b * b * b) / 4 / (b * b);
        double R = -D * D / 16 * (D * D + 4 * P);
        double S = -(c * c + 2 * P) / (h * h);

        int countOfStep = 101;

        double step = D / countOfStep;
//        arr = -D/2:step:D/2;
// [ X , Y ] = meshgrid( arr);
//        arr = [ arr D/2:-step:-D/2];

        ArrayList<Double> x = new ArrayList<Double>();
        ArrayList<Double> y = new ArrayList<Double>();
        ArrayList<Double> z = new ArrayList<Double>();

//        for (double currentX = -D*0.5 + step; currentX < D*0.5 - step; currentX+=step) {
//            for (double currentY = -D + step; currentY < D - step; currentY+=step) {
//                if (Math.sqrt(currentX*currentX + currentY*currentY) < )
//            }
//        }

        final double d = D * 0.5;
        double currentZ, r2, temp;
        double currentXSnapshot, currentYSnapshot;
        for (double currentX = -d; currentX < d; currentX += step) {
            for (double currentY = -d; currentY < d; currentY += step) {
                double[] buffer = new double[12];
                boolean isOk = true;
                currentXSnapshot = currentX;
                currentYSnapshot = currentY;
                for (int inner = 0; inner < 4; inner++) {
                    if (inner == 1) currentX += step;
                    if (inner == 2) currentY += step;
                    if (inner == 3) currentX -= step;
                    r2 = currentX * currentX + currentY * currentY;
                    temp = 2 * S * r2 + Q;
                    if (Math.sqrt(r2) < d) {
                        currentZ = Math.sqrt(0.5 * (Math.sqrt(temp * temp - 4 * (R + P * r2 + r2 * r2)) - 2 * S * r2 - Q));
//                        z.add(currentZ);
//                        x.add(currentX);
//                        y.add(currentY);
                        buffer[3*inner] = currentX;
                        buffer[3*inner+1] = currentY;
                        buffer[3*inner+2] = currentZ;

                    } else {
//                        isOk = false;
//                        break;
                        double alpha = Math.atan(Math.abs(currentY/currentX));
//                        if (currentY < 0 && currentX < 0) {
//                            alpha = Math.PI + alpha;
//                        }
//                        if (currentY < 0 && currentX > 0) {
//                            alpha = 2*Math.PI - alpha;
//                        }
                        buffer[3*inner] = Math.signum(currentX)*d*Math.cos(alpha);
                        buffer[3*inner+1] =  Math.signum(currentY)*d*Math.sin(alpha);
                        buffer[3*inner+2] = 0;
                    }
                }
                if (isOk) {
                    for (int index = 0; index < 4; index++) {

                        x.add(buffer[3*index]);
                        y.add(buffer[3*index+1]);
                        z.add(buffer[3*index+2]);
                    }

                }
                currentX = currentXSnapshot;
                currentY = currentYSnapshot;
            }

        }
//        for (double currentX = -d; currentX <= d; currentX+=step) {
//            for (double currentY = -d; currentY <= d; currentY+=step) {
//                r2 = currentX * currentX + currentY * currentY;
//                temp = 2*S*r2 + Q;
//                if (Math.sqrt(r2) <= d) {
//                    currentZ = Math.sqrt(0.5*(Math.sqrt(temp*temp - 4*(R +P*r2 + r2*r2)) -2*S*r2 - Q));
//                    z.add(currentZ);
//                    x.add(currentX);
//                    y.add(currentY);
//
//
//                }
//            }
//        }

//        for (double currentR = step; currentR <= d; currentR+=step) {
//            for (double alfa = 0; alfa < 2*Math.PI; alfa+=Math.PI/180) {
//                r2 = currentR*currentR;
//                temp = 2*S*r2 + Q;
//                if (Math.sqrt(r2) <= d) {
//                    currentZ = Math.sqrt(0.5*(Math.sqrt(temp*temp - 4*(R +P*r2 + r2*r2)) -2*S*r2 - Q));
//                    z.add(currentZ);
//                    x.add(currentR*Math.cos(alfa));
//                    y.add(currentR*Math.sin(alfa));
//                }
//            }
//        }

        ArrayList<Double> points = new ArrayList<Double>();
        for (int i = 0; i < x.size(); i++) {
            points.add(x.get(i));
            points.add(y.get(i));
            points.add(z.get(i));
        }

        for (int i = x.size()-1; i >= 0; i--) {
            points.add(x.get(i));
            points.add(y.get(i));
            points.add(-z.get(i));
        }


//triangulate the polygon
        GeometryInfo gi = new GeometryInfo(GeometryInfo.QUAD_ARRAY);
//        gi.setCoordinates( m_VertexArray );
        gi.setCoordinates(points.stream().mapToDouble(i -> i).toArray());

//        Stripifier st = new Stripifier();
//        st.stripify(gi);
//the first 10 points make up the outer edge of the polygon,
//the next five make up the hole
//        int[] stripCountArray = {10,5};
//        int[] stripCountArray = {0};
//        int[] countourCountArray = {stripCountArray.length};
//        gi.setContourCounts( countourCountArray );
//        gi.setStripCounts( stripCountArray );
//        Triangulator triangulator = new Triangulator();
//        triangulator.triangulate( gi );
//also generate normal vectors so that the surface can be light
        NormalGenerator normalGenerator = new NormalGenerator();
        normalGenerator.generateNormals(gi);
//create an appearance
        Appearance ap = new Appearance();
//render as a wireframe
        PolygonAttributes polyAttrbutes = new PolygonAttributes();
        polyAttrbutes.setPolygonMode(PolygonAttributes.POLYGON_FILL);
        polyAttrbutes.setCullFace(PolygonAttributes.CULL_NONE);
        ap.setPolygonAttributes(polyAttrbutes);

        Material material = new Material();
        material.setShininess(50f);
        material.setDiffuseColor(new Color3f(Color.RED));
        material.setAmbientColor(new Color3f(Color.RED));
        ap.setMaterial(material);
//add both a wireframe and a solid version
//of the triangulated surface
        Shape3D shape1 = new Shape3D( gi.getGeometryArray(), ap );
        tg.addChild(shape1);






//        ap = new Appearance();
//        polyAttrbutes = new PolygonAttributes();
//        polyAttrbutes.setPolygonMode(PolygonAttributes.POLYGON_LINE);
//        polyAttrbutes.setCullFace(PolygonAttributes.CULL_NONE);
//        ap.setPolygonAttributes(polyAttrbutes);
//        material = new Material();
//        material.setShininess(50f);
//        material.setDiffuseColor(new Color3f(Color.BLACK));
//        material.setAmbientColor(new Color3f(Color.BLACK));
//        ap.setMaterial(material);
//        Shape3D shape2 = new Shape3D(gi.getGeometryArray(), ap);
//        tg.addChild(shape2);
    }


    @Override
    public void createVoxelizedShape(TransformGroup transformGroup) {
        /// TODO temporary, must be abstract
    }
}