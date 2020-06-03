package adda.application.controls;

public class ComboBoxItem {
    private Object key;
    private String description;

    public ComboBoxItem(Object key, String description)
    {
        this.key = key;
        this.description = description;
    }

    public Object getKey()
    {
        return key;
    }

    public String getDescription()
    {
        return description;
    }

    public String toString()
    {
        return description;
    }
}
