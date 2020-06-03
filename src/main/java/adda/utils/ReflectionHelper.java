package adda.utils;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public class ReflectionHelper {
    protected static final Map<Class<?>, Class<?>> PACKED_TO_PRIMITIVE =
            Stream.of(boolean.class, byte.class, char.class, double.class, float.class, int.class, long.class, short.class)
                    .collect(toMap(clazz -> ((Object) Array.get(Array.newInstance(clazz, 1), 0)).getClass(), clazz -> (Class<?>) clazz));

    private static final Map<Class<?>, Method[]> METHODS_CACHE = new ConcurrentHashMap<>();

    public static <T> void setPropertyValue(Object model, String propertyName, T var) {
        if (model == null) return;

        Method[] methods = getMethods(model);

        String setterName = propertyName.startsWith("is")
                ? propertyName.replaceFirst("is", "set")
                : ("set" + StringHelper.capitalize(propertyName));

        Class<?> clazz = ((Object) var).getClass();

        boolean isPrimitive = PACKED_TO_PRIMITIVE.containsKey(clazz);
        boolean isEnum = var.getClass().isEnum();

        Optional<Method> setter =
                Arrays.stream(methods)
                        .filter(method -> method.getName().equals(setterName))
                        .filter(method -> method.getParameterTypes().length == 1)
                        .filter(method -> method.getParameterTypes()[0] == clazz
                                || (isEnum && method.getParameterTypes()[0] == Enum.class)
                                || (isPrimitive && method.getParameterTypes()[0] == PACKED_TO_PRIMITIVE.get(clazz)))
                        .findFirst();
        if (!setter.isPresent()) {
            return;
        }

        try {
            setter.get().invoke(model, var);
        } catch (IllegalAccessException | InvocationTargetException e) {
            // TODO logging e.printStackTrace();
            // rethrow throw e;
            System.out.println(e.fillInStackTrace());
        }
    }

    public static Object getPropertyValue(Object model, String propertyName) {
        Object val = null;
        try {

            Method[] methods = getMethods(model);

            String getter = "get" + StringHelper.capitalize(propertyName);

            if (Arrays.stream(methods).anyMatch(method -> method.getName().equals(getter))) {
                Method method = model.getClass().getMethod(getter);
                val = method.invoke(model);
            } else if (Arrays.stream(methods).anyMatch(method -> method.getName().equals(propertyName))) {
                Method method = model.getClass().getMethod(propertyName);
                val = method.invoke(model);
            } else {
                if (propertyName.startsWith("is")) { //todo check 'key contains value'
                    String boolGetter = propertyName.replaceFirst("is", "get");
                    if (Arrays.stream(methods).anyMatch(method -> method.getName().equals(boolGetter))) {
                        Method method = model.getClass().getMethod(boolGetter);
                        val = method.invoke(model);
                    }
                }
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            // TODO logging e.printStackTrace();
            // rethrow throw e;
            System.out.println(e.fillInStackTrace());
        }
        return val;
        // todo check cast
    }

    public static Method[] getMethods(Object model) {
        Method[] methods;
        if (METHODS_CACHE.containsKey(model.getClass())) {
            methods = METHODS_CACHE.get(model.getClass());
        } else {
            methods = model.getClass().getMethods();
            try {
                METHODS_CACHE.put(model.getClass(), methods);
            } catch (Exception ignored) {}

        }
        return methods;
    }

    public static void copy(Object from, Object to) {
        final Method[] fromMethods = getMethods(from);//from.getClass().getMethods();
        final Method[] toMethods = getMethods(to);//to.getClass().getMethods();
        for (Method method: fromMethods) {
            String methodName = method.getName();
            try {
                final boolean startsWithGet = methodName.startsWith("get");
                final boolean startsWithIs = methodName.startsWith("is");
                if (startsWithGet || startsWithIs) {
                    String setterName = methodName.replaceFirst(startsWithGet ? "get" : "is", "set");
                    Optional<Method> optionalSetter = Arrays.stream(toMethods).filter(x -> x.getName().equals(setterName)).findFirst();
                    if (optionalSetter.isPresent()) {
                        Method setter = optionalSetter.get();
                        if ((setter.getParameterTypes().length == 1) && (setter.getParameterTypes()[0] == method.getReturnType())) {
                            to.getClass().getMethod(setterName, method.getReturnType()).invoke(to, method.invoke(from));
                        }
                    }
                }
            } catch (NoSuchMethodException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
                // TODO: handle exception
                System.out.println(e.fillInStackTrace());
            }
        }
    }
}
