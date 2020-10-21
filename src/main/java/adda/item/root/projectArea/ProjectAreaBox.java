package adda.item.root.projectArea;

import adda.base.IAddaOption;
import adda.base.IAddaOptionsContainer;
import adda.base.annotation.*;
import adda.base.boxes.BoxBase;
import adda.base.boxes.IBox;
import adda.base.events.IModelPropertyChangeEvent;
import adda.base.models.IModel;
import adda.base.models.IModelObserver;
import adda.item.tab.base.BaseTabBox;
import adda.item.tab.base.refractiveIndexAggregator.RefractiveIndexAggregatorBox;
import adda.item.tab.base.refractiveIndexAggregator.RefractiveIndexAggregatorModel;
import adda.item.tab.base.size.SizeBox;
import adda.item.tab.base.size.SizeModel;
import adda.item.tab.internals.InternalsTabBox;
import adda.item.tab.internals.formulation.FormulationModel;
import adda.item.tab.options.OptionsBox;
import adda.item.tab.options.OptionsModel;
import adda.item.tab.output.OutputTabBox;
import adda.item.tab.output.granul.GranulSaveModel;
import adda.item.tab.shape.ShapeTabBox;
import adda.item.tab.shape.granules.GranulesModel;
import adda.item.tab.shape.selector.ShapeSelectorModel;
import adda.utils.Binder;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@BindModel
@BindView
@BindController
public class ProjectAreaBox extends BoxBase {

    private ShapeTabBox shapeTabBox;
    private BaseTabBox baseTabBox;
    private InternalsTabBox internalsTabBox;
    private OutputTabBox outputTabBox;
    private OptionsBox optionsBox;

    public ProjectAreaBox(String name) {
        this();
        this.name = name;
    }


    private ProjectAreaBox() {
        needInitSelf = true;
        children = new ArrayList<>();

        shapeTabBox = new ShapeTabBox();
        addChild(shapeTabBox);

        baseTabBox = new BaseTabBox();
        addChild(baseTabBox);

        internalsTabBox = new InternalsTabBox();
        addChild(internalsTabBox);

        outputTabBox = new OutputTabBox();
        addChild(outputTabBox);

        optionsBox = new OptionsBox();
        addChild(optionsBox);
    }

    @Override
    protected void initChildren() {
        super.initChildren();
        bindChildren();

    }

    private void bindChildren() {
        List<IModel> models = new ArrayList<>();
        getRecursiveModelList(this, models);

        //bind all adda options to OptionsModel
        //OptionsModel MUST be in children, in other way exception will signal about it
        //optionModel is aggregator of all configured command lines
        OptionsModel optionsModel = (OptionsModel) models.stream()
                .filter(entity -> entity instanceof OptionsModel)
                .findFirst()
                .get();

        models.forEach(loopModel -> {
            if (loopModel instanceof IAddaOptionsContainer) {
                loopModel.addObserver(optionsModel);
                if (!loopModel.isDefaultState()) {
                    optionsModel.add((IAddaOptionsContainer) loopModel);
                }
            }
        });

        //put current command lines to textarea todo rework?
        optionsModel.setCommandLineToArea();

        //to check is focused current tab
        //Binder.bindBoth(model, optionsModel);
        model.addObserver(optionsModel);
        ((ProjectAreaModel) model).setOptionsModel(optionsModel);



        ShapeSelectorModel shapeSelectorModel = (ShapeSelectorModel) models.stream()
                .filter(entity -> entity instanceof ShapeSelectorModel)
                .findFirst()
                .get();


        //set in granul for select shape domain
        GranulesModel granulesModel = (GranulesModel) models.stream()
                .filter(entity -> entity instanceof GranulesModel)
                .findFirst()
                .get();

        granulesModel.setShapeModel(shapeSelectorModel);


        //sync granulesModel and granulSaveModel
        GranulSaveModel granulSaveModel = (GranulSaveModel) models.stream()
                .filter(entity -> entity instanceof GranulSaveModel)
                .findFirst()
                .get();

        Binder.bindBoth(granulesModel, granulSaveModel);

        //RefractiveIndexAggregatorModel, ShapeSelectorModel MUST be in children, in other way exception will signal about it
        RefractiveIndexAggregatorModel refractiveIndexAggregatorModel = (RefractiveIndexAggregatorModel) models.stream()
                .filter(entity -> entity instanceof RefractiveIndexAggregatorModel)
                .findFirst()
                .get();
        refractiveIndexAggregatorModel.setShapeModel(shapeSelectorModel);
        refractiveIndexAggregatorModel.setGranulesModel(granulesModel);


        //sync granul refractive indexes
        Binder.bindBoth(granulesModel, refractiveIndexAggregatorModel);


        SizeModel sizeModel = (SizeModel) models.stream()
                .filter(entity -> entity instanceof SizeModel)
                .findFirst()
                .get();

        Binder.bind(sizeModel, granulesModel);

    }

    private void getRecursiveModelList(IBox box, List<IModel> list) {
        if (box.getChildren() == null || box.getChildren().size() == 0) return;

        for (IBox child : box.getChildren()) {
            if (child != null) {
                if (child.getModel() != null) {
                    list.add(child.getModel());
                }
                getRecursiveModelList(child, list);
            }

        }
    }

    @Override
    protected void initLayout() {
        ProjectAreaForm form = new ProjectAreaForm();

        form.getPanelShape().add(shapeTabBox.getLayout());
        form.getPanelBasic().add(baseTabBox.getLayout());
        form.getPanelOutput().add(outputTabBox.getLayout());
        form.getPanelDdaInternal().add(internalsTabBox.getLayout());
        form.getPanelOptions().add(optionsBox.getLayout());

        JPanel mainPanel = new JPanel(){
            @Override
            public boolean isOptimizedDrawingEnabled() {
                return false;
            }
        };
        mainPanel.setLayout(new OverlayLayout(mainPanel));
        mainPanel.add(view.getRootComponent());
        mainPanel.add(form.getPanelMain());
        this.panel = mainPanel;

//        List<IModel> models = new ArrayList<>();
//        getRecursiveModelList(internalsTabBox, models);
//        FormulationModel formulationModel = (FormulationModel) models.stream()
//                .filter(entity -> entity instanceof FormulationModel)
//                .findFirst()
//                .get();
//
//        formulationModel.addObserver(new IModelObserver() {
//            @Override
//            public void modelPropertyChanged(IModel sender, IModelPropertyChangeEvent event) {
//                Arrays.stream(form.getPanelDdaInternal().getComponents()).forEach(component -> {
//                    component.repaint();
//                    component.revalidate();
//                });
//                //form.getPanelDdaInternal().revalidate();
//                form.getScrollDdaInternal().repaint();
//                form.getScrollDdaInternal().revalidate();
//            }
//        });



    }

}
