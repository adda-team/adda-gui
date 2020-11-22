package adda.base.models;

import adda.base.annotation.BindEnableFrom;
import adda.base.events.ModelPropertyChangeType;
import adda.base.events.ModelPropertyChangedEvent;
import adda.base.annotation.Viewable;
import adda.utils.ReflectionHelper;
import adda.utils.StringHelper;

import java.awt.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;

public class ModelBase implements IModel {

    public static final String LABEL_FIELD_NAME = "label";
    protected List<IModelObserver> observers;

    protected boolean isVisibleIfDisabled = false;

    @Override
    public boolean isVisibleIfDisabled() {
        return isVisibleIfDisabled;
    }

    @Viewable
    protected String label;//todo localization (add field original label, put into original label untranslated string)

    protected Map<String, String> nameMap = new HashMap<>();
    protected Map<String, Class> typeMap = new LinkedHashMap<>();//LinkedHashMap instead of HashMap because  we need the same sequence as they were inserted
    protected Map<String, String> bindMap = new HashMap<>();

    public ModelBase() {
        initPropertyInfo();
        observers = new LinkedList<>();
        label = this.getClass().getSimpleName().replaceAll("Model", "");
    }

    protected Map<String, String> validationErrors = new HashMap<>();

    public Map<String, String> getValidationErrors() {
        return validationErrors;
    }

    public boolean validate() {
        return true;
    }

    protected void initPropertyInfo() {
        Class clazz = this.getClass();

        Map<String, Class> genericMap = new LinkedHashMap<>();
        Map<String, Integer> orderMap = new LinkedHashMap<>();

        int counter = 0;

        while (clazz != Object.class) {
            Type t = clazz.getGenericSuperclass();
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Viewable.class)) {
                    String fName = field.getName();
                    Viewable annotation = field.getAnnotation(Viewable.class);
                    String vName = annotation.value().trim();
                    if (!StringHelper.isEmpty(vName)) {
                        nameMap.put(fName, vName);
                    }
                    if (field.getGenericType() != field.getType() && genericMap.containsKey(field.getGenericType().getTypeName())) {
                        typeMap.put(fName, genericMap.get(field.getGenericType().getTypeName()));
                    } else {
                        typeMap.put(fName, field.getType());
                    }

                    int order = annotation.order();
                    orderMap.put(fName, order == 0 ? counter++ : order);
                }
                if (field.isAnnotationPresent(BindEnableFrom.class)) {
                    if (!StringHelper.isEmpty(field.getAnnotation(BindEnableFrom.class).value())) {
                        String source = field.getAnnotation(BindEnableFrom.class).value().trim();
                        String observer = field.getName();

                        bindMap.put(source, observer);
                    }
                }
            }
            clazz = clazz.getSuperclass();

            //!!! works only for ONE inheritance !!!
            //!!! if the same generic parameters name (for example T in one level and next level) !!!
            //todo rework and think about it!

            if (t instanceof ParameterizedType && clazz.getTypeParameters().length > 0) {
                ParameterizedType pType = (ParameterizedType) t;
                if (pType.getActualTypeArguments().length == clazz.getTypeParameters().length) {
                    for (int i = 0; i < pType.getActualTypeArguments().length; i++) {
                        genericMap.put(clazz.getTypeParameters()[i].getName(), (Class) pType.getActualTypeArguments()[i]);
                    }

                }
            }
        }

        Map<String, Class> tmp = new LinkedHashMap<>();
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(orderMap.entrySet());
        entries.stream()
                .sorted(Comparator.comparing(Map.Entry::getValue))
                .forEachOrdered(e -> tmp.put(e.getKey(), typeMap.get(e.getKey())));

        typeMap = tmp;
    }


    @Override
    public String getGroupName() {
        return null;
    }

    @Override
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
        notifyObservers(LABEL_FIELD_NAME, label);
    }


    @Override
    public Map<String, Class> getViewableProperties() {
        //return new HashMap<>(){{putAll(typeMap);}};
        return typeMap; //todo need optimization and safety, outer items can change hashmap
    }

    @Override
    public Map<String, String> getBindProperties() {
        return bindMap;//todo need optimization and safety, outer items can change hashmap
    }

    public String getViewableLabel(String field) {
        if (nameMap.containsKey(field)) {
            return nameMap.get(field);
        }
        return null;
    }

    @Override
    public void addObserver(IModelObserver observer) {
        if (observer == null) return;
        if (observers.contains(observer)) return;

        observers.add(observer);
    }

    @Override
    public void removeObserver(IModelObserver observer) {
        if (observer == null) return;
        if (!observers.contains(observer)) return;

        observers.remove(observer);
    }

    @Override
    public void copyProperties(IModel model) {
        ReflectionHelper.copy(model, this);
    }

    @Override
    public void applyDefaultState() {

    }

    @Override
    public boolean isDefaultState() {
        return true;
    }

//    protected void notifyObservers() {
//        for (IModelObserver observer : observers)
//            observer.modelPropertyChanged(this, new ModelPropertyChangedEvent("", ""));
//    }

    protected void notifyObservers(String propertyName, Object propertyValue) {
        for (IModelObserver observer : observers)
            observer.modelPropertyChanged(this, new ModelPropertyChangedEvent(propertyName, propertyValue));
    }

    protected void notifyObservers(String propertyName, Object propertyValue, ModelPropertyChangeType propertyChangeType) {
        for (IModelObserver observer : observers)
            observer.modelPropertyChanged(this, new ModelPropertyChangedEvent(propertyName, propertyValue, propertyChangeType));
    }


    @Override
    public Object clone() throws CloneNotSupportedException {
        //it is non-deep cloning
        try {
            Constructor constructor = this.getClass().getDeclaredConstructor();
            IModel model = (IModel) constructor.newInstance();
            model.copyProperties(this);
            return model;
        } catch (InstantiationException |
                IllegalAccessException |
                NoSuchMethodException |
                InvocationTargetException ex) {
            ex.printStackTrace();
        }

        return super.clone();
    }

}
