package adda.application.controls;

import adda.Context;
import adda.item.tab.shape.selector.ShapeSelectorModel;
import adda.item.tab.shape.selector.params.ModelShapeParam;
import adda.utils.StringHelper;
import org.jogamp.java3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import org.jogamp.java3d.utils.behaviors.mouse.MouseRotate;
import org.jogamp.java3d.utils.behaviors.mouse.MouseWheelZoom;
import org.jogamp.java3d.utils.geometry.Text2D;
import org.jogamp.java3d.utils.universe.PlatformGeometry;
import org.jogamp.java3d.utils.universe.SimpleUniverse;

import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.geometry.GeometryInfo;
import org.jogamp.java3d.utils.geometry.NormalGenerator;


import org.jogamp.vecmath.*;
import javax.swing.*;
import org.jogamp.vecmath.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Viewer3D extends JDialog {

    String title;
    String description;
    ModelShapeParam shapeModel;
    int boxX;
    int jagged;
    double[] rectDip;

    protected JPanel wrapperPanel;
    protected JPanel canvasPanel;
    protected JTextPane descriptionPane;
    protected JCheckBox autorotationCheckBox;
    protected JCheckBox voxelizationCheckBox;
    protected JCheckBox axisCheckBox;
    protected JPanel checkBoxesPanel;

    private SimpleUniverse universe = null;
    private Canvas3D canvas = null;
    private TransformGroup viewtrans = null;

    private TransformGroup tg = null;
    private Transform3D t3d = null;
    private Transform3D t3dstep = new Transform3D();
    private Matrix4d matrix = new Matrix4d();

    private final Timer timer = new Timer(100, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            autorotate();
        }
    });
    public Viewer3D(String title, String description, ModelShapeParam shapeModel, int boxX, int jagged, double[] rectDip, Transform3D t3d) {
        this(title, description, shapeModel, boxX, jagged, rectDip);
        this.t3d = t3d;
    }

    public Viewer3D(String title, String description, ModelShapeParam shapeModel, int boxX, int jagged, double[] rectDip) {
        super(Context.getInstance().getMainFrame(), StringHelper.removeTags(title), ModalityType.APPLICATION_MODAL);
        this.title = title;
        this.boxX = boxX;
        this.description = description;
        this.shapeModel = shapeModel;
        this.jagged = jagged;
        this.rectDip = rectDip;

        initPanels();
        setLocationRelativeTo(Context.getInstance().getMainFrame());

        setMinimumSize(new Dimension(650, 650));
        setContentPane(wrapperPanel);
        setModal(true);
        final Rectangle maximumWindowBounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        int size = Math.min(maximumWindowBounds.width, maximumWindowBounds.height);
        setPreferredSize(new Dimension(size - 50, size - 50));

        timer.setRepeats(true);
        timer.start();
    }

    public Transform3D getT3d() {
        return t3d;
    }

    public void setT3d(Transform3D t3d) {
        this.t3d = t3d;
    }

    private void autorotate() {
        if (tg == null) return;
        if (t3d == null) return;
        if (canvas == null) return;
        if (autorotationCheckBox == null || !autorotationCheckBox.isSelected()) return;

        t3dstep.rotY(-Math.PI / 128);
        tg.getTransform(t3d);
        t3d.get(matrix);
        t3d.setTranslation(new Vector3d(0.0, 0.0, 0.0));
        t3d.mul(t3dstep);
        t3d.setTranslation(new Vector3d(matrix.m03, matrix.m13, matrix.m23));

        t3dstep.rotX(Math.PI / 256);
        t3d.get(matrix);
        t3d.setTranslation(new Vector3d(0.0, 0.0, 0.0));
        t3d.mul(t3dstep);
        t3d.setTranslation(new Vector3d(matrix.m03, matrix.m13, matrix.m23));
        tg.setTransform(t3d);
    }

    public void repaint3D() {
        boolean isVoxelizationEnabled = voxelizationCheckBox.isEnabled();
        voxelizationCheckBox.setEnabled(false);
        Runnable runnable = new Runnable() {
            public void run() {
                try {
                    GraphicsConfiguration config = SimpleUniverse
                            .getPreferredConfiguration();

                    canvas = new Canvas3D(config);

                    universe = new SimpleUniverse(canvas);


                    universe.getViewingPlatform().setNominalViewingTransform();

                    universe.getViewer().getView().setBackClipDistance(100.0);

                    canvas.addKeyListener(new KeyListener() {
                        @Override
                        public void keyTyped(KeyEvent e) {
                            char key = e.getKeyChar();

                            if (key == '-') {
                                //t3dstep.set(new Vector3d(0.0, 0.0, -0.1));
                                tg.getTransform(t3d);
                                t3d.setScale(t3d.getScale() - 0.2);
                                tg.setTransform(t3d);
                                return;
                            }

                            if (key == '+') {
                                //t3dstep.set(new Vector3d(0.0, 0.0, -0.1));
                                tg.getTransform(t3d);
                                t3d.setScale(t3d.getScale() + 0.2);
                                tg.setTransform(t3d);
                                return;
                            }

                            if (key == 'd') {
                                t3dstep.set(new Vector3d(0.0, 0.0, -0.1));
                                tg.getTransform(t3d);
                                t3d.mul(t3dstep);
                                tg.setTransform(t3d);
                            }

                            if (key == 's') {

                                t3dstep.rotY(Math.PI / 32);
                                tg.getTransform(t3d);
                                t3d.get(matrix);
                                t3d.setTranslation(new Vector3d(0.0, 0.0, 0.0));
                                t3d.mul(t3dstep);
                                t3d.setTranslation(new Vector3d(matrix.m03, matrix.m13, matrix.m23));
                                tg.setTransform(t3d);

                            }

                            if (key == 'f') {

                                t3dstep.rotY(-Math.PI / 32);
                                tg.getTransform(t3d);
                                t3d.get(matrix);
                                t3d.setTranslation(new Vector3d(0.0, 0.0, 0.0));
                                t3d.mul(t3dstep);
                                t3d.setTranslation(new Vector3d(matrix.m03, matrix.m13, matrix.m23));
                                tg.setTransform(t3d);

                            }

                            if (key == 'r') {

                                t3dstep.rotX(Math.PI / 32);
                                tg.getTransform(t3d);
                                t3d.get(matrix);
                                t3d.setTranslation(new Vector3d(0.0, 0.0, 0.0));
                                t3d.mul(t3dstep);
                                t3d.setTranslation(new Vector3d(matrix.m03, matrix.m13, matrix.m23));
                                tg.setTransform(t3d);

                            }

                            if (key == 'v') {

                                t3dstep.rotX(-Math.PI / 32);
                                tg.getTransform(t3d);
                                t3d.get(matrix);
                                t3d.setTranslation(new Vector3d(0.0, 0.0, 0.0));
                                t3d.mul(t3dstep);
                                t3d.setTranslation(new Vector3d(matrix.m03, matrix.m13, matrix.m23));
                                tg.setTransform(t3d);

                            }

                            if (key == 'e') {
                                t3dstep.set(new Vector3d(0.0, 0.1, 0.0));
                                tg.getTransform(t3d);
                                t3d.mul(t3dstep);
                                tg.setTransform(t3d);
                            }

                            if (key == 'c') {
                                t3dstep.set(new Vector3d(0.0, -0.1, 0.0));
                                tg.getTransform(t3d);
                                t3d.mul(t3dstep);
                                tg.setTransform(t3d);
                            }
                        }

                        @Override
                        public void keyPressed(KeyEvent e) {

                        }

                        @Override
                        public void keyReleased(KeyEvent e) {

                        }
                    });
                    BranchGroup scene = new BranchGroup();

                    BoundingSphere bounds = new BoundingSphere(new Point3d(), 10000.0);

                    viewtrans = universe.getViewingPlatform().getViewPlatformTransform();



                    BranchGroup objRoot = new BranchGroup();
                    objRoot.setCapability(BranchGroup.ALLOW_DETACH);
                    objRoot.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
                    objRoot.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
                    tg = new TransformGroup();

                    boolean isNewRotation = t3d == null;

                    if (isNewRotation) {
                        t3d = new Transform3D();
                        t3d.setTranslation(new Vector3d(-0.15, -0.3, -5.0));
                        t3d.setRotation(new AxisAngle4f(1f, 1f, 1f, 1f));
                        t3d.setScale(shapeModel.getInitialScale());
                    }

                    tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
                    tg.setTransform(t3d);


                    objRoot.addChild(tg);
                    //objRoot.addChild(createLight(new Vector3f(-0.3f, 0.2f, -1.0f)));

                    float directionalPower = 0.55f;

                    final Color3f color3f = new Color3f(directionalPower, directionalPower, directionalPower);

                    DirectionalLight directionalLight = new DirectionalLight(true, color3f, new Vector3f(-0.3f, 0.2f, -1.0f));

                    directionalLight.setInfluencingBounds(new BoundingSphere(new Point3d(), 10000.0));

                    objRoot.addChild(directionalLight);


//                        AmbientLight ambientLight = new AmbientLight(true, new Color3f(0.1f, 0.1f, 0.1f));
//
//                        ambientLight.setInfluencingBounds(new BoundingSphere(new Point3d(), 10000.0));
//
//                        objRoot.addChild(ambientLight);

                    if (axisCheckBox.isSelected()) {
                        double rectScaleX = rectDip[0];
                        double rectScaleY = rectDip[1];
                        double rectScaleZ = rectDip[2];

                        int boxY = shapeModel.getBoxY(boxX, rectScaleX, rectScaleY, rectScaleZ, jagged);
                        int boxZ = shapeModel.getBoxZ(boxX, rectScaleX, rectScaleY, rectScaleZ, jagged);

                        float factorY = (float)(1.*boxY/boxX*rectScaleY/rectScaleX);
                        float factorZ = (float)(1.*boxZ/boxX*rectScaleZ/rectScaleX);


                        addAxis(new Point3f(0.6f, 0.0f, 0.0f), "X");
                        addAxis(new Point3f(0.0f, 0.5f*factorY + 0.1f, 0.0f), "Y");
                        addAxis(new Point3f(0.0f, 0.0f, 0.5f*factorZ + 0.1f), "Z");
                    }


                    if (voxelizationCheckBox.isSelected()) {
                        shapeModel.createVoxelizedShape(tg, boxX, jagged, rectDip);
                    } else {
                        shapeModel.createSurfaceShape(tg);
                    }

                    MouseWheelZoom mouseWheelZoom = new MouseWheelZoom();
                    mouseWheelZoom.setTransformGroup(tg);
                    mouseWheelZoom.setSchedulingBounds(tg.getBounds());
                    mouseWheelZoom.setupCallback((i, transform3D) -> tg.getTransform(t3d));
//                    mouseWheelZoom.setupCallback((i, transform3D) -> {
//                        System.out.println();
//                        tg.getTransform(t3d);
//                        System.out.println(t3d.toString());
//                        System.out.println();
//                    });
                    objRoot.addChild(mouseWheelZoom);

                    MouseRotate myMouseRotate = new MouseRotate();
                    myMouseRotate.setTransformGroup(tg);
                    myMouseRotate.setSchedulingBounds(new BoundingSphere());
                    myMouseRotate.setupCallback((i, transform3D) -> tg.getTransform(t3d));
                    objRoot.addChild(myMouseRotate);


//                    KeyNavigatorBehavior keyNavBeh = new KeyNavigatorBehavior(viewtrans);
//                    keyNavBeh.setSchedulingBounds(bounds);
//                    PlatformGeometry platformGeom = new PlatformGeometry();
//                    platformGeom.addChild(keyNavBeh);
//                    universe.getViewingPlatform().setPlatformGeometry(platformGeom);


                    objRoot.compile();
                    scene.addChild(objRoot);
                    Background background = new Background();
                    background.setColor(shapeModel.getBackgroundColor());
                    background.setApplicationBounds(bounds);
                    scene.addChild(background);
                    universe.addBranchGraph(scene);

                    SwingUtilities.invokeLater(() -> {
                        canvasPanel.removeAll();
                        canvasPanel.add(canvas);
                        canvasPanel.repaint();
                        canvasPanel.revalidate();
                        canvas.requestFocus();
                        voxelizationCheckBox.setEnabled(isVoxelizationEnabled);
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    SwingUtilities.invokeLater(() -> {
                        set3dLoadedStatus("something went wrong, try again");
                        voxelizationCheckBox.setEnabled(isVoxelizationEnabled);
                    });
                }
            }
        };
        set3dLoadedStatus();
        new Thread(runnable).start();


    }

    protected void addAxis(Point3f endPoint, String title) {
        LineArray lineX = new LineArray(2, LineArray.COORDINATES);
        lineX.setCoordinate(0, new Point3f(0.0f, 0.0f, 0.0f));
        lineX.setCoordinate(0, endPoint);
        Appearance ap = new Appearance();
        final Color3f white = new Color3f(1f, 1f, 1f);
        ap.setMaterial(new Material(white,white,white,white, 0.5f));
        Shape3D line = new Shape3D(lineX, ap);
        tg.addChild(line);

        for (int i = 0; i < 2; i++) {
            Text2D axisLabel = new Text2D(title, white, "Console", 16, Font.BOLD);

            Transform3D transform3D = new Transform3D();
            transform3D.setScale(1.f);
            if (endPoint.x > 0) {
                transform3D.setTranslation(new Vector3f( endPoint.x, 0.0f, 0.0f));
            }
            if (endPoint.y > 0) {
                transform3D.setTranslation(new Vector3f( 0.0f,   endPoint.y, 0.0f));
            }
            if (endPoint.z > 0) {
                transform3D.setTranslation(new Vector3f( 0.0f, 0.0f,  endPoint.z));
            }
            transform3D.setRotation(new AxisAngle4f(0f, 1f, 0f, (float) Math.PI * i));

// Create the node for the text
            TransformGroup textTransformGroup = new TransformGroup(transform3D);
            textTransformGroup.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
            textTransformGroup.addChild(axisLabel);
            tg.addChild(textTransformGroup);
        }
    }

    protected void set3dLoadedStatus() {
        set3dLoadedStatus("3D model is loading ...");
    }

    protected void set3dLoadedStatus(String message) {
        canvasPanel.removeAll();
        final JLabel label = new JLabel(message);
        label.setAlignmentY(Component.CENTER_ALIGNMENT);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        canvasPanel.add(label);
        canvasPanel.repaint();
        canvasPanel.revalidate();
    }

    private void initPanels() {
        wrapperPanel = new JPanel(new BorderLayout());
        canvasPanel = new JPanel(new BorderLayout());
        canvasPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        canvasPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        canvasPanel.setPreferredSize(new Dimension(400, 400));

        JPanel leftPanel = new JPanel(new BorderLayout());
//        leftPanel.setBackground(Color.white);
        //leftPanel.setMaximumSize(new Dimension(250, 9999));
//        leftPanel.setMinimumSize(new Dimension(250, 400));
        leftPanel.setLayout(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JScrollPane treeScroll = new JScrollPane(leftPanel);
        treeScroll.setPreferredSize(new Dimension(150, 700));
        treeScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        //treeScroll.setMaximumSize(new Dimension(250, 9999));
//        treeScroll.setMinimumSize(new Dimension(250, 400));
        treeScroll.getVerticalScrollBar().setUnitIncrement(16);
        wrapperPanel.add(treeScroll, BorderLayout.WEST);
        //treeScroll.setViewportView(leftPanel);



        descriptionPane = new JTextPane();
        descriptionPane.setEditable(false);

        descriptionPane.setMaximumSize(new Dimension(200, 9999));
//        descriptionPane.setMinimumSize(new Dimension(150, 400));
        descriptionPane.setPreferredSize(new Dimension(150, 700));
        descriptionPane.setContentType("text/html");
        descriptionPane.setText(description);
        leftPanel.add(descriptionPane);

        autorotationCheckBox = new JCheckBox();
        autorotationCheckBox.setText("autorotate");
        autorotationCheckBox.setName(ShapeSelectorModel.AUTOROTATE_FIELD_NAME);

        voxelizationCheckBox = new JCheckBox();
        voxelizationCheckBox.setText("voxelization");
        voxelizationCheckBox.setName(ShapeSelectorModel.VOXELIZATION_FIELD_NAME);
        voxelizationCheckBox.addActionListener(e -> repaint3D());



        axisCheckBox = new JCheckBox();
        axisCheckBox.setText("show axes");
        axisCheckBox.setSelected(true);
        axisCheckBox.addActionListener(e -> repaint3D());

        checkBoxesPanel = new JPanel();
        checkBoxesPanel.setLayout(new BorderLayout());
        checkBoxesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        checkBoxesPanel.add(autorotationCheckBox, BorderLayout.WEST);
        checkBoxesPanel.add(voxelizationCheckBox);
        checkBoxesPanel.add(axisCheckBox, BorderLayout.EAST);

        JPanel wrapperRight = new JPanel(new BorderLayout());

        wrapperRight.add(checkBoxesPanel, BorderLayout.NORTH);
        wrapperRight.add(canvasPanel);

        wrapperPanel.add(wrapperRight);
    }

    public void setVoxelizationEnabled(boolean enabled) {
        if (enabled) {
            voxelizationCheckBox.setEnabled(true);
        } else {
            voxelizationCheckBox.setEnabled(false);
            voxelizationCheckBox.setSelected(false);
        }
    }

    public void setVoxelization(boolean enabled) {
        if (voxelizationCheckBox.isEnabled()) {
            voxelizationCheckBox.setSelected(enabled);
        }
    }

    public void setAutorotate(boolean enabled) {
        if (autorotationCheckBox.isEnabled()) {
            autorotationCheckBox.setSelected(enabled);
        }
    }


}
