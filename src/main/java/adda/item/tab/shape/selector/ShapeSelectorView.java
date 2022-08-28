package adda.item.tab.shape.selector;

import adda.Context;
import adda.application.controls.JScaledImageLabel;
import adda.base.events.IModelPropertyChangeEvent;
import adda.base.models.IModel;
import adda.base.views.ViewBase;
import adda.item.tab.base.dplGrid.DplGridEnum;
import adda.item.tab.base.dplGrid.DplGridModel;
import adda.item.tab.base.lambda.LambdaModel;
import adda.item.tab.base.refractiveIndexAggregator.RefractiveIndexAggregatorModel;
import adda.item.tab.base.size.SizeModel;
import adda.item.tab.internals.jagged.JaggedModel;
import adda.item.tab.shape.dipoleShape.DipoleShapeEnum;
import adda.item.tab.shape.dipoleShape.DipoleShapeModel;
import adda.item.tab.shape.selector.params.ModelShapeParam;
import com.sun.j3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseWheelZoom;
import com.sun.j3d.utils.universe.PlatformGeometry;
import com.sun.j3d.utils.universe.SimpleUniverse;

import javax.help.CSH;
import javax.media.j3d.*;
import javax.swing.*;
import javax.vecmath.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class ShapeSelectorView extends ViewBase {

    private String currentShapeID = null;
    private SimpleUniverse universe = null;
    private Canvas3D canvas = null;
    private TransformGroup viewtrans = null;

    private TransformGroup tg = null;
    private Transform3D t3d = null;
    private Transform3D t3dstep = new Transform3D();
    private Matrix4d matrix = new Matrix4d();

    JLabel pictureLabel = new JScaledImageLabel();
    //todo copypaste from DialogView. create AdditionalPanelView and inherite from it dialog view and this
    protected JPanel outerPanel;
    protected JPanel additionalPanel;
    protected JPanel canvasPanel;

    protected JCheckBox autorotationCheckBox;
    protected JCheckBox voxelizationCheckBox;
    protected JButton separate3dSceneButton;


    protected volatile boolean isBusy;
    protected volatile ShapeSelectorModel modelToProcess;
    protected volatile int counter = 0;

    private final Timer timer = new Timer(100, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            autorotate();
            if (counter > 50) {
                counter = 0;
                isBusy = false;
                return;
            }
            if (isBusy) {
                counter = counter + 1;
                return;
            }
            if (modelToProcess == null) {
                return;
            }
            counter = 0;
            isBusy = true;
            ShapeSelectorModel currentModelToProcess = modelToProcess;
            modelToProcess = null;
            if (currentModelToProcess != null && currentModelToProcess.getParamsBox() != null && currentModelToProcess.getParamsBox().getModel() != null) {
                currentShapeID = getShapeID(currentModelToProcess);
                init3dCanvas((ModelShapeParam) currentModelToProcess.getParamsBox().getModel());
            }

        }
    });

    private void autorotate() {
        if (tg == null) return;
        if (t3d == null) return;
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

    @Override
    public JComponent getRootComponent() {
        return this.outerPanel;
    }

    @Override
    public void initPanel() {
        super.initPanel();
        JPanel outerPanel = new JPanel(new BorderLayout());
//        JPanel outerPanel = new JPanel(new GridBagLayout());

        outerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        outerPanel.setAlignmentY(Component.TOP_ALIGNMENT);

        JPanel additionalPanel = new JPanel();
        additionalPanel.setLayout(new BorderLayout());
        additionalPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        additionalPanel.setAlignmentY(Component.TOP_ALIGNMENT);
        additionalPanel.setPreferredSize(new Dimension(140, 155));
        additionalPanel.setMaximumSize(new Dimension(140, 155));

        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrapper.setAlignmentY(Component.TOP_ALIGNMENT);
        wrapper.add(Box.createVerticalGlue());
        wrapper.add(panel);
        wrapper.add(additionalPanel);

        this.additionalPanel = additionalPanel;
        this.outerPanel = outerPanel;

//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.gridx = 0;
//        gbc.gridy = 0;
//        gbc.weightx = 1.0;
//        gbc.weighty = 1.0;
//        gbc.fill = GridBagConstraints.BOTH;

        this.outerPanel.add(wrapper, BorderLayout.WEST);

//        gbc = new GridBagConstraints();
//        gbc.gridx = 1;
//        gbc.gridy = 0;
//        gbc.weightx = 1.0;
//        gbc.weighty = 1.0;
//        gbc.fill = GridBagConstraints.BOTH;

        JPanel canvasWrapper = new JPanel(new BorderLayout());
        this.outerPanel.add(canvasWrapper);

//        autorotationCheckBox = new JCheckBox();
//        autorotationCheckBox.setText("autorotate");
//        canvasWrapper.add(autorotationCheckBox, BorderLayout.NORTH);
//



        canvasPanel = new JPanel();
        canvasPanel.setLayout(new BorderLayout());
//        canvasPanel.setAlignmentY(Component.TOP_ALIGNMENT);
//
//        canvasPanel.setAlignmentX(Component.CENTER_ALIGNMENT);


        canvasPanel.setPreferredSize(new Dimension(170, 170));
        canvasPanel.setMinimumSize(new Dimension(170, 170));

        canvasWrapper.add(canvasPanel);

        autorotationCheckBox = new JCheckBox();
        autorotationCheckBox.setText("autorotate");
        autorotationCheckBox.setName(ShapeSelectorModel.AUTOROTATE_FIELD_NAME);

        voxelizationCheckBox = new JCheckBox();
        voxelizationCheckBox.setText("voxelization");
        voxelizationCheckBox.setName(ShapeSelectorModel.VOXELIZATION_FIELD_NAME);

        JPanel checkBoxesPanel = new JPanel();
        checkBoxesPanel.setLayout(new BorderLayout());
        checkBoxesPanel.add(autorotationCheckBox, BorderLayout.WEST);
        checkBoxesPanel.add(voxelizationCheckBox, BorderLayout.EAST);


        canvasWrapper.add(checkBoxesPanel, BorderLayout.NORTH);

        JButton button = new JButton("<HTML><FONT color=\"#000099\"><U>open 3D in new window</U></FONT></HTML>");
        //button.addActionListener(Context.getInstance().getHelpActionListener());
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setFocusable(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setHorizontalAlignment(JButton.LEFT);
        canvasWrapper.add(button, BorderLayout.SOUTH);
        set3dLoadedStatus("initialization ...");
        //this.outerPanel.add(wrapper);

    }

    @Override
    protected void initFromModelInner(IModel model) {
        super.initFromModelInner(model);

        if (components.containsKey(ShapeSelectorModel.ENUM_VALUE_FIELD_NAME)) {
            final Component component = components.get(ShapeSelectorModel.ENUM_VALUE_FIELD_NAME);
            component.setPreferredSize(new Dimension(120, 20));
            setHelpTooltip(model, (JComponent) component);
        }
        ShapeSelectorModel shapeSelectorModel = (ShapeSelectorModel) model;
        autorotationCheckBox.setSelected(shapeSelectorModel.isAutorotate());
        voxelizationCheckBox.setSelected(shapeSelectorModel.isVoxelization());

//        pictureLabel.setAlignmentY(Component.TOP_ALIGNMENT);
//        pictureLabel.setVerticalAlignment(JLabel.TOP);
//        pictureLabel.setVerticalTextPosition(JLabel.TOP);
//        pictureLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//        pictureLabel.setHorizontalAlignment(JLabel.CENTER);
//        pictureLabel.setHorizontalTextPosition(JLabel.CENTER);
////        pictureLabel.setBorder(BorderFactory.createLineBorder(Color.black));
//        additionalPanel.setAlignmentY(Component.TOP_ALIGNMENT);
//        pictureLabel.setPreferredSize(new Dimension(140, 165));
//        pictureLabel.setMaximumSize(new Dimension(140, 165));
////        pictureLabel.setPreferredSize(new Dimension(230, 230));

//        pictureLabel.setIcon(getImageIcon(shapeSelectorModel.getEnumValue().toString()));


//
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.gridx = 1;
//        gbc.gridy = 0;
//        gbc.weightx = 1.0;
//        gbc.weighty = 1.0;
//        gbc.fill = GridBagConstraints.BOTH;
//        outerPanel.add(pictureLabel, gbc);
//
//        //outerPanel.add(pictureLabel);
        if (shapeSelectorModel.getParamsBox() != null) {
            if (shapeSelectorModel.getParamsBox().getLayout() != null) {
                additionalPanel.add(shapeSelectorModel.getParamsBox().getLayout());
                additionalPanel.repaint();
                additionalPanel.revalidate();
            }
            SwingUtilities.invokeLater(() -> {
                if (!getShapeID(shapeSelectorModel).equals(currentShapeID) && shapeSelectorModel.getParamsBox().getModel() != null) {
                    currentShapeID = getShapeID(shapeSelectorModel);
                    init3dCanvas((ModelShapeParam) shapeSelectorModel.getParamsBox().getModel());
                }
            });

        }
        timer.setRepeats(true);
        timer.start();
    }

    protected void init3dCanvas(ModelShapeParam shapeModel) {
        if (shapeModel.validate()) {
            set3dLoadedStatus();
            init3dCanvasInner(shapeModel);
        } else {
            set3dLoadedStatus("Invalid parameters");
        }

    }

    protected void init3dCanvasInner(final ModelShapeParam shapeModel) {
        Runnable runnable = new Runnable() {
            public void run() {
                try {
                    GraphicsConfiguration config = SimpleUniverse
                            .getPreferredConfiguration();

                    canvas = new Canvas3D(config);

                    universe = new SimpleUniverse(canvas);


                    universe.getViewingPlatform().setNominalViewingTransform();

                    universe.getViewer().getView().setBackClipDistance(10.0);

                    canvas.addKeyListener(new KeyListener() {
                        @Override
                        public void keyTyped(KeyEvent e) {
                            char key = e.getKeyChar();

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

                    KeyNavigatorBehavior keyNavBeh = new KeyNavigatorBehavior(viewtrans);
                    keyNavBeh.setSchedulingBounds(bounds);
                    PlatformGeometry platformGeom = new PlatformGeometry();
                    platformGeom.addChild(keyNavBeh);
                    universe.getViewingPlatform().setPlatformGeometry(platformGeom);

                    BranchGroup objRoot = new BranchGroup();
                    objRoot.setCapability(BranchGroup.ALLOW_DETACH);
                    objRoot.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
                    objRoot.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
                    tg = new TransformGroup();
                    t3d = new Transform3D();

                    tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

                    t3d.setTranslation(new Vector3d(-0.15, -0.3, -5.0));
                    t3d.setRotation(new AxisAngle4f(1f, 1f, 1f, 1f));
                    t3d.setScale(4.5);

                    tg.setTransform(t3d);


                    objRoot.addChild(tg);
                    //objRoot.addChild(createLight(new Vector3f(-0.3f, 0.2f, -1.0f)));

                    float directionalPower = 0.55f;

                    final Color3f color3f = new Color3f(directionalPower, directionalPower, directionalPower);

                    DirectionalLight directionalLight = new DirectionalLight(true, color3f, new Vector3f(-0.3f, 0.2f, -1.0f));

                    directionalLight.setInfluencingBounds(new BoundingSphere(new Point3d(), 10000.0));

                    objRoot.addChild(directionalLight);


                    AmbientLight ambientLight = new AmbientLight(true, new Color3f(1f - directionalPower, 1f - directionalPower, 1f - directionalPower));

                    ambientLight.setInfluencingBounds(new BoundingSphere(new Point3d(), 10000.0));

                    objRoot.addChild(ambientLight);

                    LineArray lineX = new LineArray(2, LineArray.COORDINATES);
//      lineX.setCoordinate(0, new Point3f(0.0f, 1.0f, 0.0f));
//      lineX.setCoordinate(1, new Point3f(0.0f, 0.0f, 0.0f));

                    lineX.setCoordinate(0, new Point3f(1.0f, 0.0f, 0.0f));
                    lineX.setCoordinate(1, new Point3f(0.0f, 0.0f, 0.0f));
                    Shape3D line = new Shape3D(lineX);
                    tg.addChild(line);

                    MouseRotate myMouseRotate = new MouseRotate();
                    myMouseRotate.setTransformGroup(tg);
                    myMouseRotate.setSchedulingBounds(new BoundingSphere());
                    objRoot.addChild(myMouseRotate);

                    MouseWheelZoom mouseWheelZoom = new MouseWheelZoom();
                    mouseWheelZoom.setTransformGroup(tg);
                    mouseWheelZoom.setSchedulingBounds(tg.getBounds());
//                mouseWheelZoom.setupCallback((i, transform3D) -> {
//                    System.out.println();
//                    tg.getTransform(t3d);
//                    System.out.println(t3d.toString());
//                    System.out.println();
//                });
                    objRoot.addChild(mouseWheelZoom);


                    if (voxelizationCheckBox.isSelected()) {
                        DipoleShapeModel dipoleShapeModel = (DipoleShapeModel) Context.getInstance().getChildModelFromSelectedBox(DipoleShapeModel.class);
                        DplGridModel dplGridModel  = (DplGridModel) Context.getInstance().getChildModelFromSelectedBox(DplGridModel.class);
                        JaggedModel jaggedModel  = (JaggedModel) Context.getInstance().getChildModelFromSelectedBox(JaggedModel.class);
                        SizeModel sizeModel  = (SizeModel) Context.getInstance().getChildModelFromSelectedBox(SizeModel.class);
                        LambdaModel lambdaModel  = (LambdaModel) Context.getInstance().getChildModelFromSelectedBox(LambdaModel.class);

                        if (lambdaModel.getLambda() <= 0 || sizeModel.getValue() <= 0 || dplGridModel.getValue() <= 0) {
                            set3dLoadedStatus("Invalid parameters");
                            if (Context.getInstance().getLastParamsComponent() != null) {
                                Context.getInstance().getLastParamsComponent().requestFocus();
                            }
                            return;
                        }

                        int jagged = jaggedModel.getFlag() ? jaggedModel.getJagged() : 1;
                        double sizeX = sizeModel.getValue();
                        double drelX, dMax;
                        double[] rectDip;
                        if (dipoleShapeModel.getEnumValue() == DipoleShapeEnum.Rect) {
                            dMax = Math.max(dipoleShapeModel.getScaleX(), dipoleShapeModel.getScaleY());
                            dMax = Math.max(dMax, dipoleShapeModel.getScaleZ());
                            drelX = dipoleShapeModel.getScaleX()/ dMax;
                            rectDip = new double[] {dipoleShapeModel.getScaleX(), dipoleShapeModel.getScaleY(), dipoleShapeModel.getScaleZ()};
                        } else {
                            dMax = 1;
                            drelX = 1;
                            rectDip = new double[] {1, 1, 1};
                        }


                        int minBoxX = 16;
                        int boxX;
                        if (dplGridModel.getEnumValue() == DplGridEnum.dpl) {
                            boxX=(int)Math.ceil((sizeX/drelX)*dplGridModel.getValue()/lambdaModel.getLambda());
                        } else {
                            boxX = (int) dplGridModel.getValue();
                        }

                        boxX=ModelShapeParam.fitBox(boxX, jagged);
                        boxX = Math.max(boxX, minBoxX);
                        shapeModel.createVoxelizedShape(tg, boxX, jagged, rectDip);
                    } else {
                        shapeModel.createSurfaceShape(tg);
                    }


                    objRoot.compile();

                    scene.addChild(objRoot);

                    Background background = new Background();
                    background.setColor(0.75f, 0.69f, 0.680f);
                    background.setApplicationBounds(bounds);
                    scene.addChild(background);
                    universe.addBranchGraph(scene);

                    SwingUtilities.invokeLater(() -> {
                        canvasPanel.removeAll();
                        canvasPanel.add(canvas);
                        canvasPanel.repaint();
                        canvasPanel.revalidate();

                        if (Context.getInstance().getLastParamsComponent() != null) {
                            Context.getInstance().getLastParamsComponent().requestFocus();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    SwingUtilities.invokeLater(() -> {
                        set3dLoadedStatus("something went wrong, try again");
                    });
                } finally {
                    isBusy = false;
                }
            }
        };
        //new Thread(runnable).start();



//        try {
//            SwingUtilities.invokeLater(runnable);
            runnable.run();
//        } catch (Exception e) {
//            set3dLoadedStatus("something went wrong, try again");
//        }

        //canvasPanel.repaint();
    }

    protected void set3dLoadedStatus() {
        set3dLoadedStatus("3D model is loading ...");
    }

    protected void set3dLoadedStatus(String message) {
        canvasPanel.removeAll();
        canvasPanel.add(new JLabel(message));
        canvasPanel.repaint();
        canvasPanel.revalidate();
    }

    @Override
    public void modelPropertyChanged(IModel sender, IModelPropertyChangeEvent event) {
        super.modelPropertyChanged(sender, event);
        if (ShapeSelectorModel.AUTOROTATE_FIELD_NAME.equals(event.getPropertyName())) {
            autorotationCheckBox.setSelected((boolean) event.getPropertyValue());
        }
        if (ShapeSelectorModel.VOXELIZATION_FIELD_NAME.equals(event.getPropertyName())) {
            voxelizationCheckBox.setSelected((boolean) event.getPropertyValue());
            modelToProcess = (ShapeSelectorModel) sender;
            set3dLoadedStatus();
        }

        if (ShapeSelectorModel.EXTERNAL_CHANGES.equals(event.getPropertyName()) && voxelizationCheckBox.isSelected()) {
            modelToProcess = (ShapeSelectorModel) sender;
            set3dLoadedStatus();
        }


        if (event.getPropertyValue() instanceof ShapeSelectorEnum) {
            //pictureLabel.setIcon(getImageIcon(event.getPropertyValue().toString()));
            ShapeSelectorModel model = (ShapeSelectorModel) sender;
            if (model.getParamsBox() == null) {
                if (additionalPanel.getComponents().length > 0) {
                    additionalPanel.removeAll();
                    additionalPanel.repaint();
                    additionalPanel.revalidate();
                    additionalPanel.add(new JPanel());
                }
            } else {
                if (additionalPanel.getComponents().length == 1) {
                    if (!additionalPanel.getComponent(0).equals(model.getParamsBox().getLayout())) {
                        additionalPanel.removeAll();
                        additionalPanel.repaint();
                        additionalPanel.revalidate();
                        additionalPanel.add(model.getParamsBox().getLayout());
                        additionalPanel.repaint();
                        additionalPanel.revalidate();
                    }
                } else {
                    if (additionalPanel.getComponents().length > 1) {
                        additionalPanel.removeAll();
                        additionalPanel.repaint();
                        additionalPanel.revalidate();
                    }
                    additionalPanel.add(model.getParamsBox().getLayout());
                    additionalPanel.repaint();
                    additionalPanel.revalidate();
                }

                if (!getShapeID(model).equals(currentShapeID) && model.getParamsBox().getModel() != null) {
                    //currentShapeID = getShapeID(model);
                    //System.out.println(currentShapeID);
                    modelToProcess = model;
                    set3dLoadedStatus();
                    //init3dCanvas((ModelShapeParam) model.getParamsBox().getModel());
                }
            }
        }
    }

    private String getShapeID(ShapeSelectorModel shapeSelectorModel) {
        return shapeSelectorModel.getEnumValue().toString()
                + (shapeSelectorModel.isVoxelization() ? "voxel" : "math")
                + ((shapeSelectorModel.getParamsBox() == null || shapeSelectorModel.getParamsBox().getModel() == null) ? "" : String.join("", ((ModelShapeParam) shapeSelectorModel.getParamsBox().getModel()).getParamsList()));
    }

    protected static ImageIcon getImageIcon(String imageName) {
        String path = "image/shape/" + imageName + ".png";

        java.net.URL imgURL = ShapeSelectorView.class.getClassLoader().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            return null;
        }
    }

    @Override
    protected void initLabel(IModel model) {

    }


}