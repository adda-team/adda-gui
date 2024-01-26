package adda.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static boolean isEmptyOrWhitespaces(String s) {
        return s == null || "".equals("" + s) || s.trim().isEmpty();
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


    private static final Pattern REMOVE_TAGS = Pattern.compile("<.+?>");

    public static String removeTags(String string) {
        if (string == null || string.length() == 0) {
            return string;
        }

        Matcher m = REMOVE_TAGS.matcher(string);
        return m.replaceAll("");
    }


    private static final Pattern TO_FOLDER_NAME = Pattern.compile("[\\/?%*:|\"<>\\s]+?");

    public static String toFolderName(String string) {
        if (string == null || string.length() == 0) {
            Date now = new Date();
            SimpleDateFormat pattern = new SimpleDateFormat("MM_dd_HH_mm_ss");
            return "folder_" + pattern.format(now);
        }

        Matcher m = TO_FOLDER_NAME.matcher(string);
        return m.replaceAll("_").toLowerCase();
    }




}
