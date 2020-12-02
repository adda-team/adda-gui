package adda.item.root.projectTree;

public class ProjectTreeNode {
    protected String id;
    protected String name;
    protected String desc;
    protected String folder;
    protected boolean isPath;
    protected boolean isProject;

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

    public String toString() {
        return desc;
    }
}
