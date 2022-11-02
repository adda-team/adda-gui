package adda.item.tab.shape.selector;

import adda.Context;
import adda.application.controls.JScaledImageLabel;
import adda.application.controls.Viewer3D;
import adda.base.IAddaOptionsContainer;
import adda.base.events.IModelPropertyChangeEvent;
import adda.base.models.IModel;
import adda.base.views.ViewBase;
import adda.help.HelpProvider;
import adda.item.tab.base.dplGrid.DplGridModel;
import adda.item.tab.base.lambda.LambdaModel;
import adda.item.tab.base.size.SizeEnum;
import adda.item.tab.base.size.SizeModel;
import adda.item.tab.internals.jagged.JaggedModel;
import adda.item.tab.shape.dipoleShape.DipoleShapeEnum;
import adda.item.tab.shape.dipoleShape.DipoleShapeModel;
import adda.item.tab.shape.selector.params.ModelShapeParam;
import adda.utils.StringHelper;
import net.java.balloontip.BalloonTip;
import net.java.balloontip.positioners.BalloonTipPositioner;
import net.java.balloontip.styles.RoundedBalloonStyle;
import net.java.balloontip.utils.FadingUtils;
import net.java.balloontip.utils.TimingUtils;
import net.java.balloontip.utils.ToolTipUtils;
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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class ShapeSelectorView extends ViewBase {

    private String currentShapeID = null;
    private SimpleUniverse universe = null;
    private Canvas3D canvas = null;
    private TransformGroup viewtrans = null;

    private TransformGroup tg = null;
    private Transform3D t3d = new Transform3D();
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

    JButton buttonOpen3D;


    protected volatile boolean isBusy;
    protected volatile boolean isNewt3d = true;
    protected volatile ShapeSelectorModel modelToProcess;
    protected volatile int counter = 0;

    private final Timer timer = new Timer(1000, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (counter > 5) {
                counter = 0;
                isBusy = false;
                return;
            }
            if (isBusy) {
                counter++;
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
                if (!getShapeID(currentModelToProcess).equals(currentShapeID)) {
                    currentShapeID = getShapeID(currentModelToProcess);
                    init3dCanvas((ModelShapeParam) currentModelToProcess.getParamsBox().getModel());
                }
            }

        }
    });

    private final Timer rotationtimer = new Timer(100, (e) -> autorotate());

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

        buttonOpen3D = new JButton("<HTML><FONT color=\"#000099\"><U>open 3D in new window</U></FONT></HTML>");
        //button.addActionListener(Context.getInstance().getHelpActionListener());
        buttonOpen3D.setBorderPainted(false);
        buttonOpen3D.setFocusPainted(false);
        buttonOpen3D.setOpaque(false);
        buttonOpen3D.setContentAreaFilled(false);
        buttonOpen3D.setFocusable(false);
        buttonOpen3D.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        buttonOpen3D.setHorizontalAlignment(JButton.LEFT);

        canvasWrapper.add(buttonOpen3D, BorderLayout.SOUTH);
        set3dLoadedStatus("initialization ...");
        //this.outerPanel.add(wrapper);

    }
    ShapeSelectorModel shapeSelectorModel;
    @Override
    protected void initFromModelInner(IModel model) {
        super.initFromModelInner(model);

        if (components.containsKey(ShapeSelectorModel.ENUM_VALUE_FIELD_NAME)) {
            final Component component = components.get(ShapeSelectorModel.ENUM_VALUE_FIELD_NAME);
            component.setPreferredSize(new Dimension(120, 20));
            setHelpTooltip(model, (JComponent) component);
        }
        shapeSelectorModel = (ShapeSelectorModel) model;
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
//            SwingUtilities.invokeLater(() -> {
//                if (!getShapeID(shapeSelectorModel).equals(currentShapeID) && shapeSelectorModel.getParamsBox().getModel() != null) {
//                    currentShapeID = getShapeID(shapeSelectorModel);
//                    init3dCanvas((ModelShapeParam) shapeSelectorModel.getParamsBox().getModel());
//                }

//            });
            modelToProcess = shapeSelectorModel;

        }

        buttonOpen3D.addActionListener((e) -> {
            SizeModel sizeModel = (SizeModel) Context.getInstance().getChildModelFromSelectedBox(SizeModel.class);
            int jagged = getJagged();
            double[] rectDip = getRectDip();
            int boxX = ModelShapeParam.getActiveProjectGridSizeAlongXAxis();
            int dialogResult = JOptionPane.YES_OPTION;
            if (sizeModel.getType() == SizeEnum.EqRadius) {
                JOptionPane.showMessageDialog(Context.getInstance().getMainFrame(), "<html>the voxelization view available only for<br><b>'Size along X axis'</b> </html>","3D view cannot be opened",  JOptionPane.WARNING_MESSAGE);
                //return;
            } else {
                if (boxX <= 0) {
                    JOptionPane.showMessageDialog(Context.getInstance().getMainFrame(), "invalid parameters ","3D view cannot be opened",  JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (boxX > 150) {
                    dialogResult = JOptionPane.showConfirmDialog (Context.getInstance().getMainFrame(), "<html>estimated count of voxels are <br>more than <b>10^6</b> <br> 3D model could be freeze your PC.<br> Do you want to proceed?</html>","Warning", JOptionPane.YES_NO_OPTION);
                }
            }




            if(dialogResult == JOptionPane.YES_OPTION){
                //set3dLoadedStatus();
                ModelShapeParam modelShapeParam = (ModelShapeParam) shapeSelectorModel.getParamsBox().getModel();
                String title = getViewerTitle();
                String desc = getViewerDesc();
                Viewer3D viewer3D = new Viewer3D(title, desc, modelShapeParam, boxX, jagged, rectDip);

                viewer3D.setVoxelizationEnabled(sizeModel.getType() != SizeEnum.EqRadius);
                viewer3D.setVoxelization(voxelizationCheckBox.isSelected());
                viewer3D.setAutorotate(autorotationCheckBox.isSelected());
                viewer3D.setT3d(new Transform3D(t3d));
                viewer3D.addWindowFocusListener(new WindowAdapter() {
                    @Override
                    public void windowLostFocus(WindowEvent e) {
                        tg.setTransform(viewer3D.getT3d());
                    }
                });
                viewer3D.repaint3D();
                viewer3D.pack();
                viewer3D.setLocationRelativeTo(null);

                viewer3D.setVisible(true);



            }
        });
        timer.setRepeats(true);
        timer.start();

        rotationtimer.setRepeats(true);
        rotationtimer.start();


    }

    private String getViewerTitle() {
        String description = shapeSelectorModel.getDescription();
        if (StringHelper.isEmpty(description)) {
            description = "sphere";
        }
        return shapeSelectorModel.getLabel() + ": " + description ;
    }

    private String getViewerDesc() {
        StringBuilder descriptionBuilder = new StringBuilder();

        descriptionBuilder.append("<html>");
        descriptionBuilder.append("<b>");
        descriptionBuilder.append(getViewerTitle().replaceAll("<html>","").replaceAll("</html>",""));
        descriptionBuilder.append("</b>");
        descriptionBuilder.append("<br>");
        descriptionBuilder.append("<br>");

        descriptionBuilder.append(HelpProvider.getShortDescByClass(shapeSelectorModel.getParamsBox().getModel().getClass()));
        descriptionBuilder.append("<br>");
        descriptionBuilder.append("<br>");

        DipoleShapeModel dipoleShapeModel = (DipoleShapeModel) Context.getInstance().getChildModelFromSelectedBox(DipoleShapeModel.class);
        DplGridModel dplGridModel  = (DplGridModel) Context.getInstance().getChildModelFromSelectedBox(DplGridModel.class);
        JaggedModel jaggedModel  = (JaggedModel) Context.getInstance().getChildModelFromSelectedBox(JaggedModel.class);
        SizeModel sizeModel  = (SizeModel) Context.getInstance().getChildModelFromSelectedBox(SizeModel.class);
        LambdaModel lambdaModel  = (LambdaModel) Context.getInstance().getChildModelFromSelectedBox(LambdaModel.class);

        IAddaOptionsContainer[] array = new IAddaOptionsContainer[]{dipoleShapeModel, dplGridModel, jaggedModel, sizeModel, lambdaModel};

        for (IAddaOptionsContainer container: array) {
            container.getAddaOptions();
            descriptionBuilder.append("<b>");
            descriptionBuilder.append(container.getLabel());
            descriptionBuilder.append("</b>:  ");
            String description = container.getDescription();
            if (StringHelper.isEmpty(description)) {
                if (container instanceof JaggedModel) description = "1";
                if (container instanceof LambdaModel) description = "2*PI";
                if (container instanceof DipoleShapeModel) description = "cubic";
            } else {
                if (container instanceof DipoleShapeModel) description = "rectangular " + description;
            }

            descriptionBuilder.append(description.replaceAll("<html>","").replaceAll("</html>", ""));
            descriptionBuilder.append("<br>");
            descriptionBuilder.append("<br>");
        }

        descriptionBuilder.append("<b>");
        descriptionBuilder.append("Grid");
        descriptionBuilder.append("</b>: ");
        descriptionBuilder.append(getViewerGrid());
        descriptionBuilder.append("<br>");
        descriptionBuilder.append("<br>");
        descriptionBuilder.append("<br>");
        descriptionBuilder.append("<br>");
        descriptionBuilder.append("<br>");
        descriptionBuilder.append("<i>");
        descriptionBuilder.append("Navigation:");
        descriptionBuilder.append("</i>");
        descriptionBuilder.append("<br>");
        descriptionBuilder.append("<br>");
        descriptionBuilder.append("<b>");
        descriptionBuilder.append("rotation:");
        descriptionBuilder.append("</b> ");
        descriptionBuilder.append("<br>use a mouse or <br>keys 'v', 'r', 's' or 'f'");
        descriptionBuilder.append("<br>");
        descriptionBuilder.append("<br>");
        descriptionBuilder.append("<b>");
        descriptionBuilder.append("zoom:");
        descriptionBuilder.append("</b> ");
        descriptionBuilder.append("<br>use a mouse wheel or <br>keys '+' or '-'");
        descriptionBuilder.append("<br>");
        descriptionBuilder.append("</html>");

        return descriptionBuilder.toString();
    }

    private String getViewerGrid() {
        int jagged = getJagged();
        double[] rectDip = getRectDip();

        int boxX = ModelShapeParam.getActiveProjectGridSizeAlongXAxis();
        ModelShapeParam modelShapeParam = (ModelShapeParam) shapeSelectorModel.getParamsBox().getModel();


        return boxX + "x" + modelShapeParam.getBoxY(boxX, rectDip[0], rectDip[1], rectDip[2], jagged) + "x" + modelShapeParam.getBoxZ(boxX, rectDip[0], rectDip[1], rectDip[2], jagged) ;
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
        SwingUtilities.invokeLater(() -> voxelizationCheckBox.setEnabled(false));
        Runnable runnable = new Runnable() {
            public void run() {
                try {
                    isBusy = true;
                    GraphicsConfiguration config = SimpleUniverse
                            .getPreferredConfiguration();


                    canvas = new Canvas3D(config);
                    if (universe != null) {
                        universe.removeAllLocales();
                        universe.cleanup();
                    }
                    universe = new SimpleUniverse(canvas);


                    universe.getViewingPlatform().setNominalViewingTransform();

                    universe.getViewer().getView().setBackClipDistance(100.0);

                    BranchGroup scene = new BranchGroup();

                    BoundingSphere bounds = new BoundingSphere(new Point3d(), 10000.0);

                    viewtrans = universe.getViewingPlatform().getViewPlatformTransform();

//                    KeyNavigatorBehavior keyNavBeh = new KeyNavigatorBehavior(viewtrans);
//                    keyNavBeh.setSchedulingBounds(bounds);
//                    PlatformGeometry platformGeom = new PlatformGeometry();
//                    platformGeom.addChild(keyNavBeh);
//                    universe.getViewingPlatform().setPlatformGeometry(platformGeom);

                    BranchGroup objRoot = new BranchGroup();
                    objRoot.setCapability(BranchGroup.ALLOW_DETACH);
                    objRoot.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
                    objRoot.setCapability(BranchGroup.ALLOW_CHILDREN_READ);

                    tg = new TransformGroup();

                    t3d = isNewt3d ? new Transform3D() : t3d;

                    tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);


                    if (isNewt3d) {
                        t3d.setTranslation(new Vector3d(-0.15, -0.3, -5.0));
                        t3d.setRotation(new AxisAngle4f(1f, 1f, 1f, 1f));
                        t3d.setScale(shapeModel.getInitialScale());
                        isNewt3d = false;
                    }

                    tg.setTransform(t3d);


                    objRoot.addChild(tg);
                    //objRoot.addChild(createLight(new Vector3f(-0.3f, 0.2f, -1.0f)));

                    float directionalPower = 0.55f;

                    final Color3f color3f = new Color3f(directionalPower, directionalPower, directionalPower);

                    DirectionalLight directionalLight = new DirectionalLight(true, color3f, new Vector3f(-0.3f, 0.2f, -1.0f));

                    directionalLight.setInfluencingBounds(new BoundingSphere(new Point3d(), 10000.0));

                    objRoot.addChild(directionalLight);


                        AmbientLight ambientLight = new AmbientLight(true, new Color3f(0.05f, 0.05f, 0.05f));

                        ambientLight.setInfluencingBounds(new BoundingSphere(new Point3d(), 10000.0));

                        objRoot.addChild(ambientLight);

                    LineArray lineX = new LineArray(2, LineArray.COORDINATES);
//      lineX.setCoordinate(0, new Point3f(0.0f, 1.0f, 0.0f));
//      lineX.setCoordinate(1, new Point3f(0.0f, 0.0f, 0.0f));

                    lineX.setCoordinate(0, new Point3f(1.0f, 0.0f, 0.0f));
                    lineX.setCoordinate(1, new Point3f(0.0f, 0.0f, 0.0f));
                    Shape3D line = new Shape3D(lineX);
                    //tg.addChild(line);

//                    LineArray lineY = new LineArray(2, LineArray.COORDINATES);
////      lineX.setCoordinate(0, new Point3f(0.0f, 1.0f, 0.0f));
////      lineX.setCoordinate(1, new Point3f(0.0f, 0.0f, 0.0f));
//
//                    lineY.setCoordinate(0, new Point3f(0.0f, 1.0f, 0.0f));
//                    lineY.setCoordinate(1, new Point3f(0.0f, 0.0f, 0.0f));
//                    Appearance a = new Appearance();
//                    Material m = new Material();
//                    m.setDiffuseColor(new Color3f(Color.CYAN));
//                    m.setAmbientColor(new Color3f(Color.CYAN));
//                    m.setShininess(0.0f);
//                    a.setMaterial(m);
//                    line = new Shape3D(lineY, a);
//                    tg.addChild(line);
                    int jagged = getJagged();
                    double[] rectDip = getRectDip();
                    int boxX = ModelShapeParam.getActiveProjectGridSizeAlongXAxis();

                    if (voxelizationCheckBox.isSelected()) {

                        SizeModel sizeModel = (SizeModel) Context.getInstance().getChildModelFromSelectedBox(SizeModel.class);
                        if (sizeModel.getType() == SizeEnum.EqRadius) {
                            set3dLoadedStatus("<html>the voxelization view available only for<br><b>'Size along X axis'</b> </html>");
                            return;
                        }

                        if (boxX <= 0) {
                            set3dLoadedStatus("Invalid parameters");
                            if (Context.getInstance().getLastParamsComponent() != null) {
                                Context.getInstance().getLastParamsComponent().requestFocus();
                            }
                            return;
                        }

                        if (boxX > 150) {
                            set3dLoadedStatus("<html>estimated count of voxels are <br>more than <b>10^6</b> <br> 3D model cannot be loaded<br>as voxelization view</html>");
                            return;
                        }


                        shapeModel.createVoxelizedShape(tg, boxX, jagged, rectDip);
                    } else {
                        shapeModel.createSurfaceShape(tg);
                    }


                    if (boxX > 0) {
                        double rectScaleX = rectDip[0];
                        double rectScaleY = rectDip[1];
                        double rectScaleZ = rectDip[2];
                        int boxY = shapeModel.getBoxY(boxX, rectScaleX, rectScaleY, rectScaleZ, jagged);
                        int boxZ = shapeModel.getBoxZ(boxX, rectScaleX, rectScaleY, rectScaleZ, jagged);
                        float factorY = (float)(1.*boxY/boxX*rectScaleY/rectScaleX);
                        float factorZ = (float)(1.*boxZ/boxX*rectScaleZ/rectScaleX);

                        int fontSize = 20;
                        if (shapeModel.getInitialScale() < 4) {
                            fontSize = fontSize + 10 * (int)(4 - shapeModel.getInitialScale());
                        }
                        float delta = 0.15f;// + 0.1f*(4 - (float)shapeModel.getInitialScale());
                        addAxis(new Point3f(0.5f + delta, 0.0f, 0.0f), "x", fontSize);
                        addAxis(new Point3f(0.0f, 0.5f*factorY + delta, 0.0f), "y", fontSize);
                        addAxis(new Point3f(0.0f, 0.0f, 0.5f*factorZ + delta), "z", fontSize);
                    }

                    MouseRotate myMouseRotate = new MouseRotate();
                    myMouseRotate.setTransformGroup(tg);
                    myMouseRotate.setSchedulingBounds(new BoundingSphere());
                    myMouseRotate.setupCallback((i, transform3D) -> tg.getTransform(t3d));
                    objRoot.addChild(myMouseRotate);

                    MouseWheelZoom mouseWheelZoom = new MouseWheelZoom();
                    mouseWheelZoom.setTransformGroup(tg);
                    mouseWheelZoom.setSchedulingBounds(tg.getBounds());
                    mouseWheelZoom.setupCallback((i, transform3D) -> tg.getTransform(t3d));
//                mouseWheelZoom.setupCallback((i, transform3D) -> {
//                    System.out.println();
//                    tg.getTransform(t3d);
//                    System.out.println(t3d.toString());
//                    System.out.println();
//                });
                    objRoot.addChild(mouseWheelZoom);
                    objRoot.compile();

                    scene.addChild(objRoot);

                    Background background = new Background();
                    background.setColor(shapeModel.getBackgroundColor());
                    background.setApplicationBounds(bounds);
                    scene.addChild(background);
                    universe.addBranchGraph(scene);

                    if (modelToProcess == null) {
                        SwingUtilities.invokeLater(() -> {
                            try {
                                canvasPanel.removeAll();
                                canvasPanel.add(canvas);
                                canvasPanel.repaint();
                                canvasPanel.revalidate();

                                if (Context.getInstance().getLastParamsComponent() != null) {
                                    Context.getInstance().getLastParamsComponent().requestFocus();
                                }
                            } catch (Exception e) {
                                set3dLoadedStatus("<html>ERROR: "+e.getMessage()+"</html>");
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    SwingUtilities.invokeLater(() -> {
                        set3dLoadedStatus("something went wrong, try again");
                    });
                } finally {
                    isBusy = false;
                    if (modelToProcess == null) {
                        SwingUtilities.invokeLater(() -> voxelizationCheckBox.setEnabled(true));
                    }
                }
            }
        };
        new Thread(runnable).start();

//        try {
//            SwingUtilities.invokeLater(runnable);
        //runnable.run();
//        } catch (Exception e) {
//            set3dLoadedStatus("something went wrong, try again");
//        }

        //canvasPanel.repaint();
    }
    protected void addAxis(Point3f endPoint, String title, int fontSize) {
        LineArray lineX = new LineArray(2, LineArray.COORDINATES);
        lineX.setCoordinate(0, new Point3f(0.0f, 0.0f, 0.0f));
        lineX.setCoordinate(0, endPoint);
        Appearance ap = new Appearance();
        final Color3f white = new Color3f(1f, 1f, 1f);
        ap.setMaterial(new Material(white,white,white,white, 0.5f));
        Shape3D line = new Shape3D(lineX, ap);
        tg.addChild(line);

        for (int i = 0; i < 2; i++) {
            Text2D axisLabel = new Text2D(title, white, "Console", fontSize, Font.BOLD);

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
    protected int getJagged() {
        JaggedModel jaggedModel = (JaggedModel) Context.getInstance().getChildModelFromSelectedBox(JaggedModel.class);
        return jaggedModel.getFlag() ? jaggedModel.getJagged() : 1;
    }

    protected double[] getRectDip() {
        DipoleShapeModel dipoleShapeModel = (DipoleShapeModel) Context.getInstance().getChildModelFromSelectedBox(DipoleShapeModel.class);
        double[] rectDip;
        if (dipoleShapeModel.getEnumValue() == DipoleShapeEnum.Rect) {
            double factor = Math.min(dipoleShapeModel.getScaleX(), dipoleShapeModel.getScaleY());
            factor = Math.min(dipoleShapeModel.getScaleZ(), factor);

            rectDip = new double[]{dipoleShapeModel.getScaleX()/factor, dipoleShapeModel.getScaleY()/factor, dipoleShapeModel.getScaleZ()/factor};
        } else {
            rectDip = new double[]{1, 1, 1};
        }
        return rectDip;
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
    BalloonTip noChangesBaloon;
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


            final ShapeSelectorModel model = (ShapeSelectorModel) sender;
            ((ModelShapeParam) model.getParamsBox().getModel()).initParams();

            String message = "The voxel grid is: ";
            if (noChangesBaloon != null) {
                noChangesBaloon.closeBalloon();
                noChangesBaloon = null;
            }
            if (!getShapeID(model).equals(currentShapeID)) {
                this.modelToProcess = model;
                set3dLoadedStatus();
            } else if (!isBusy && modelToProcess == null) {
                message = "The voxel grid remains unchanged: ";
            }

            RoundedBalloonStyle style = new RoundedBalloonStyle(5, 5, Color.WHITE, Color.black);
            final String viewerGrid = getViewerGrid();
            if (!viewerGrid.startsWith("0x")) {
                noChangesBaloon = new BalloonTip(
                        canvasPanel,
                        new JLabel("<html>"+ message + "<b>" + viewerGrid + "</b></html>"),
                        style,
                        BalloonTip.Orientation.RIGHT_ABOVE,
                        BalloonTip.AttachLocation.NORTHEAST,
                        30, 10,
                        true
                );
                BalloonTip copyNoChangeBaloon = noChangesBaloon;
                TimingUtils.showTimedBalloon(noChangesBaloon, 6000, (e) -> copyNoChangeBaloon.closeBalloon());
            }
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
                    isNewt3d = true;
                    modelToProcess = model;
                    set3dLoadedStatus();

                    //init3dCanvas((ModelShapeParam) model.getParamsBox().getModel());
                }
            }
        }
    }

    private String getShapeID(ShapeSelectorModel shapeSelectorModel) {
        return shapeSelectorModel.getEnumValue().toString()
                + (shapeSelectorModel.isVoxelization() ? ("voxel"+getViewerGrid()+"jagged"+getJagged()) : "math")
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