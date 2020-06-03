package adda.settings.serializer;

import adda.settings.formatters.IFormatter;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public abstract class Serializer implements ISerializer {
    protected IFormatter formatter;
    private static Random rand = new Random();

    public Serializer() {
    }

    public Serializer(IFormatter formatter) {
        this.formatter = formatter;
    }

    private static String getRandomString() {
        int length = rand.nextInt(20) + 5;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (rand.nextInt(10) % 2 == 0) {
                builder.append((char) ('A' + rand.nextInt(26)));
            } else {
                builder.append((char) ('a' + rand.nextInt(26)));
            }
        }
        return builder.toString();
    }

    public static <T> T generateRandomInitializedInstance(Class<T> clazz) throws IllegalAccessException, InstantiationException {
        Field[] fields = clazz.getDeclaredFields();
        T o = clazz.newInstance();
        for (Field field : fields) {
            Class<?> type = field.getType();
            field.setAccessible(true);
            if (String.class.isAssignableFrom(type)) {
                field.set(o, field.getName() + "-" + getRandomString());
            } else if (BigDecimal.class.isAssignableFrom(type)) {
                field.set(o, BigDecimal.valueOf(rand.nextDouble() * 500).setScale(2, RoundingMode.HALF_UP));
            } else if (Date.class.isAssignableFrom(type)) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                cal.add(Calendar.DATE, rand.nextInt(60) - 30);
                field.set(o, cal.getTime());
            } else if (Integer.class.isAssignableFrom(type)) {
                field.set(o, rand.nextInt());
            } else if (Double.class.isAssignableFrom(type)) {
                field.set(o, rand.nextDouble());
            } else if (!type.isPrimitive()) {
                field.set(o, generateRandomInitializedInstance(type));
            }
        }
        return o;
    }
}
