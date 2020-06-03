package adda.base.boxes;

import adda.base.IModelState;
import adda.base.annotation.BindController;
import adda.base.annotation.BindModel;
import adda.base.annotation.BindView;
import adda.base.controllers.ControllerBase;
import adda.base.controllers.IController;
import adda.base.models.IModel;
import adda.base.models.ModelBase;
import adda.base.views.IView;
import adda.base.views.ViewBase;
import org.reflections.Reflections;

import javax.swing.*;
import java.awt.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoxBase implements IBox {

    protected boolean needInitSelf = true;
    protected String name;
    protected IView view;
    protected IModel model;
    protected IController controller;
    protected IBox parent;
    protected JPanel panel;
    protected List<IBox> children;

    public boolean isInitialized() {
        return isInitialized;
    }

    protected boolean isInitialized = false;

    public static final Map<String, Class<? extends IModel>> models = new HashMap<>();
    public static final Map<String, Class<? extends IController>> controllers = new HashMap<>();
    public static final Map<String, Class<? extends IView>> views = new HashMap<>();

    private static final String MODEL = "Model";
    private static final String VIEW = "View";
    private static final String CONTROLLER = "Controller";

    /**
     * The prefix says that all components(model, view, controller) should be in this package, for correct in architecture
     */
    static {
        Reflections reflections = new Reflections("adda.item");

        reflections.getSubTypesOf(IModel.class)
                .forEach(model -> models.put(model.getSimpleName(), model));

        reflections.getSubTypesOf(IView.class)
                .forEach(view -> views.put(view.getSimpleName(), view));

        reflections.getSubTypesOf(IController.class)
                .forEach(controller -> controllers.put(controller.getSimpleName(), controller));
    }

    public BoxBase() {
        this.name = this.getClass().getSimpleName();
    }

    public BoxBase(String name) {
        this.name = name;
    }

    public void init() {
        if (!isInitialized) {
            initSelf();
            initChildren();
            initLayout();
            isInitialized = true;
        }
    }

    protected JPanel getPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        //panel.setLayout(new VerticalLayout());
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setAlignmentY(Component.TOP_ALIGNMENT);
        return panel;
    }

    protected void initLayout() {
        JPanel panel = getPanel();

        if (needInitSelf) {
            panel.add(view.getRootComponent());
        }

        if (children != null) {
            for (IBox container : children) {
                panel.add(container.getLayout());
            }
        }
        //panel.add(Box.createGlue());

        this.panel = panel;
    }


    protected void initSelf() {
        if (!needInitSelf) return;
        initModel();//todo check is success
        initView(model);//todo check is success
        initController(model, view);//todo check is success
    }

    protected void initChildren() {
        if (children == null) return;
        for (IBox container : children) {
            container.init();
        }
    }

    protected void initModel() {
        this.model = getAssociatedModel();
    }

    protected void initView(IModel model) {
        IView view = getAssociatedView();
        model.addObserver(view); //todo check is null
        view.initFromModel(model);
        this.view = view;
    }

    protected void initController(IModel model, IView view) {
        ControllerBase controller = (ControllerBase) getAssociatedController();
        controller.setModel(model);
        controller.setView(view);
        controller.init();
        this.controller = controller;
    }

    public IModel getAssociatedModel() {
        return getAssociatedComponent(IModel.class, new ModelBase(), models);
    }

    public IController getAssociatedController() {
        return getAssociatedComponent(IController.class, new ControllerBase(), controllers);
    }

    public IView getAssociatedView() {
        return getAssociatedComponent(IView.class, new ViewBase(), views);
    }

    private <T> T getAssociatedComponent(Class<T> baseInterface, T baseObject, Map<String, Class<? extends T>> componentMap) {
        String replacementName = getReplacementName(baseInterface.getSimpleName());
        Annotation annotation = this.getClass().getAnnotation(getAnnotationByClassName(replacementName));
        String bindComponentName = null;

        try {
            if (annotation != null) {
                Method method = annotation.getClass().getMethod("value");
                Class<T> annotationValue = (Class<T>) method.invoke(annotation);
                bindComponentName = !annotationValue.equals(baseInterface) ? annotationValue.getSimpleName() :
                        this.getClass().getSimpleName().replace("Box", replacementName);
            } else if(!this.getClass().equals(BoxBase.class)) { //todo remove temporary if (impl. for test reason)
                throw new RuntimeException("Can not found bind by " + replacementName.toLowerCase()
                        + " for " + this.getClass().getSimpleName());
            }

            if (componentMap.containsKey(bindComponentName)) {
                baseObject = componentMap.get(bindComponentName).newInstance();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return baseObject;
    }

    private static String getReplacementName(String name) {
        if (name.contains(MODEL)) {
            return MODEL;
        } else if (name.contains(VIEW)) {
            return VIEW;
        } else if (name.contains(CONTROLLER)) {
            return CONTROLLER;
        } else {
            throw new RuntimeException("Failed to get replacement name"); //todo make correct exception
        }
    }

    private static Class<? extends Annotation> getAnnotationByClassName(String name) {
        if (name.contains(MODEL)) {
            return BindModel.class;
        } else if (name.contains(VIEW)) {
            return BindView.class;
        } else if (name.contains(CONTROLLER)) {
            return BindController.class;
        } else {
            throw new RuntimeException("Failed to get annotation"); //todo make correct exception
        }
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public JPanel getLayout() {
        return panel;
    }

    @Override
    public IModelState getState() {
        return null;
    }

    @Override
    public IModel getModel() {
        return model;
    }

    @Override
    public IBox getParent() {
        return this.parent;
    }

    @Override
    public List<IBox> getChildren() {

        //return Collections.unmodifiableList(children);
        return children;//set immutable list
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean setState(IModelState model) {
        return false;
    }

    @Override
    public void setParent(IBox parent) {
        this.parent = parent;
    }

    @Override
    public void addChild(IBox child) {
        children.add(child);
        child.setParent(this);
    }

    @Override
    public void removeChild(IBox child) {
        children.remove(child);
        child.setParent(null);
    }

    @Override
    public void clearChildren() {
        children.clear();
    }
}
