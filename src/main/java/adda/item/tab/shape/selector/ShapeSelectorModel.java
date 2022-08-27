package adda.item.tab.shape.selector;

import adda.base.AddaOption;
import adda.base.IAddaOption;
import adda.base.IAddaOptionsContainer;
import adda.base.annotation.Viewable;
import adda.base.boxes.IBox;
import adda.base.events.IModelPropertyChangeEvent;
import adda.base.models.IModel;
import adda.base.models.IModelObserver;
import adda.base.models.ModelBase;
import adda.item.tab.TabEnumModel;
import adda.item.tab.internals.formulation.FormulationEnum;
import adda.item.tab.shape.selector.params.ModelShapeParam;
import adda.item.tab.shape.selector.params.bicoated.BicoatedBox;
import adda.item.tab.shape.selector.params.biellipsoid.BiellipsoidBox;
import adda.item.tab.shape.selector.params.bisphere.BisphereBox;
import adda.item.tab.shape.selector.params.capsule.CapsuleBox;
import adda.item.tab.shape.selector.params.chebyshev.ChebyshevBox;
import adda.item.tab.shape.selector.params.coated.CoatedBox;
import adda.item.tab.shape.selector.params.cuboid.CuboidBox;
import adda.item.tab.shape.selector.params.cylinder.CylinderBox;
import adda.item.tab.shape.selector.params.egg.EggBox;
import adda.item.tab.shape.selector.params.ellipsoid.EllipsoidBox;
import adda.item.tab.shape.selector.params.line.LineBox;
import adda.item.tab.shape.selector.params.plate.PlateBox;
import adda.item.tab.shape.selector.params.prism.PrismBox;
import adda.item.tab.shape.selector.params.rbc.RbcBox;
import adda.item.tab.shape.selector.params.sphere.SphereBox;
import adda.item.tab.shape.selector.params.spherecuboid.SphereCuboidBox;

import java.util.*;

public class ShapeSelectorModel extends TabEnumModel<ShapeSelectorEnum> implements IModelObserver {

    public static final String ENUM_VALUE_FIELD_NAME = "enumValue";
    public static final String AUTOROTATE_FIELD_NAME = "isAutorotate";
    public static final String SHAPE = "shape";
    transient private Map<ShapeSelectorEnum, IBox> paramsMap = new EnumMap<>(ShapeSelectorEnum.class);//todo replace with immutable in constructor
    transient private Map<ShapeSelectorEnum, List<ShapeDomainInfo>> domainInfoMap = new EnumMap<>(ShapeSelectorEnum.class);//todo replace with immutable in constructor



