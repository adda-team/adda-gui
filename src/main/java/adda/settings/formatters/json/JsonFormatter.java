package adda.settings.formatters.json;

import adda.settings.formatters.IFormatter;
import adda.settings.formatters.IFormatterItem;
import adda.settings.formatters.plaintext.PlainTextFormatterItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class JsonFormatter implements IFormatter {

    private IFormatterItem rootItem = new JsonFormatterItem("", "");
    private final String TAB = "    ";


    @Override
    public IFormatterItem createFormatterItem(String name, String value) {
        return new JsonFormatterItem(name, value);
    }

    @Override
    public IFormatterItem createFormatterItem(String name) {
        return new JsonFormatterItem(name, null);
    }

    @Override
    public IFormatterItem getRootFormatterItem() {
        return rootItem;
    }

    @Override
    public void parse(String str) {
        rootItem = new PlainTextFormatterItem("", "");
        try {
            JSONObject json = new JSONObject(str);
            innerParse(getRootFormatterItem(), json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void innerParse(IFormatterItem item, JSONObject obj) {
        // We need to know keys of Jsonobject
        Iterator iterator = obj.keys();
        String key = null;
        while (iterator.hasNext()) {
            key = (String) iterator.next();
            // if object is just string we change value in key
            if ((obj.optJSONArray(key) == null) && (obj.optJSONObject(key) == null)) {
                IFormatterItem currentItem = createFormatterItem(key, obj.getString(key));
                item.appendChild(currentItem);
            }

            // if it's jsonobject
            if (obj.optJSONObject(key) != null) {
                IFormatterItem currentItem = createFormatterItem(key);
                item.appendChild(currentItem);
                innerParse(currentItem, obj.getJSONObject(key));
            }

            // if it's jsonarray
            if (obj.optJSONArray(key) != null) {
                JSONArray jArray = obj.getJSONArray(key);
                IFormatterItem currentItem = createFormatterItem(key);
                currentItem.setArrayAttribute(true);
                item.appendChild(currentItem);
                for (int i = 0; i < jArray.length(); i++) {
                    Class<?> currentType = jArray.get(i).getClass();
                    if (isPrimitive(currentType)) {
                        IFormatterItem loopItem = createFormatterItem(currentType.getSimpleName(), jArray.get(i).toString());
                        currentItem.appendChild(loopItem);
                    } else {
                        IFormatterItem loopItem = createFormatterItem(key);
                        currentItem.appendChild(loopItem);
                        innerParse(loopItem, jArray.getJSONObject(i));
                    }
                }
            }
        }
    }

    @Override
    public void init() {

        rootItem = new PlainTextFormatterItem("", "");
    }

    @Override
    public String getFormattedText() {
        StringBuilder builder = new StringBuilder();
        processFormattedTextInner(rootItem.getChildren(), builder, 0, false);
        return builder.toString();
    }

    private void processFormattedTextInner(List<IFormatterItem> items, StringBuilder builder, int level, boolean isArray) {
        StringBuilder tabs = new StringBuilder();
        tabs.append(String.join("", Collections.nCopies(Math.max(0, level), TAB)));


        if (isArray) {
            builder.append(tabs);
            builder.append(TAB);
            builder.append("[");
            level = level + 1;
        } else {
            builder.append(tabs);
            builder.append("{\n");
        }

        for (int i = 0; i < items.size(); i++) {
            IFormatterItem item = items.get(i);
            builder.append(tabs);
            builder.append(TAB);
            if (!isArray) {
                builder.append("\"");
                builder.append(item.getName());
                builder.append("\"");

                builder.append(" : ");

                if ((item.getValue() != null && !item.getValue().equals(""))) {
                    builder.append("\"");
                    builder.append(item.getValue());
                    builder.append("\"");
                }
            }
            if (item.getChildren().size() > 0) {
                builder.append("\n");
                processFormattedTextInner(item.getChildren(), builder, level + 1 + (item.isArray() ? 1 : 0), item.isArray());
            }
            else if (isArray) {
                if ((item.getValue() != null && !item.getValue().equals(""))) {
                    if (i == 0) {
                        builder.append('\n');
                        builder.append(tabs);
                        builder.append(TAB);
                    }
                    builder.append(TAB);
                    builder.append("\"");
                    builder.append(item.getValue());
                    builder.append("\"");
                }
            }

            if ((items.size() - 1) != i) {
                builder.append(",");
            }
            builder.append("\n");
        }
        if (isArray) {
            builder.append(tabs);
            builder.append(TAB);
            builder.append("]");
        } else {
            builder.append(tabs);
            builder.append("}");
        }

    }

    private boolean isPrimitive(Class<?> type) {
        return (String.class.isAssignableFrom(type) || type.isPrimitive() || BigDecimal.class.isAssignableFrom(type));
    }
}
