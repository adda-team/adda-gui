package adda.item.root.projectTree;

import java.util.Objects;

public class ProjectTreeNode {
    protected String id;
    protected String name;
    protected String desc;
    protected String folder;
    protected boolean isPath;
    protected boolean isProject;

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public void setPath(boolean path) {
        isPath = path;
    }

    public void setProject(boolean project) {
        isProject = project;
    }

    public boolean isProject() {
        return isProject;
    }

    public String getId() {
        return id;
    }

    public String getFolder() {
        return folder;
    }

    public String getName() {
        return name;
    }

    public boolean isPath() {
        return isPath;
    }

    public String getDesc() {
        return desc;
    }

    public String toString() {
        return desc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectTreeNode that = (ProjectTreeNode) o;
        return isPath == that.isPath &&
                isProject == that.isProject &&
                Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(desc, that.desc) &&
                Objects.equals(folder, that.folder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, desc, folder, isPath, isProject);
    }
}
