package adda.settings.formatters.plaintext;

import adda.settings.formatters.IFormatterItem;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

public class PlainTextFormatterItem implements IFormatterItem {

    protected List<IFormatterItem> children = new ArrayList<>();
    protected IFormatterItem parent;
    protected String name;
    protected String value;

    public IFormatterItem getParent() {
        return parent;
    }

    public void setParent(IFormatterItem parent) {
        this.parent = parent;
    }

    public PlainTextFormatterItem(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public void appendChild(IFormatterItem child) {
        if (child instanceof PlainTextFormatterItem) {
            ((PlainTextFormatterItem) child).setParent(this);
            children.add(child);
        }
    }

    @Override
    public List<IFormatterItem> getChildren() {
        return children;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {

    }

    @Override
    public void setArrayAttribute(boolean isArray) {

    }

    @Override
    public boolean isArray() {
        return false;
    }


    public String toString() {
        return new Formatter().format("{name : %s ; value : %s}", name, value).toString();
    }
}
