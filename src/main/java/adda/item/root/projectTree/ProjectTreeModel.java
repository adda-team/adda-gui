package adda.item.root.projectTree;

import adda.Context;
import adda.base.boxes.IBox;
import adda.base.models.ModelBase;
import adda.settings.ProjectSetting;
import adda.settings.Setting;
import adda.settings.SettingsManager;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.regex.*;
//

import adda.utils.OsUtils;
import adda.utils.StringHelper;
import io.methvin.watcher.DirectoryChangeEvent;
import io.methvin.watcher.DirectoryWatcher;
import org.reflections.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.NOPLogger;

public class ProjectTreeModel extends ModelBase implements TreeModel, Serializable, Cloneable {

    public static final String REFRESH = "refresh";
    //public static final int DIRECTORY_LISTENER_MASK = JNotify.FILE_ANY;
    public static final String SELECTED_PATH_FIELD_NAME = "selectedPath";
    public static final String FILE_MODIFIED_FIELD_NAME = "FILE_MODIFIED";
    protected Map<ProjectTreeNode, Integer> directoryListenerIds = new HashMap<>();
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
            node.id = projectSetting.getPath();
            node.name = projectSetting.getName();
            node.desc = String.format("<HTML><b>%s</b><br><small>%s</small></HTML>", projectSetting.getName(), projectSetting.getPath());
            node.folder = projectSetting.getPath();
            node.isPath = true;
            node.isProject = true;
            starDirtWatch(projectSetting.getPath());
            rootChildren.add(node);
        }

        map.put(root.id, rootChildren);
        timer.start();
    }

    private void starDirtWatch(String path) {
        try {
            DirectoryWatcher.builder()
                    .path(new File(path).toPath()) // or use paths(directoriesToWatch)
                    .listener(event -> {
                        isChanged = true;
                        if (event.eventType() == DirectoryChangeEvent.EventType.MODIFY) {
                            SwingUtilities.invokeLater(() -> notifyObservers(FILE_MODIFIED_FIELD_NAME, event.path().toString()));
                        }
                    })
                    .fileHashing(true)
                    .logger(NOPLogger.NOP_LOGGER) // defaults to LoggerFactory.getLogger(DirectoryWatcher.class)
                    .build()
                    .watchAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }


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
                if (file.getName().equals(".DS_Store")) {
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
        if ((this.selectedPath != null && !this.selectedPath.equals(selectedPath))
                || (this.selectedPath == null && selectedPath != null)) {
            this.selectedPath = selectedPath;
            notifyObservers(SELECTED_PATH_FIELD_NAME, selectedPath);
        }
    }

    public void forceNotifySelectedPath() {
        notifyObservers(SELECTED_PATH_FIELD_NAME, selectedPath);
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
        if (StringHelper.isEmptyOrWhitespaces(newItemModel.getDisplayName())) {
            node.name = StringHelper.toFolderName(newItemModel.getDisplayName());
        } else {
            node.name = newItemModel.getDisplayName();
        }
        String projectFolderName = StringHelper.toFolderName(newItemModel.getDisplayName());
        String folder = newItemModel.getDirectory() + File.separator + projectFolderName;

        File file = new File(folder);
        if (!file.exists()) {
            if (!file.mkdir()) {
                JOptionPane.showMessageDialog(null, "<html>Can't create a folder <b>" + folder + "</b></html>");
                return;
            }
        } else {
            folder = folder + "_" + StringHelper.toFolderName("");
            file = new File(folder);
            if (!file.mkdir()) {
                JOptionPane.showMessageDialog(null, "<html>Can't create a folder <b>" + folder + "</b></html>");
                return;
            }
        }

        node.setId(folder);
        newItemModel.setDirectory(folder);

        node.setFolder(folder);
        node.desc = String.format("<HTML><b>%s</b><br><small>%s</small></HTML>", newItemModel.getDisplayName(), newItemModel.getDirectory());
        node.isPath = true;
        node.isProject = true;
        map.get(root.id).add(node);
        Setting setting = SettingsManager.getSettings();
        ProjectSetting projectSetting = new ProjectSetting();
        projectSetting.setName(node.name);
        projectSetting.setPath(folder);
        setting.getProjects().add(projectSetting);
        SettingsManager.saveSettings(setting);
        starDirtWatch(folder);

        javax.swing.SwingUtilities.invokeLater(() -> {
            reloadForce();
            setSelectedPath(node);
        });
    }

    public void showNewProjectDialog() {
        ProjectTreeNewItemModel model = new ProjectTreeNewItemModel();
        model.setDisplayName("New project");
        model.setFolderName("new-project");
        model.setDirectory(OsUtils.getDefaultDirectory());

        ProjectTreeNewItemDialog dialog = new ProjectTreeNewItemDialog(model);
        dialog.pack();
        dialog.setVisible(true);
        if (dialog.isOkPressed()) {
            addProject(model);
        }
    }


    private final javax.swing.Timer timer = new Timer(1000, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (isChanged) {
                javax.swing.SwingUtilities.invokeLater(() -> reload());
                isChanged = false;
            }
        }
    });


    protected volatile boolean isChanged;


    public void reload() {
        if (enableAutoReload) {
            reloadForce();
        }
    }

    public void reloadForce() {
        notifyObservers(REFRESH, true);
    }


}
