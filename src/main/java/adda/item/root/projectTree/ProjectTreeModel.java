package adda.item.root.projectTree;

import adda.Context;
import adda.base.boxes.IBox;
import adda.base.models.ModelBase;
import adda.settings.ProjectSetting;
import adda.settings.Setting;
import adda.settings.SettingsManager;

import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

import net.contentobjects.jnotify.JNotify;
import net.contentobjects.jnotify.JNotifyException;
import net.contentobjects.jnotify.JNotifyListener;

public class ProjectTreeModel extends ModelBase implements TreeModel, Serializable, Cloneable {

    public static final String REFRESH = "refresh";
    public static final int DIRECTORY_LISTENER_MASK = JNotify.FILE_CREATED | JNotify.FILE_DELETED | JNotify.FILE_RENAMED;
    public static final String SELECTED_PATH_FIELD_NAME = "selectedPath";
    protected Map<ProjectTreeNode, Integer> directoryListenerIds  = new HashMap<>();
    private ProjectTreeNode selectedPath;
    protected EventListenerList listeners;
    private Map<String, List<ProjectTreeNode>> map;
    private ProjectTreeNode root;
    private static final String ROOT_NAME = "This PC";
    private static final String ROOT_ID = "local_computer";

    public volatile boolean enableAutoReload = true;

    public ProjectTreeModel() {
        this.root = new ProjectTreeNode();
        root.id = ROOT_ID;
        root.name = ROOT_NAME;
        root.desc = ROOT_NAME;
        root.isPath = false;



        this.listeners = new EventListenerList();

        this.map = new HashMap<>();

        ArrayList<ProjectTreeNode> rootChildren = new ArrayList<>();
        Setting settings = SettingsManager.getSettings();
        for (ProjectSetting projectSetting : settings.getProjects()) {
            ProjectTreeNode node = new ProjectTreeNode();
//            node.id = String.join("_", projectSetting.getName(), projectSetting.getPath());
            node.id = projectSetting.getPath();
            node.name = projectSetting.getName();
            node.desc = String.format("<HTML><b>%s</b><br><small>%s</small></HTML>", projectSetting.getName(), projectSetting.getPath());
            node.folder = projectSetting.getPath();
            node.isPath = true;
            node.isProject = true;
            JNotifyListener directoryListener = new JNotifyListener() {
                @Override
                public void fileCreated(int wd, String rootPath, String name) {
                    reload();
                }

                @Override
                public void fileDeleted(int wd, String rootPath, String name) {
                    reload();
                }

                @Override
                public void fileModified(int wd, String rootPath, String name) {

                }

                @Override
                public void fileRenamed(int wd, String rootPath, String oldName, String newName) {
                    reload();
                }
            };

            try {
                int watchID = JNotify.addWatch(projectSetting.getPath(), DIRECTORY_LISTENER_MASK, false, directoryListener);
                directoryListenerIds.put(node, watchID);
            } catch (JNotifyException e) {
                e.printStackTrace();
            }

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
            Map<String, Boolean> info = getPathInfo(node.folder);

            if (info != null) {
                children = new ArrayList<>(info.size());
                for (Map.Entry<String, Boolean> entry : info.entrySet()) {
                    ProjectTreeNode d = new ProjectTreeNode();
                    d.id = entry.getKey();
                    d.name = new File(entry.getKey()).getName();
                    d.folder = entry.getKey();
                    d.desc = d.name;
                    d.isPath = entry.getValue();
                    d.isProject = false;
                    children.add(d);
                }
            } else
                children = new ArrayList<>(0);

            map.put(node.id, children);
        }

        return children;
    }

    private Map<String, Boolean> getPathInfo(String path) {
        Map<String, Boolean> map = new LinkedHashMap<String, Boolean>();

        try {
            File[] files = (new File(path)).listFiles();
            Arrays.sort(files);
            for (File file : files) {
                if (file.getName().equals("adda_gui_state.data")) {
                    continue;
                }
                map.put(file.getCanonicalPath(), file.isDirectory());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //todo replace with find real children in path
//        return new HashMap<String, Boolean>() {{
//            put("path_to_file1", false);
//            put("path_to_file2", false);
//            put("path_to_folder", true);
//        }};
        return map;
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


    public ProjectTreeNode getSelectedPath() {
        return selectedPath;
    }

    public void setSelectedPath(ProjectTreeNode selectedPath) {
        if((this.selectedPath != null && !this.selectedPath.equals(selectedPath))
                || (this.selectedPath == null && selectedPath != null)) {
            this.selectedPath = selectedPath;
            notifyObservers(SELECTED_PATH_FIELD_NAME, selectedPath);
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

    public void addProject(ProjectTreeNewItemModel newItemModel) {
        ProjectTreeNode node = new ProjectTreeNode();
//            node.id = String.join("_", projectSetting.getName(), projectSetting.getPath());
        node.id = newItemModel.getDirectory();
        node.name = newItemModel.getDisplayName();
        node.desc = String.format("<HTML><b>%s</b><br><small>%s</small></HTML>", newItemModel.getDisplayName(), newItemModel.getDirectory());
        node.folder = newItemModel.getDirectory();
        node.isPath = true;
        map.get(root.id).add(node);

        reload();
    }

    public void showNewProjectDialog() {
        ProjectTreeNewItemModel model = new ProjectTreeNewItemModel();
        model.setDisplayName("New project");
        model.setFolderName("new-project");
        model.setDirectory(System.getProperty("user.dir"));

        ProjectTreeNewItemDialog dialog = new ProjectTreeNewItemDialog(model);
        dialog.pack();
        dialog.setVisible(true);
        if (dialog.isOkPressed()) {
            addProject(model);
        }
    }

    public void reload() {
        if (enableAutoReload) {
            notifyObservers(REFRESH, true);
        }
    }

    public void reloadForce() {
        notifyObservers(REFRESH, true);
    }




}
