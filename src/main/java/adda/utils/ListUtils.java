package adda.utils;

import adda.base.annotation.DisplayString;
import adda.application.controls.ComboBoxItem;

import java.lang.reflect.Field;
import java.util.*;

public class ListUtils {
    public static Vector<ComboBoxItem> getDataSource(Class<?> enumClass) {
        Map<Object, String> map = getEnumDisplayValues(enumClass);
        Vector<ComboBoxItem> dataSource = new Vector<>();
        map.entrySet().forEach(innerEntry ->{
            dataSource.add(new ComboBoxItem(innerEntry.getKey(), innerEntry.getValue()));
        });
        return dataSource;

    }

    private static final Map<Class<?>, Map<Object, String>> ENUM_MAP = new HashMap<>();

    public static Map<Object, String> getEnumDisplayValues(Class<?> enumClass) {

        if (ENUM_MAP.containsKey(enumClass)) {
            return ENUM_MAP.get(enumClass);
        }

        Map<Object, String> map = new LinkedHashMap<>();//LinkedHashMap instead of HashMap because  we need the same sequence as they were inserted
        for (Object enumConstant : enumClass.getEnumConstants()) {
            Field declaredField = null;
            try {
                declaredField = enumClass.getDeclaredField(enumConstant.toString());
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            if (declaredField != null) {//should never be null
                DisplayString annotation = declaredField.getAnnotation(DisplayString.class);
                if (annotation != null &&  !"".equals(annotation.value() + "")) {
                    map.put(enumConstant, annotation.value());//todo localization
                } else {
                    map.put(enumConstant, enumConstant.toString());//todo localization
                }
            }
        }
        ENUM_MAP.put(enumClass, map);
        return map;
    }

    public static Map<String, String> getListTranslatedValues(List list) {
        Map<String, String> map = new HashMap<>();
        list.forEach(item -> {
            if (item != null && map.containsKey(item.toString())) {
                map.put(item.toString(), item.toString());//todo localization
            }
        });
        return map;
    }


}
