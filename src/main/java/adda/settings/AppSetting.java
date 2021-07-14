package adda.settings;

import java.util.Map;

public class AppSetting {
    protected String language;
    //protected Map<String, Object> defaultAddaValues;
    protected String defaultProjectName;
    protected String defaultProjectPath;
    protected String addaExecSeq;
    protected String addaExecMpi;
    protected String addaExecGpu;
    protected String gitPath;
    protected int fontSize;

    public String getGitPath() {
        return gitPath;
    }

    public void setGitPath(String gitPath) {
        this.gitPath = gitPath;
    }

    public String getDefaultProjectPath() {
        return defaultProjectPath;
    }

    public void setDefaultProjectPath(String defaultProjectPath) {
        this.defaultProjectPath = defaultProjectPath;
    }

    public String getAddaExecSeq() {
        return addaExecSeq;
    }

    public void setAddaExecSeq(String addaExecSeq) {
        this.addaExecSeq = addaExecSeq;
    }

    public String getAddaExecMpi() {
        return addaExecMpi;
    }

    public void setAddaExecMpi(String addaExecMpi) {
        this.addaExecMpi = addaExecMpi;
    }

    public String getAddaExecGpu() {
        return addaExecGpu;
    }

    public void setAddaExecGpu(String addaExecGpu) {
        this.addaExecGpu = addaExecGpu;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

//    public Map<String, Object> getDefaultAddaValues() { return defaultAddaValues; }
//
//    public void setDefaultAddaValues(Map<String, Object> defaultAddaValues) {
//        this.defaultAddaValues = defaultAddaValues;
//    }

    public String getDefaultProjectName() {
        return defaultProjectName;
    }

    public void setDefaultProjectName(String defaultProjectName) {
        this.defaultProjectName = defaultProjectName;
    }
}
