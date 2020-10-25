package adda.item.root.workspace;

import adda.base.controllers.ControllerBase;
import adda.application.controls.CloseTabEvent;
import adda.application.controls.ICloseTabListener;
import adda.application.controls.JTabbedPaneClosable;
import adda.item.root.projectTree.ProjectTreeNode;

import javax.swing.event.*;
import java.awt.*;

public class WorkspaceController extends ControllerBase {

    @Override
    protected void createAndBindListenersFromView() {
        if(view == null) {
            return;
        }
        for (Component component : view.getRootComponent().getComponents()) {
            if (component.getClass().equals(JTabbedPaneClosable.class)) {
                JTabbedPaneClosable pane = (JTabbedPaneClosable) component;
                pane.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        WorkspaceModel workspaceModel = (WorkspaceModel) WorkspaceController.this.model;
                        Object selectedTabAssociatedObject = pane.getSelectedTabAssociatedObject();
                        if (selectedTabAssociatedObject != null && selectedTabAssociatedObject instanceof ProjectTreeNode) {
                            workspaceModel.selectBoxByPath((ProjectTreeNode) selectedTabAssociatedObject);
                        } else {
                            workspaceModel.setFocusedBox(null);
                        }
                    }
                });
                pane.addCloseTabListener(new ICloseTabListener() {
                    @Override
                    public void tabClosed(CloseTabEvent closeTabEvent) {
                        Object associatedObject = closeTabEvent.getAssotiatedObject();
                        if (associatedObject != null && associatedObject instanceof String) {
                            ((WorkspaceModel)model).removeBoxByPath((ProjectTreeNode)associatedObject);
                        }
                    }
                });

            }
        }
    }
}
