package adda.item.tab.base.refractiveIndexAggregator;

import adda.Context;
import adda.base.AddaOption;
import adda.base.IAddaOption;
import adda.base.boxes.BoxBase;
import adda.base.events.IModelPropertyChangeEvent;
import adda.base.models.IModel;
import adda.base.models.IModelObserver;
import adda.base.models.ModelBase;
import adda.base.models.ModelBaseAddaOptionsContainer;
import adda.item.tab.base.refractiveIndex.RefractiveIndexBox;
import adda.item.tab.base.refractiveIndex.RefractiveIndexModel;
import adda.item.tab.shape.dipoleShape.DipoleShapeEnum;
import adda.item.tab.shape.dipoleShape.DipoleShapeModel;
import adda.item.tab.internals.formulation.FormulationModel;
import adda.item.tab.internals.formulation.InteractionEnum;
import adda.item.tab.internals.formulation.PolarizationEnum;
import adda.item.tab.shape.granules.GranulesModel;
import adda.item.tab.shape.selector.ShapeSelectorModel;
import adda.utils.Binder;
import adda.utils.StringHelper;

import javax.swing.*;
import java.util.*;

public class RefractiveIndexAggregatorModel extends ModelBaseAddaOptionsContainer implements IModelObserver {


    public static final String SHAPE_REFRACTIVE_INDEXES_COUNT_FIELD_NAME = "shapeRefractiveIndexesCount";
    public static final String IS_SHOW_SHAPE_FIELD_NAME = "isShowShape";
    public static final String IS_SHOW_GRANUL_FIELD_NAME = "isShowGranul";
    public static final String IS_SHOW_SURFACE_FIELD_NAME = "isShowSurface";
    public static final String GRANULES = "granules";
    public static final String SURFACE = "surface";
    public static final String M = "m";
    public static final String ANISOTR = "anisotr";
    public static final String ANISOTROPY = "anisotropy";
    public static final String COMPLEX_FORMAT = "%s + i%s";
    public static final String OX = "xx ";
    public static final String OY = "; yy ";
    public static final String OZ = "; zz ";
    public static final String SPACE_STR = " ";
    public static final String SEPARATE_STR = "; ";
    protected boolean isShowGranul = false;
    protected boolean isShowSurface = false;
    protected boolean isShowShape = true;
    protected int shapeRefractiveIndexesCount = 0;

    public int getShapeRefractiveIndexesCount() {
        return shapeRefractiveIndexesCount;
    }

    public void setShapeRefractiveIndexesCount(int shapeRefractiveIndexesCount) {
        this.shapeRefractiveIndexesCount = shapeRefractiveIndexesCount;
        notifyObservers(SHAPE_REFRACTIVE_INDEXES_COUNT_FIELD_NAME, shapeRefractiveIndexesCount);

    }




    transient protected ShapeSelectorModel shapeModel;



    transient protected GranulesModel granulesModel;
//    protected ShapeSelectorModel shapeModel;



    transient protected List<RefractiveIndexBox> shapeBoxes = Collections.unmodifiableList(Arrays.asList(new RefractiveIndexBox(), new RefractiveIndexBox()));
    transient protected RefractiveIndexBox granulBox = new RefractiveIndexBox();
    transient protected RefractiveIndexBox surfaceBox = new RefractiveIndexBox();


    public RefractiveIndexAggregatorModel() {
        setLabel("Refractive indexes");//todo localization
        SwingUtilities.invokeLater(() -> {
            shapeBoxes.forEach(BoxBase::init);
            granulBox.init();
            final RefractiveIndexModel refractiveIndexModel = (RefractiveIndexModel) granulBox.getModel();
            refractiveIndexModel.getValidationErrors().put(RefractiveIndexModel.IS_ENABLED_ANISOTROP_FIELD_NAME, StringHelper.toDisplayString("<html>Anisotropy refractive index <br>does`t compatible with granules"));
            refractiveIndexModel.setEnabledAnisotrop(false);
            surfaceBox.init();
            ((RefractiveIndexModel) surfaceBox.getModel()).setEnabledAnisotrop(false);

            shapeBoxes.forEach(shapeBox -> {
                shapeBox.getModel().addObserver(RefractiveIndexAggregatorModel.this);
            });

            granulBox.getModel().addObserver(RefractiveIndexAggregatorModel.this);
            surfaceBox.getModel().addObserver(RefractiveIndexAggregatorModel.this);

        });
    }

    public GranulesModel getGranulesModel() {
        return granulesModel;
    }

    public void setGranulesModel(GranulesModel granulesModel) {
        if (this.granulesModel != granulesModel) {

            if (this.granulesModel != null) {
                Binder.unbindBoth(granulBox.getModel(), this.granulesModel);
                this.granulesModel.removeObserver(this);
            }
            if (granulesModel != null) {
                setShowGranul(true);
                granulesModel.addObserver(this);
                granulBox.init();
                ((ModelBase)granulBox.getModel()).setLabel(GRANULES);//todo localization
                Binder.bindBoth(granulBox.getModel(), granulesModel);
            }
            this.granulesModel = granulesModel;
            setShowGranul(this.granulesModel != null && this.granulesModel.isUseGranul());
        }
    }

