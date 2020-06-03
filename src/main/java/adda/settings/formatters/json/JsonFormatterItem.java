package adda.settings.formatters.json;

import adda.settings.formatters.plaintext.PlainTextFormatterItem;

public class JsonFormatterItem extends PlainTextFormatterItem {


    protected boolean isArray;

    public JsonFormatterItem(String name, String value) {
        super(name, value);
    }

    @Override
    public void setArrayAttribute(boolean isArray) {
        this.isArray = isArray;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean isArray() {
        return isArray;
    }

}
