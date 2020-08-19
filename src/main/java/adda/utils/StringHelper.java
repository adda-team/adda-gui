package adda.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class StringHelper {

    private static DecimalFormat DECIMAL_FORMAT;
    static {
        DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols();
        formatSymbols.setDecimalSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("0.#", formatSymbols);
        decimalFormat.setMaximumFractionDigits(340);
        decimalFormat.setGroupingUsed(false);
        DECIMAL_FORMAT = decimalFormat;
    }

    public static String capitalize(String s) {
        if (s == null || s.length() < 1) {
            return s;
        }
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    public static boolean isEmpty(String s) {
        return s == null || "".equals("" + s);
    }

    private Map<Object, String> map = new HashMap<>();

    public static String toDisplayString(Enum enumValue) {
        if (enumValue == null) return null;
        return ListUtils.getEnumDisplayValues(enumValue.getClass()).get(enumValue);
    }

    public static String toDisplayString(String value) {

        return value;
    }

    public static String toDisplayString(Object value) {
        if (value == null) return null;
        if (value instanceof Enum) {
            return toDisplayString((Enum) value);
        }
        if (value instanceof String) {
            return toDisplayString((String) value);
        }
        if (value instanceof Double) {
            return DECIMAL_FORMAT.format(value);
        }
        return value.toString();
    }





}