    public ShapeSelectorModel getShapeModel() {
        return shapeModel;
    }

    public void setShapeModel(ShapeSelectorModel shapeModel) {
        if (this.shapeModel != shapeModel) {

            if (this.shapeModel != null) {
                this.shapeModel.removeObserver(this);
            }
            if (shapeModel != null) {
                shapeModel.addObserver(this);
            }
            this.shapeModel = shapeModel;
            setShowShape(this.shapeModel != null);

        }
    }

    public List<RefractiveIndexBox> getShapeBoxes() {
        return shapeBoxes;
    }

    public RefractiveIndexBox getGranulBox() {
        return granulBox;
    }

    public RefractiveIndexBox getSurfaceBox() {
        return surfaceBox;
    }

    public boolean isShowShape() {
        return isShowShape;
    }

    public void setShowShape(boolean isShowShape) {
        //no check for fire events everytime
        if (this.isShowShape != isShowShape) {
            this.isShowShape = isShowShape;
            notifyObservers(IS_SHOW_SHAPE_FIELD_NAME, isShowShape);
        }
    }

    public boolean isShowGranul() {
        return isShowGranul;
    }

    public void setShowGranul(boolean isShowGranul) {
        if (this.isShowGranul != isShowGranul) {
            this.isShowGranul = isShowGranul;
            notifyObservers(IS_SHOW_GRANUL_FIELD_NAME, isShowGranul);
        }
    }
    
    public boolean isShowSurface() {
        return isShowSurface;
    }

    public void setShowSurface(boolean isShowSurface) {
        if (this.isShowSurface != isShowSurface) {
            this.isShowSurface = isShowSurface;
            notifyObservers(IS_SHOW_SURFACE_FIELD_NAME, isShowSurface);
        }
    }

    @Override
    public void modelPropertyChanged(IModel sender, IModelPropertyChangeEvent event) {
        if (sender!= null) {
            if (sender.equals(shapeModel)) {
                notifyObservers();
                return;
            }

            if (sender.equals(granulesModel)) {
                if (GranulesModel.IS_USE_GRANUL_FIELD_NAME.equals(event.getPropertyName())) {
                    setShowGranul(granulesModel.isUseGranul());
                }
                return;
            }

            if (ModelBase.LABEL_FIELD_NAME.equals(event.getPropertyName())) {
                return;
            }

            if(isShowSurface && sender.equals(surfaceBox.getModel())) {
                notifyObservers();
                return;
            }

            if(isShowGranul && sender.equals(granulBox.getModel())) {
                notifyObservers();
                return;
            }

            if (sender instanceof RefractiveIndexModel) {
                notifyObservers();
            }

            if (sender instanceof FormulationModel && !FormulationModel.SCATTERING_QUANTITIES_FIELD_NAME.equals(event.getPropertyName())) {
                crossValidation();
            }

            if (sender instanceof DipoleShapeModel) {
                crossValidation();
            }
        }
    }

    private void crossValidation() {
        FormulationModel formulationModel = (FormulationModel) Context.getInstance().getChildModelFromSelectedBox(FormulationModel.class);
        DipoleShapeModel dipoleShapeModel = (DipoleShapeModel) Context.getInstance().getChildModelFromSelectedBox(DipoleShapeModel.class);
        String error = "";
        boolean isEnableAnisotropy = true;
        if (formulationModel.getPolarization() == PolarizationEnum.cldr) {
            isEnableAnisotropy = false;
            error += StringHelper.toDisplayString("<html>Anisotropy refractive index <br>does`t compatible with CLDR polarizability formulation</html>");
        }
        if (formulationModel.getInteraction() == InteractionEnum.so) {
            isEnableAnisotropy = false;
            error += StringHelper.toDisplayString("<html>Anisotropy refractive index <br>does`t compatible with SO interaction</html>");
        }
        if (dipoleShapeModel.getEnumValue() == DipoleShapeEnum.Rect) {
            isEnableAnisotropy = false;
            error += StringHelper.toDisplayString("<html>Anisotropy refractive index <br>does`t compatible with non cubic dipoles</html>");
        }



        setEnableAnisotropyExternal(error, isEnableAnisotropy);
    }

    private void setEnableAnisotropyExternal(String error, boolean isEnableAnisotropy) {
        boolean finalIsEnableAnisotropy = isEnableAnisotropy;
        String finalError = error;
        shapeBoxes
                .forEach(box -> {
                    RefractiveIndexModel refractiveIndexModel = (RefractiveIndexModel) box.getModel();

                    //!!! error must set before disabling
                    refractiveIndexModel.getValidationErrors().put(RefractiveIndexModel.IS_ENABLED_ANISOTROP_FIELD_NAME, finalError);
                    refractiveIndexModel.setEnabledAnisotrop(finalIsEnableAnisotropy);

                });
    }

