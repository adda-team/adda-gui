package adda.settings.formatters;

import java.util.List;

public interface IFormatterItem {
    void appendChild(IFormatterItem child);

    List<IFormatterItem> getChildren();

    String getName();

    String getValue();

    void setValue(String value);

    IFormatterItem getParent();

    void setArrayAttribute(boolean isArray);

    boolean isArray();

}
