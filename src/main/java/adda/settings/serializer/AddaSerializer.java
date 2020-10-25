package adda.settings.serializer;

import adda.settings.formatters.IFormatter;
import adda.settings.formatters.IFormatterItem;
import adda.settings.formatters.xml.XmlFormatter;

import java.lang.reflect.*;
import java.math.BigDecimal;
import java.util.*;


public class AddaSerializer extends Serializer {

    protected IFormatter formatter;

    public AddaSerializer() {
        formatter = new XmlFormatter();
    }

    public AddaSerializer(IFormatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public String serialize(Object obj) {
        formatter.init();
        IFormatterItem element = formatter.createFormatterItem(getClassTagName(obj.getClass()));
        formatter.getRootFormatterItem().appendChild(element);
        innerSerialize(obj, element);
        return formatter.getFormattedText();
    }

    private void innerSerialize(final Object obj, final IFormatterItem currentElement) {
        //todo obj is null
        if (obj == null) {
            currentElement.setValue("null");
            return;
        }

        Class<?> currentClass = obj.getClass();
        if (isPrimitive(currentClass)) {
            currentElement.setValue(obj.toString());
        } else if (obj instanceof List) {
            serializeList((List<?>) obj, currentElement);
        } else if (obj instanceof Map) {
            serializeMap((Map<?, ?>) obj, currentElement);
        } else {
            serializeObject(obj, currentElement, currentClass);
        }
    }

    private void serializeList(List<?> list, IFormatterItem currentElement) {
        Class<?> genericType = list.get(0).getClass();
        currentElement.setArrayAttribute(true);
        if (isPrimitive(genericType)) {
            list.forEach(listElement -> {currentElement.appendChild(formatter.createFormatterItem(genericType.getSimpleName(), listElement.toString()));});
        } else {
            for (Object item : list) {
                IFormatterItem loopElement = formatter.createFormatterItem(genericType.getSimpleName());
                innerSerialize(item, loopElement);
                currentElement.appendChild(loopElement);
            }
        }
    }

    private void serializeMap(Map<?, ?> map, IFormatterItem currentElement) {
        Set<? extends Map.Entry<?, ?>> entrySet = map.entrySet();
        Map.Entry<?, ?> firstElement = entrySet.iterator().next();
        Class<?> keyType = firstElement.getKey().getClass();
        Class<?> valueType = firstElement.getValue().getClass();
        if (isPrimitive(keyType) && isPrimitive(valueType)) {
            entrySet.forEach(entry -> currentElement.appendChild(formatter.createFormatterItem(entry.getKey().toString(), entry.getValue().toString())));
        } else if (isPrimitive(keyType) && !isPrimitive(valueType)) {
            for (Map.Entry<? ,?> entry : entrySet) {
                IFormatterItem loopElement = formatter.createFormatterItem(entry.getKey().toString());
                innerSerialize(entry.getValue(), loopElement);
                currentElement.appendChild(loopElement);
            }
        }
    }

    private void serializeObject(final Object obj, final IFormatterItem currentElement, Class<?> currentClass) {
        List<Field> fields = getFilteredFields(currentClass);
        for (Field field : fields) {
            try {
                String tagName = getFieldTagName(field);
                Object value = field.get(obj);
                IFormatterItem element = formatter.createFormatterItem(tagName);
                innerSerialize(value, element);
                currentElement.appendChild(element);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public <T> T deserialize(String text, Class<T> clazz) {
        formatter.parse(text);
        T deserializedObj = null;

        try {
                deserializedObj = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        if (deserializedObj == null) {
            return null;
        }
        IFormatterItem rootItem = formatter.getRootFormatterItem();
        Optional<IFormatterItem> firstChild = rootItem.getChildren().stream().findFirst();
        if (firstChild.isPresent() && firstChild.get().getName().equals(getClassTagName(clazz))) {
            rootItem = firstChild.get();
        }

        innerDeserialize(deserializedObj, rootItem);
        return deserializedObj;
    }


    protected void innerDeserialize(final Object obj, final IFormatterItem currentElement) {
        Class<?> currentClass = obj.getClass();
        String classTagName = getClassTagName(currentClass);
        List<Field> fields = getFilteredFields(currentClass);
        List<IFormatterItem> children = currentElement.getChildren();


        for (Field field : fields) {
            String tagName = getFieldTagName(field);
            try {

                Class<?> type = field.getType();
                if (String.class.isAssignableFrom(type)
                        || type.isPrimitive()
                        || BigDecimal.class.isAssignableFrom(type)) {

                    IFormatterItem element = getElementByName(tagName, children);
                    if (element != null) {
                        String value = element.getValue();
                        if (String.class.isAssignableFrom(type)) {
                            field.set(obj, value);
                        } else if (BigDecimal.class.isAssignableFrom(type)) {
                            field.set(obj, new BigDecimal(value));
                        }
                    }

                } else if (type.isAssignableFrom(List.class)) {
                    List<Object> list = safelyCreateObject(type, ArrayList.class);
                    ParameterizedType genericTypes = (ParameterizedType) field.getGenericType();
                    Class<?> genericType = (Class<?>) genericTypes.getActualTypeArguments()[0];
                    List<IFormatterItem> items = getNestedItems(field, children, classTagName, fields.size());

                    String genericClassName = field.getName();
                    Type t = field.getGenericType();
                    if (t instanceof ParameterizedType) {
                        ParameterizedType pType = (ParameterizedType) t;
                        genericClassName = getClassTagName((Class) pType.getActualTypeArguments()[0]);
                    }
                    if(isPrimitive(genericType)) {
                        items.forEach(item -> list.add(item.getValue()));
                    } else {
                        for (IFormatterItem child : items) {
                            if (child.getName().equals(field.getName()) ||
                                    child.getName().equals(genericClassName) ||
                                    (fields.size() == 1 && child.getName().equals(classTagName))
                            ) {
                                Object instance = null;
                                try {
                                    instance = genericType.getDeclaredConstructor().newInstance();
                                    innerDeserialize(instance, child);
                                    list.add(instance);
                                } catch (InstantiationException | NoSuchMethodException | InvocationTargetException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    field.set(obj, list);
                } else if (type.isAssignableFrom(Map.class)) {
                    Map<Object, Object> map = safelyCreateObject(type, HashMap.class);
                    List<IFormatterItem> items = getNestedItems(field, children, classTagName, fields.size());
                    items.forEach(item -> map.put(item.getName(), item.getValue()));
                    field.set(obj, map);
                } else {
                    IFormatterItem element = getElementByName(tagName, children);
                    if (element != null) {
                        try {
                            Object item = type.newInstance();
                            innerDeserialize(item, element);
                            field.set(obj, item);
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        }
                    }
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public Map<String, Object> deserialize(String text) {
        formatter.parse(text);
        Map<String, Object> map = new HashMap<>();
        innerDeserializeWithoutClass(formatter.getRootFormatterItem(), map);
        return map;
    }

    private void innerDeserializeWithoutClass(final IFormatterItem element, Map<String, Object> map) {
        for (IFormatterItem item : element.getChildren()) {
            if (item.isArray()) {
                ArrayList<Map<String, Object>> currentMapList = new ArrayList<>();
                map.put(item.getName(), currentMapList);
                for (IFormatterItem child : item.getChildren()) {
                    Map<String, Object> currentMap = new HashMap<>();
                    innerDeserializeWithoutClass(child, currentMap);
                    currentMapList.add(currentMap);
                }

            } else if (item.getChildren().size() > 0) {
                Map<String, Object> currentMap = new HashMap<>();
                innerDeserializeWithoutClass(item, currentMap);
                map.put(item.getName(), currentMap);
            } else {
                map.put(item.getName(), item.getValue());
            }
        }
    }


    private String getFieldTagName(Field field) {
        return field.getName();
    }

    private List<Field> getFilteredFields(Class currentClass) {
        List<Field> fields = new LinkedList<>();
        for (Field field : currentClass.getDeclaredFields()) {
            if (field.isAccessible() || Modifier.isProtected(field.getModifiers())) {
                field.setAccessible(true);
                fields.add(field);
            }
        }
        return fields;
    }

    private String getClassTagName(Class currentClass) {
        return currentClass.getSimpleName();
    }

    private IFormatterItem getElementByName(String name, List<IFormatterItem> list) {
        for (IFormatterItem item : list) {
            if (item.getName().equals(name)) {
                return item;
            }
        }
        return null;
    }

    private boolean isSameNameElementsInCollection(String name, List<IFormatterItem> list) {
        if (list.size() == 0) {
            return false;
        }
        for (IFormatterItem item : list) {
            if (!item.getName().equals(name)) {
                return false;
            }
        }
        return true;
    }

    private List<IFormatterItem> getNestedItems(Field field,
                                                List<IFormatterItem> children,
                                                String classTagName,
                                                int fieldsAmount) {
        List<IFormatterItem> items = new ArrayList<>();
        IFormatterItem element = null;

        if (isSameNameElementsInCollection(field.getName(), children)) {
            items = children;
        } else if (fieldsAmount == 1 && children.get(0).getName().equals(classTagName)) {
            element = getElementByName(classTagName, children);
        } else {
            element = getElementByName(field.getName(), children);
        }

        if (items.isEmpty() && element != null) {
            items = element.getChildren();
        }
        return items;
    }

    private <T> T safelyCreateObject(Class<?> type_one, Class<?> type_two) {
        T collection = null;
        try {
            collection = (T) type_one.getDeclaredConstructor().newInstance();
        } catch (InstantiationException |
                IllegalAccessException |
                NoSuchMethodException |
                InvocationTargetException e) {
            try {
                collection = (T) type_two.getDeclaredConstructor().newInstance();
            } catch (InstantiationException |
                    IllegalAccessException |
                    NoSuchMethodException |
                    InvocationTargetException ex) {
                ex.printStackTrace();
            }
        }
        return collection;
    }

    private boolean isPrimitive(Class<?> type) {
        return (String.class.isAssignableFrom(type) ||
                type.isPrimitive() ||
                BigDecimal.class.isAssignableFrom(type) ||
                Number.class.isAssignableFrom(type));
    }
}
