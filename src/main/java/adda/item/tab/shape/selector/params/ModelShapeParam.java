package adda.item.tab.shape.selector.params;

import adda.base.models.ModelBase;
import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;

import javax.media.j3d.*;
import javax.vecmath.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ModelShapeParam extends ModelBase {

    public static final String FIRST_PARAM = "firstParam";
    public static final String SECOND_PARAM = "secondParam";
    public static final String THIRD_PARAM = "thirdParam";
    public static final String FOURTH_PARAM = "fourthParam";
    public static final String FIFTH_PARAM = "fifthParam";

    public abstract List<String> getParamsList();

    public void createSurfaceShape(TransformGroup tg) {
        initParams();
        List<Double> points = getSurfacePoints();

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
        material.setShininess(50f);
        material.setDiffuseColor(getSurfaceColor());
        material.setAmbientColor(getSurfaceColor());
        ap.setMaterial(material);

        Shape3D shape1 = new Shape3D( gi.getGeometryArray(), ap );
        tg.addChild(shape1);
    }


    protected List<Double> getSurfacePoints() {
        return new ArrayList<>();
    }

    public void createVoxelizedShape(TransformGroup tg, int boxX, int jagged, double[] rectDip) {
        initParams();
        double gridspace = 1.0/ boxX;

        double xr,yr,zr;
        double rectScaleX,rectScaleY,rectScaleZ;
        int xj,yj,zj;

        rectScaleX = rectDip[0];
        rectScaleY = rectDip[1];
        rectScaleZ = rectDip[2];

        double gridSpaceX=gridspace*rectScaleX;
        double gridSpaceY=gridspace*rectScaleY;
        double gridSpaceZ=gridspace*rectScaleZ;

        //double gridSpace = Math.max(gridSpaceX, Math.max(gridSpaceZ, gridSpaceY));

        ArrayList<Point3d> point3dArrayList = new ArrayList<Point3d>();

        int boxY = getBoxY(boxX, rectScaleX, rectScaleY, rectScaleZ, jagged);//fitBox_yz(boxX*(rectScaleX/rectScaleY), jagged);
        int boxZ = getBoxZ(boxX, rectScaleX, rectScaleY, rectScaleZ, jagged);//fitBox_yz(h*boxX*(rectScaleX/rectScaleZ), jagged);

        Map<String, Point3d> hash = new HashMap<>();

        for (int k = 0; k < boxZ; k++)
            for (int j = 0; j < boxY; j++)
                for (int i = 0; i < boxX; i++) {
                    xj = 2 * jagged * (i / jagged) + jagged - boxX;
                    yj = 2 * jagged * (j / jagged) + jagged - boxY;
                    zj = 2 * jagged * (k / jagged) + jagged - boxZ;
                    /* all the following coordinates should be scaled by the same sizeX. So we scale xj,yj,zj by 2boxX with extra
                     * ratio for rectangular dipoles. Thus, yr and zr are not necessarily in fixed ranges (like from -1/2 to 1/2).
                     * This is done to treat adequately cases when particle dimensions are the same (along different axes), but e.g.
                     * boxY!=boxX (so there are some extra void dipoles). All anisotropies in the particle itself are treated in
                     * the specific shape modules below (see e.g. ELLIPSOID).
                     */
                    xr = (0.5 * xj) / boxX;
                    yr = (0.5 * yj) / boxX * (rectScaleY / rectScaleX);
                    zr = (0.5 * zj) / boxX * (rectScaleZ / rectScaleX);

                    if (isPointInsideShapeVolume(xr, yr, zr)) {
                        hash.put(getVoxelCenterKey(i, j, k), new Point3d(xr, yr, zr));
                        //addVoxel(xr, yr, zr, gridSpaceX, gridSpaceY, gridSpaceZ, point3dArrayList);
                    }
                }

        for (String key : hash.keySet()) {
            String[] raw = key.split("_");
            int i = Integer.parseInt(raw[0]);
            int j = Integer.parseInt(raw[1]);
            int k = Integer.parseInt(raw[2]);

            boolean isExternalVoxel = i == 0 || j == 0 || k == 0;
            if (!isExternalVoxel) {
                checkIsExternalVoxel : {
                    for (int indX=-1;indX<2;indX+=2)
                        for (int indY=-1;indY<2;indY+=2)
                            for (int indZ=-1;indZ<2;indZ+=2) {
                                isExternalVoxel = !hash.containsKey(getVoxelCenterKey(i + indX, j + indY, k + indZ));
                                if (isExternalVoxel) {
                                    break checkIsExternalVoxel;
                                }
                            }
                }
            }

            if (isExternalVoxel) {
                Point3d point = hash.get(key);
                addVoxel(point.x, point.y, point.z, gridSpaceX, gridSpaceY, gridSpaceZ, point3dArrayList);
            }
        }

        if (point3dArrayList.size() < 1) {
            return;
        }

        ArrayList<Double> points = new ArrayList<Double>();
        for (Point3d point3d : point3dArrayList) {
            points.add(point3d.x);
            points.add(point3d.y);
            points.add(point3d.z);
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
        material.setShininess(50f);
        material.setAmbientColor(getVoxelColor());
        material.setDiffuseColor(getVoxelColor());
        ap.setMaterial(material);

//add both a wireframe and a solid version
//of the triangulated surface
        Shape3D shape1 = new Shape3D( gi.getGeometryArray(), ap );
        tg.addChild(shape1);

        ap = new Appearance();
        polyAttrbutes = new PolygonAttributes();
        polyAttrbutes.setPolygonMode(PolygonAttributes.POLYGON_LINE);
        polyAttrbutes.setCullFace(PolygonAttributes.CULL_NONE);
        ap.setPolygonAttributes(polyAttrbutes);
        material = new Material();
        material.setShininess(50f);
        material.setDiffuseColor(getVoxelLineColor());
        material.setAmbientColor(getVoxelLineColor());
        ap.setMaterial(material);
        Shape3D shape2 = new Shape3D(gi.getGeometryArray(), ap);
        tg.addChild(shape2);
    }

    protected void initParams() {

    }

    public int getBoxY(int boxX, double rectScaleX, double rectScaleY, double rectScaleZ, int jagged) {
        return fitBox_yz(boxX*(rectScaleX/rectScaleY), jagged);
    }

    public int getBoxZ(int boxX, double rectScaleX, double rectScaleY, double rectScaleZ, int jagged) {
        return fitBox_yz(boxX*(rectScaleX/rectScaleZ), jagged);
    }

    protected boolean isPointInsideShapeVolume(double xr, double yr, double zr) {
        return true;
    }

    protected void addVoxel(double xr, double yr, double zr, double gridSpaceX, double gridSpaceY, double gridSpaceZ, ArrayList<Point3d> point3dArrayList) {
        Point3d point1 = new Point3d(xr + 0.5*gridSpaceX, yr - 0.5*gridSpaceY, zr - 0.5*gridSpaceZ);
        Point3d point2 = new Point3d(xr + 0.5*gridSpaceX, yr - 0.5*gridSpaceY, zr + 0.5*gridSpaceZ);
        Point3d point3 = new Point3d(xr - 0.5*gridSpaceX, yr - 0.5*gridSpaceY, zr + 0.5*gridSpaceZ);
        Point3d point4 = new Point3d(xr - 0.5*gridSpaceX, yr - 0.5*gridSpaceY, zr - 0.5*gridSpaceZ);

        Point3d point5 = new Point3d(xr + 0.5*gridSpaceX, yr + 0.5*gridSpaceY, zr - 0.5*gridSpaceZ);
        Point3d point6 = new Point3d(xr - 0.5*gridSpaceX, yr + 0.5*gridSpaceY, zr - 0.5*gridSpaceZ);
        Point3d point7 = new Point3d(xr - 0.5*gridSpaceX, yr + 0.5*gridSpaceY, zr + 0.5*gridSpaceZ);
        Point3d point8 = new Point3d(xr + 0.5*gridSpaceX, yr + 0.5*gridSpaceY, zr + 0.5*gridSpaceZ);


        point3dArrayList.add(point1);
        point3dArrayList.add(point2);
        point3dArrayList.add(point3);
        point3dArrayList.add(point4);
        point3dArrayList.add(point5);
        point3dArrayList.add(point6);
        point3dArrayList.add(point7);
        point3dArrayList.add(point8);


        Point3d point9 = new Point3d(xr + 0.5*gridSpaceX, yr + 0.5*gridSpaceY, zr - 0.5*gridSpaceZ);
        Point3d point10 = new Point3d(xr + 0.5*gridSpaceX, yr + 0.5*gridSpaceY, zr + 0.5*gridSpaceZ);
        Point3d point11 = new Point3d(xr + 0.5*gridSpaceX, yr - 0.5*gridSpaceY, zr + 0.5*gridSpaceZ);
        Point3d point12 = new Point3d(xr + 0.5*gridSpaceX, yr - 0.5*gridSpaceY, zr - 0.5*gridSpaceZ);

        Point3d point13 = new Point3d(xr - 0.5*gridSpaceX, yr + 0.5*gridSpaceY, zr - 0.5*gridSpaceZ);
        Point3d point14 = new Point3d(xr - 0.5*gridSpaceX, yr - 0.5*gridSpaceY, zr - 0.5*gridSpaceZ);
        Point3d point15 = new Point3d(xr - 0.5*gridSpaceX, yr - 0.5*gridSpaceY, zr + 0.5*gridSpaceZ);
        Point3d point16 = new Point3d(xr - 0.5*gridSpaceX, yr + 0.5*gridSpaceY, zr + 0.5*gridSpaceZ);

        point3dArrayList.add(point9);
        point3dArrayList.add(point10);
        point3dArrayList.add(point11);
        point3dArrayList.add(point12);
        point3dArrayList.add(point13);
        point3dArrayList.add(point14);
        point3dArrayList.add(point15);
        point3dArrayList.add(point16);


        Point3d point17 = new Point3d(xr + 0.5*gridSpaceX, yr + 0.5*gridSpaceY, zr + 0.5*gridSpaceZ);
        Point3d point18 = new Point3d(xr - 0.5*gridSpaceX, yr + 0.5*gridSpaceY, zr + 0.5*gridSpaceZ);
        Point3d point19 = new Point3d(xr - 0.5*gridSpaceX, yr - 0.5*gridSpaceY, zr + 0.5*gridSpaceZ);
        Point3d point20 = new Point3d(xr + 0.5*gridSpaceX, yr - 0.5*gridSpaceY, zr + 0.5*gridSpaceZ);

        Point3d point21 = new Point3d(xr + 0.5*gridSpaceX, yr + 0.5*gridSpaceY, zr - 0.5*gridSpaceZ);
        Point3d point22 = new Point3d(xr + 0.5*gridSpaceX, yr - 0.5*gridSpaceY, zr - 0.5*gridSpaceZ);
        Point3d point23 = new Point3d(xr - 0.5*gridSpaceX, yr - 0.5*gridSpaceY, zr - 0.5*gridSpaceZ);
        Point3d point24 = new Point3d(xr - 0.5*gridSpaceX, yr + 0.5*gridSpaceY, zr - 0.5*gridSpaceZ);

        point3dArrayList.add(point17);
        point3dArrayList.add(point18);
        point3dArrayList.add(point19);
        point3dArrayList.add(point20);
        point3dArrayList.add(point21);
        point3dArrayList.add(point22);
        point3dArrayList.add(point23);
        point3dArrayList.add(point24);
    }

    public static int fitBox(int box, int jagged) {
        return jagged*((box+jagged-1)/jagged);
    }

    public static int fitBox_yz(double size, int jagged)
        /* given the size of the particle in y or z direction (in units of dipoles), finds the closest grid size, which would
         * satisfy the FitBox function. The rounding is performed so to minimize the maximum difference between the stack of
         * dipoles and corresponding particle dimension.
         * The distance between the center of the outer super-dipole (J^3 original dipoles) and the particle (enclosing box)
         * boundary is between 0.25 and 0.75 of super-dipole size, corresponding to the discretization along the x-axis, for
         * which the optimum distance of 0.5 (the dipole cube fits tight into the boundary) is automatically satisfied.
         *
         * !!! However, it is still possible that the whole outer layer of dipoles would be void because the estimate does not
         * take into account the details of the shape e.g. its curvature. For instance, 'adda -grid 4 -shape ellipsoid 1 2.13'
         * results in grid 4x4x9, but the layers z=0, z=8 will be void. This is because the dipole centers in this layers always
         * have non-zero x and y coordinates (at least half-dipole in absolute value) and do not fall inside the ellipsoid,
         * although the points {+-4,0,0} do fall into it.
         */
    {
        return jagged*(int)Math.round(size/jagged);
    }

    protected String getVoxelCenterKey(int i, int j, int k) {
        StringBuilder builder = new StringBuilder();
        builder.append(i);
        builder.append("_");
        builder.append(j);
        builder.append("_");
        builder.append(k);
        return builder.toString();
    }

    protected Color3f getVoxelColor() {
        return new Color3f(Color.MAGENTA);
    }

    protected Color3f getVoxelLineColor() {
        return new Color3f(Color.BLACK);
    }


    protected Color3f getSurfaceColor() {
        return new Color3f(Color.MAGENTA);
    };
}