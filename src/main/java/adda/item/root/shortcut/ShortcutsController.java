package adda.item.root.shortcut;

import adda.Context;
import adda.base.boxes.IBox;
import adda.base.controllers.ControllerBase;
import adda.base.events.IModelPropertyChangeEvent;
import adda.base.models.IModel;
import adda.base.models.IModelObserver;
import adda.item.root.projectArea.ProjectAreaBox;
import adda.item.root.projectArea.ProjectAreaModel;
import adda.item.root.projectTree.ProjectTreeNode;
import adda.item.root.workspace.WorkspaceModel;
import adda.settings.AppSetting;
import adda.settings.SettingsManager;
import adda.utils.StringHelper;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ShortcutsController extends ControllerBase {
    @Override
    protected void createAndBindListenersFromView() {
        if (view instanceof ShortcutsView) {
            ShortcutsView shortcutsView = ((ShortcutsView) view);
            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {

                    Context.getInstance().getWorkspaceModel().addObserver(new IModelObserver() {
                        @Override
                        public void modelPropertyChanged(IModel sender, IModelPropertyChangeEvent event) {
                            if (WorkspaceModel.FOCUSED_BOX_FIELD_NAME.equals(event.getPropertyName())) {
                                WorkspaceModel workspaceModel = (WorkspaceModel) sender;
                                ProjectTreeNode projectTreeNode = workspaceModel.getPathByBox(workspaceModel.getFocusedBox());
                                shortcutsView.runButton.setEnabled(projectTreeNode != null && projectTreeNode.isProject());
                            }
                        }
                    });
                }
            });


            shortcutsView.runButton.addActionListener(e -> {
                final AppSetting appSetting = SettingsManager.getSettings().getAppSetting();
                if (StringHelper.isEmpty(appSetting.getAddaExecSeq())) {
                    JOptionPane.showMessageDialog(null, "Executable ADDA path does`t configured");
                    SettingsManager.openSettingsDialog();
                    return;
                }

                IBox focusedBox = Context.getInstance().getWorkspaceModel().getFocusedBox();
                ProjectTreeNode projectTreeNode = Context.getInstance().getWorkspaceModel().getPathByBox(focusedBox);
                if (projectTreeNode.isProject() && focusedBox instanceof ProjectAreaBox) {

                    ProjectAreaModel projectAreaModel = ((ProjectAreaModel) focusedBox.getModel());
                    if (projectAreaModel.isRunning()) {
                        JOptionPane.showMessageDialog(null, "Already active, please close previous run");
                    } else {
                        Context.getInstance().getMainForm().setLoadingVisible(true);
                        javax.swing.SwingUtilities.invokeLater(() -> {
                            Date now = new Date();
                            SimpleDateFormat pattern = new SimpleDateFormat("MMddHHmmss");
                            String name = "run_" + pattern.format(now);
                            final String projectPath = projectTreeNode.getFolder();
                            String path = projectPath + "/" + name;

                            File file = new File(path);
                            Context.getInstance().getProjectTreeModel().enableAutoReload = false;

                            //deleteFolder(file);
                            boolean runCreated = true;
                            if (!file.exists()) {
                                if (!file.mkdir()) {
                                    //throw new FileNotFoundException("Directory " + firstProjectDir + "cannot be created");
                                    runCreated = false;
                                } else {
                                    File from = new File(projectPath + "/adda_gui_state.data");
                                    File to = new File(path + "/adda_gui_state.data");
                                    try {
                                        if (from.exists()) {
                                            Files.copy(from.toPath(), to.toPath(), StandardCopyOption.REPLACE_EXISTING);
                                        }
                                    } catch (IOException ex) {
                                        runCreated = false;
                                    }
                                    Context.getInstance().getProjectTreeModel().reloadForce();
                                }
                            }
                            Context.getInstance().getProjectTreeModel().enableAutoReload = true;

                            if (runCreated) {
                                ProjectTreeNode newRunTreeNode = new ProjectTreeNode();
                                newRunTreeNode.setId(path);
                                newRunTreeNode.setName(name);
                                newRunTreeNode.setFolder(path);
                                newRunTreeNode.setDesc(name);
                                newRunTreeNode.setPath(true);
                                newRunTreeNode.setProject(false);

                                Context.getInstance().getProjectTreeModel().setSelectedPath(newRunTreeNode);

                                javax.swing.SwingUtilities.invokeLater(new Runnable() {
                                    public void run() {
                                        IBox focusedBox = Context.getInstance().getWorkspaceModel().getFocusedBox();
                                        ProjectTreeNode projectTreeNode = Context.getInstance().getWorkspaceModel().getPathByBox(focusedBox);
                                        if (projectTreeNode.equals(newRunTreeNode) && focusedBox instanceof ProjectAreaBox) {
                                            ProjectAreaModel projectAreaModel = ((ProjectAreaModel) focusedBox.getModel());
                                            projectAreaModel.start();
                                        }
                                    }
                                });
                            } else {
                                javax.swing.SwingUtilities.invokeLater(() -> Context.getInstance().getMainForm().setLoadingVisible(false));

                                JOptionPane.showMessageDialog(null, "Directory " + path + "cannot be created or UI state cannot be moved");
                            }
                        });
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Selected tab does`t contain any runnable item");
                }
            });
        }
    }
}