    public ShapeSelectorModel() {
        this.setLabel("Shape");//todo localization
        setEnumValue(ShapeSelectorEnum.sphere);
        setDefaultEnumValue(ShapeSelectorEnum.sphere);

        paramsMap.put(ShapeSelectorEnum.bicoated, new BicoatedBox());
        paramsMap.put(ShapeSelectorEnum.biellipsoid, new BiellipsoidBox());
        paramsMap.put(ShapeSelectorEnum.bisphere, new BisphereBox());
        paramsMap.put(ShapeSelectorEnum.box, new CuboidBox());
        paramsMap.put(ShapeSelectorEnum.capsule, new CapsuleBox());
        paramsMap.put(ShapeSelectorEnum.chebyshev, new ChebyshevBox());
        paramsMap.put(ShapeSelectorEnum.coated, new CoatedBox());
        paramsMap.put(ShapeSelectorEnum.cylinder, new CylinderBox());
        paramsMap.put(ShapeSelectorEnum.egg, new EggBox());
        paramsMap.put(ShapeSelectorEnum.ellipsoid, new EllipsoidBox());
        paramsMap.put(ShapeSelectorEnum.plate, new PlateBox());
        paramsMap.put(ShapeSelectorEnum.prism, new PrismBox());
        paramsMap.put(ShapeSelectorEnum.rbc, new RbcBox());
        paramsMap.put(ShapeSelectorEnum.spherebox, new SphereCuboidBox());
        paramsMap.put(ShapeSelectorEnum.sphere, new SphereBox());
        paramsMap.put(ShapeSelectorEnum.line, new LineBox());

        domainInfoMap.put(ShapeSelectorEnum.bicoated, Arrays.asList(new ShapeDomainInfo("bicoated cores", 1), new ShapeDomainInfo("bicoated shells", 2)));
        domainInfoMap.put(ShapeSelectorEnum.biellipsoid, Arrays.asList(new ShapeDomainInfo("first ellipsoid", 1), new ShapeDomainInfo("second ellipsoid", 2)));
        domainInfoMap.put(ShapeSelectorEnum.coated, Arrays.asList(new ShapeDomainInfo("coated core", 1), new ShapeDomainInfo("coated shell", 2)));
        domainInfoMap.put(ShapeSelectorEnum.spherebox, Arrays.asList(new ShapeDomainInfo("sphere", 1), new ShapeDomainInfo("box", 2)));

        //todo init in background worker
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                paramsMap.entrySet().forEach(entry -> {
                    entry.getValue().init();
                    entry.getValue().getModel().addObserver(ShapeSelectorModel.this);
                });
            }
        });
    }


    protected boolean isAutorotate = true;

    public boolean isAutorotate() {
        return isAutorotate;
    }

    public void setAutorotate(boolean autorotate) {
        if (this.isAutorotate != autorotate) {
            this.isAutorotate = autorotate;
            notifyObservers(AUTOROTATE_FIELD_NAME, isAutorotate);
        }
    }


    public List<ShapeDomainInfo> getShapeDomainInfos() {
        if (domainInfoMap.containsKey(enumValue)) {
            return domainInfoMap.get(enumValue);
        }
        return Arrays.asList(new ShapeDomainInfo(enumValue.toString(), 1));
    }

    public IBox getParamsBox() {
        if (paramsMap.containsKey(enumValue)) {
            return paramsMap.get(enumValue);
        }
        return null;
    }

    private static final String[] paramNames = new String[]{"firstParam", "secondParam", "thirdParam", "fourthParam", "fifthParam"};


    @Override
    protected List<IAddaOption> getAddaOptionsInner() {
        String command = SHAPE;
        StringBuilder valueBuilder = new StringBuilder(enumValue.toString());
        StringBuilder displayStringBuilder = new StringBuilder(enumValue.toString());


        IBox paramsBox = getParamsBox();
        if (paramsBox != null
                && paramsBox.getModel() != null
                && paramsBox.getModel() instanceof ModelShapeParam) {
            ModelShapeParam paramsModel = (ModelShapeParam) paramsBox.getModel();
            List<String> params = paramsModel.getParamsList();

            //no more than 5 params
            final int count = Math.min(params.size(), 5);
            if (count > 0) {
                valueBuilder.append(" ");
                displayStringBuilder.append(" ");
                displayStringBuilder.append("(");
                for (int i = 0; i < count; i++) {
                    String label = paramsModel.getViewableLabel(paramNames[i]);
                    String param = params.get(i);

                    valueBuilder.append(param);

                    displayStringBuilder.append(label);
                    displayStringBuilder.append("=");
                    displayStringBuilder.append(param);
                    if (i < count - 1) {
                        valueBuilder.append(" ");
                        displayStringBuilder.append("; ");
                    }
                }
                displayStringBuilder.append(")");
            }

        }

        return Arrays.asList(new AddaOption(command, valueBuilder.toString(), displayStringBuilder.toString()));
    }

    @Override
    public void modelPropertyChanged(IModel sender, IModelPropertyChangeEvent event) {
        if (sender instanceof ModelShapeParam) {
            notifyObservers(ENUM_VALUE_FIELD_NAME, enumValue);
        }
    }
}