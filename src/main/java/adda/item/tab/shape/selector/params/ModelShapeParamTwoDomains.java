package adda.item.tab.shape.selector.params;

import org.jogamp.java3d.utils.geometry.GeometryInfo;
import org.jogamp.java3d.utils.geometry.NormalGenerator;

import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.geometry.GeometryInfo;
import org.jogamp.java3d.utils.geometry.NormalGenerator;


import org.jogamp.vecmath.*;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Point3d;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class ModelShapeParamTwoDomains extends ModelShapeParam {

    protected boolean isPointInsideShapeVolumeFirst(double xr, double yr, double zr) {
        return false;
    }


    protected boolean isPointInsideShapeVolumeSecond(double xr, double yr, double zr) {
        return false;
    }

    protected boolean isOpacityForFirst() {
        return true;
    }



    public void createVoxelizedShape(TransformGroup tg, int boxX, int jagged, double[] rectDip) {
        this.jagged = jagged;
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

        ArrayList<Point3d> point3dArrayList = new ArrayList<>();
        ArrayList<Point3d> point3dArrayListSecond = new ArrayList<>();

        int boxY = getBoxY(boxX, rectScaleX, rectScaleY, rectScaleZ, jagged);//fitBox_yz(boxX*(rectScaleX/rectScaleY), jagged);
        int boxZ = getBoxZ(boxX, rectScaleX, rectScaleY, rectScaleZ, jagged);//fitBox_yz(h*boxX*(rectScaleX/rectScaleZ), jagged);

        Map<String, Point3d> hash = new HashMap<>();
        Map<String, Point3d> hashSecond = new HashMap<>();

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

                    if (isPointInsideShapeVolumeFirst(xr, yr, zr)) {
                        hash.put(getVoxelCenterKey(i, j, k), new Point3d(xr, yr, zr));
                        //addVoxel(xr, yr, zr, gridSpaceX, gridSpaceY, gridSpaceZ, point3dArrayList);
                    }

                    if (isPointInsideShapeVolumeSecond(xr, yr, zr)) {
                        hashSecond.put(getVoxelCenterKey(i, j, k), new Point3d(xr, yr, zr));
                        //addVoxel(xr, yr, zr, gridSpaceX, gridSpaceY, gridSpaceZ, point3dArrayList);
                    }

                }

        filterPoints(gridSpaceX, gridSpaceY, gridSpaceZ, point3dArrayList, hash);
        filterPoints(gridSpaceX, gridSpaceY, gridSpaceZ, point3dArrayListSecond, hashSecond);

        if (point3dArrayList.size() < 1 && point3dArrayListSecond.size() < 1) {
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
        material.setShininess(0.5f);
        material.setAmbientColor(getVoxelColor());
        material.setDiffuseColor(getVoxelColor());
        ap.setMaterial(material);
        if (isOpacityForFirst()) {
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

            Shape3D shape1 = new Shape3D( gi.getGeometryArray(), ap );
            tg.addChild(shape1);
        } else {
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
            material.setShininess(0.5f);
            material.setDiffuseColor(getVoxelLineColor());
            material.setAmbientColor(getVoxelLineColor());
            ap.setMaterial(material);
            Shape3D shape2 = new Shape3D(gi.getGeometryArray(), ap);
            tg.addChild(shape2);
        }

        ArrayList<Double> pointsSecond = new ArrayList<Double>();
        for (Point3d point3d : point3dArrayListSecond) {
            pointsSecond.add(point3d.x);
            pointsSecond.add(point3d.y);
            pointsSecond.add(point3d.z);
        }

        gi = new GeometryInfo(GeometryInfo.QUAD_ARRAY);
        gi.setCoordinates(pointsSecond.stream().mapToDouble(i -> i).toArray());
        normalGenerator = new NormalGenerator();
        normalGenerator.generateNormals(gi);
        ap = new Appearance();
        polyAttrbutes = new PolygonAttributes();
        polyAttrbutes.setPolygonMode(PolygonAttributes.POLYGON_FILL);
        polyAttrbutes.setCullFace(PolygonAttributes.CULL_NONE);
        ap.setPolygonAttributes(polyAttrbutes);
        material = new Material();
        material.setShininess(0.5f);
        material.setAmbientColor(getVoxelColorSecond());
        material.setDiffuseColor(getVoxelColorSecond());
        ap.setMaterial(material);

        Shape3D shape1 = new Shape3D( gi.getGeometryArray(), ap );
        tg.addChild(shape1);

        ap = new Appearance();
        polyAttrbutes = new PolygonAttributes();
        polyAttrbutes.setPolygonMode(PolygonAttributes.POLYGON_LINE);
        polyAttrbutes.setCullFace(PolygonAttributes.CULL_NONE);
        ap.setPolygonAttributes(polyAttrbutes);
        material = new Material();
        material.setShininess(0.5f);
        material.setDiffuseColor(getVoxelLineColor());
        material.setAmbientColor(getVoxelLineColor());
        ap.setMaterial(material);
        Shape3D shape2 = new Shape3D(gi.getGeometryArray(), ap);
        tg.addChild(shape2);

    }

    protected void filterPoints(double gridSpaceX, double gridSpaceY, double gridSpaceZ, ArrayList<Point3d> point3dArrayList, Map<String, Point3d> hash) {
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
                if (jagged > 1) {
                    double leftBottomX = point.x - gridSpaceX*jagged*0.5f;
                    double leftBottomY = point.y - gridSpaceY*jagged*0.5f;
                    double leftBottomZ = point.z - gridSpaceZ*jagged*0.5f;

                    double deltaX = gridSpaceX*0.5f;
                    double deltaY = gridSpaceY*0.5f;
                    double deltaZ = gridSpaceZ*0.5f;

//                    Map<String, Point3d> jaggedHash = new HashMap<>();
//                    int halfJagged = jagged / 2;
                    for (int jaggedI = 0; jaggedI < jagged; jaggedI++) {
                        for (int jaggedJ = 0; jaggedJ < jagged; jaggedJ++) {
                            for (int jaggedK = 0; jaggedK < jagged; jaggedK++) {
//                                String jaggedKey = getVoxelCenterKey(jaggedI, jaggedJ, jaggedK);
//                                if (!jaggedHash.containsKey(jaggedKey)) {
//                                    jaggedHash.put(jaggedKey, new Point3d());
//                                }
                                boolean isOuterX = jaggedI == 0 || jaggedI == jagged - 1;
                                boolean isOuterY = jaggedJ == 0 || jaggedJ == jagged - 1;
                                boolean isOuterZ = jaggedK == 0 || jaggedK == jagged - 1;

                                if (isOuterX || isOuterY || isOuterZ) {
                                    double currentX = leftBottomX + deltaX + jaggedI*gridSpaceX;
                                    double currentY = leftBottomY + deltaY + jaggedJ*gridSpaceY;
                                    double currentZ = leftBottomZ + deltaZ + jaggedK*gridSpaceZ;
                                    addVoxel(currentX, currentY, currentZ, gridSpaceX, gridSpaceY, gridSpaceZ, point3dArrayList);
                                }
                            }
                        }
                    }
                } else {
                    addVoxel(point.x, point.y, point.z, gridSpaceX, gridSpaceY, gridSpaceZ, point3dArrayList);
                }
            }
        }
    }

    protected Color3f getVoxelColor() {
        return new Color3f(0.75f,0f,0.75f);
    }

    protected Color3f getVoxelColorSecond() {
        return new Color3f(0f, 0.75f, 0f);
    }


}
