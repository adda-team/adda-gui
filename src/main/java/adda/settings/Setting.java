package adda.settings;

import java.util.List;

public class Setting {
    public AppSetting getAppSetting() {
        return appSetting;
    }

    public void setAppSetting(AppSetting appSetting) {
        this.appSetting = appSetting;
    }

    public List<ProjectSetting> getProjects() {
        return projects;
    }

    public void setProjects(List<ProjectSetting> projects) {
        this.projects = projects;
    }

    protected AppSetting appSetting;
    protected List<ProjectSetting> projects;


}
