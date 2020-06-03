package adda.settings.formatters;

public interface IFormatter {
    IFormatterItem createFormatterItem(String name, String value);

    IFormatterItem createFormatterItem(String name);

    IFormatterItem getRootFormatterItem();

    void parse(String str);

    void init();

    String getFormattedText();

}