    private void notifyObservers() {
        notifyObservers(IS_SHOW_SHAPE_FIELD_NAME, isShowShape);
    }

    @Override
    public boolean isDefaultState() {

        if (shapeModel == null) return true;

        if (shapeModel.getShapeDomainInfos() == null) return false;

        boolean isDefault = true;

        int count = Math.min(shapeBoxes.size(), shapeModel.getShapeDomainInfos().size());

        for (int i = 0; i < count; i++) {
            isDefault = isShowShape && shapeBoxes.get(i).getModel().isDefaultState();
            if (!isDefault) {
                break;
            }
        }

        if (isDefault && isShowGranul) {
            isDefault = granulBox.getModel().isDefaultState();
        }

        if (isDefault && isShowSurface) {
            isDefault = surfaceBox.getModel().isDefaultState();
        }

        return isDefault;
    }

    @Override
    public void applyDefaultState() {
        if (isShowShape && shapeModel.getShapeDomainInfos() != null) {
            int count = Math.min(shapeBoxes.size(), shapeModel.getShapeDomainInfos().size());
            for (int i = 0; i < count; i++) {
                shapeBoxes.get(i).getModel().applyDefaultState();
            }
        }

        if (isShowGranul) {
            granulBox.getModel().applyDefaultState();
        }

        if (isShowSurface) {
            surfaceBox.getModel().applyDefaultState();
        }

    }

    @Override
    protected List<IAddaOption> getAddaOptionsInner() {

        Map<String, RefractiveIndexModel> map = new LinkedHashMap<>();

        int count = Math.min(shapeBoxes.size(), shapeModel.getShapeDomainInfos().size());

        for (int i = 0; i < count; i++) {
            map.put(shapeModel.getShapeDomainInfos().get(i).getName(), (RefractiveIndexModel) shapeBoxes.get(i).getModel());
        }

        if (isShowGranul) {
            map.put(StringHelper.toDisplayString(GRANULES), (RefractiveIndexModel) granulBox.getModel());
        }

        if (isShowSurface) {
            map.put(StringHelper.toDisplayString(SURFACE), (RefractiveIndexModel) surfaceBox.getModel());
        }

        boolean isAnisotrop = map.entrySet().stream().anyMatch(entry -> entry.getValue().isAnisotrop());

        StringBuilder valueBuilder = new StringBuilder();
        StringBuilder descriptionBuilder = new StringBuilder();

        map.entrySet().forEach(entry -> {
            valueBuilder.append(getParams(entry.getValue(), isAnisotrop))
                        .append(SPACE_STR);

            descriptionBuilder.append(entry.getKey())
                              .append(SPACE_STR)
                              .append(getInfo(entry.getValue(), entry.getValue().isAnisotrop()))
                              .append(SEPARATE_STR);
        });

        IAddaOption option = new AddaOption(M, valueBuilder.toString(), descriptionBuilder.toString().substring(0, descriptionBuilder.length() - 2));
        if (isAnisotrop) {
            return Arrays.asList(new AddaOption(ANISOTR, null, StringHelper.toDisplayString(ANISOTROPY)) , option);
        } else {
            return Arrays.asList(option);
        }
    }

    private String getParams(RefractiveIndexModel refractiveIndexModel, boolean isAnisotrop) {
        StringBuilder builder =
                new StringBuilder()
                        .append(StringHelper.toDisplayString(refractiveIndexModel.getRealX()))
                        .append(SPACE_STR)
                        .append(StringHelper.toDisplayString(refractiveIndexModel.getImagX()));
        if (isAnisotrop) {
            builder.append(SPACE_STR)
                   .append(StringHelper.toDisplayString(refractiveIndexModel.getRealY()))
                   .append(SPACE_STR)
                   .append(StringHelper.toDisplayString(refractiveIndexModel.getImagY()))
                   .append(SPACE_STR)
                   .append(StringHelper.toDisplayString(refractiveIndexModel.getRealZ()))
                   .append(SPACE_STR)
                   .append(StringHelper.toDisplayString(refractiveIndexModel.getImagZ()));

        }
        return builder.toString();
    }

    private String getInfo(RefractiveIndexModel refractiveIndexModel, boolean isAnisotrop) {
        if (isAnisotrop) {
            return new StringBuilder()
                    .append(OX)
                    .append(getComplexInfo(refractiveIndexModel.getRealX(), refractiveIndexModel.getImagX()))
                    .append(OY)
                    .append(getComplexInfo(refractiveIndexModel.getRealY(), refractiveIndexModel.getImagY()))
                    .append(OZ)
                    .append(getComplexInfo(refractiveIndexModel.getRealZ(), refractiveIndexModel.getImagZ()))
                    .toString();
        }
        return getComplexInfo(refractiveIndexModel.getRealX(), refractiveIndexModel.getImagX());
    }

    private String getComplexInfo(double real, double imag) {
        return String.format(COMPLEX_FORMAT, StringHelper.toDisplayString(real), StringHelper.toDisplayString(imag));
    }
}