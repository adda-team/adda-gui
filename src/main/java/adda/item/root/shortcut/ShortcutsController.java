package adda.item.root.shortcut;

import adda.Context;
import adda.base.boxes.IBox;
import adda.base.controllers.ControllerBase;
import adda.item.root.projectArea.ProjectAreaBox;
import adda.item.root.projectArea.ProjectAreaModel;

import javax.swing.*;

public class ShortcutsController extends ControllerBase {
    @Override
    protected void createAndBindListenersFromView() {
        if (view instanceof ShortcutsView) {
            ShortcutsView shortcutsView = ((ShortcutsView) view);
            shortcutsView.runButton.addActionListener(e -> {
                IBox focusedBox = Context.getInstance().getWorkspaceModel().getFocusedBox();
                if (focusedBox instanceof ProjectAreaBox) {
                    ProjectAreaModel projectAreaModel = ((ProjectAreaModel) focusedBox.getModel());
                    if (projectAreaModel.isRunning()) {
                        JOptionPane.showMessageDialog(null, "Already active, please close previous run");
                    } else {
                        projectAreaModel.start();
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Selected tab does`t contain any runnable item");
                }
            });
        }
    }
}
