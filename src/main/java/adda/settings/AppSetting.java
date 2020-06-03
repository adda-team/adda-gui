package adda.settings;

import java.util.Map;

public class AppSetting {
    protected String language;
    protected Map<String, Object> defaultAddaValues;
    protected String defaultProjectName;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Map<String, Object> getDefaultAddaValues() { return defaultAddaValues; }

    public void setDefaultAddaValues(Map<String, Object> defaultAddaValues) {
        this.defaultAddaValues = defaultAddaValues;
    }

    public String getDefaultProjectName() {
        return defaultProjectName;
    }

    public void setDefaultProjectName(String defaultProjectName) {
        this.defaultProjectName = defaultProjectName;
    }
}
