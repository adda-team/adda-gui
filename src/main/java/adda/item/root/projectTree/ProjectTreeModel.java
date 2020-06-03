package adda.item.root.projectTree;

import adda.base.models.ModelBase;
import adda.settings.ProjectSetting;
import adda.settings.Setting;
import adda.settings.SettingsManager;

import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectTreeModel extends ModelBase implements TreeModel, Serializable, Cloneable {

    private String selectedPath;
    protected EventListenerList listeners;
    private Map<String, List<ProjectTreeNode>> map;
    private ProjectTreeNode root;
    private static final String ROOT_NAME = "Local computer";
    private static final String ROOT_ID = "local_computer";

    public ProjectTreeModel() {
        this.root = new ProjectTreeNode();
        root.id = ROOT_ID;
        root.name = ROOT_NAME;
        root.isPath = false;

        this.listeners = new EventListenerList();

        this.map = new HashMap<>();

        ArrayList<ProjectTreeNode> rootChildren = new ArrayList<>();
        Setting settings = SettingsManager.getSettings();
        for (ProjectSetting projectSetting : settings.getProjects()) {
            ProjectTreeNode node = new ProjectTreeNode();
//            node.id = String.join("_", projectSetting.getName(), projectSetting.getPath());
            node.id = projectSetting.getPath();
            node.name = String.format("%s (%s)", projectSetting.getName(), projectSetting.getPath());
            node.isPath = true;
            rootChildren.add(node);
        }

        map.put(root.id, rootChildren);
    }

    @Override
    public Object getRoot() {
        return root;
    }

    @Override
    public Object getChild(Object parent, int index) {
        return getChildren((ProjectTreeNode) parent).get(index);
    }

    @Override
    public int getChildCount(Object o) {
        List<ProjectTreeNode> children = getChildren((ProjectTreeNode) o);

        if (children == null)
            return 0;

        return children.size();
    }

    protected List<ProjectTreeNode> getChildren(ProjectTreeNode node) {
        Object value = map.get(node.id);

        List<ProjectTreeNode> children = (List) value;

        if (!ROOT_ID.equals(node.id) && node.isPath) {
            Map<String, Boolean> info = getPathInfo(node.id);

            if (info != null) {
                children = new ArrayList<>(info.size());
                for (Map.Entry<String, Boolean> entry : info.entrySet()) {
                    ProjectTreeNode d = new ProjectTreeNode();
                    d.id = entry.getKey();
                    d.name = entry.getKey();
                    d.isPath = entry.getValue();
                    children.add(d);
                }
            } else
                children = new ArrayList<>(0);

            map.put(node.id, children);
        }

        return children;
    }

    private Map<String, Boolean> getPathInfo(String path) {

        //todo replace with find real children in path
        return new HashMap<String, Boolean>() {{
            put("path_to_file1", false);
            put("path_to_file2", false);
            put("path_to_folder", true);
        }};
    }

    @Override
    public boolean isLeaf(Object o) {
        boolean isTreePoint = ROOT_ID.equals(((ProjectTreeNode) o).id) || ((ProjectTreeNode) o).isPath;
        return !isTreePoint;
    }

    @Override
    public void valueForPathChanged(TreePath treePath, Object o) {

    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        return getChildren((ProjectTreeNode) parent).indexOf(child);
    }

    public void addTreeModelListener(TreeModelListener l) {
        listeners.add(TreeModelListener.class, l);
    }

    public void removeTreeModelListener(TreeModelListener l) {
        listeners.remove(TreeModelListener.class, l);
    }


    public String getSelectedPath() {
        return selectedPath;
    }

    public void setSelectedPath(String selectedPath) {
        if((this.selectedPath != null && !this.selectedPath.equals(selectedPath))
                || (this.selectedPath == null && selectedPath != null)) {
            this.selectedPath = selectedPath;
            notifyObservers("selectedPath", selectedPath);
        }

    }

    public Object clone() {
        try {
            ProjectTreeModel clone = (ProjectTreeModel) super.clone();
            clone.listeners = new EventListenerList();
            clone.map = new HashMap<>(map);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }
}
