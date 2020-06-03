package adda.item.root.workspace;

import adda.base.boxes.IBox;
import adda.base.events.IModelPropertyChangeEvent;
import adda.base.models.IModel;
import adda.base.views.ViewBase;
import adda.application.controls.JTabbedPaneClosable;
import javax.swing.*;
import java.awt.*;


public class WorkspaceView extends ViewBase {
    protected JTabbedPaneClosable pane;

    public void initFromModel(IModel model) {
        WorkspaceModel workspaceModel = (WorkspaceModel) model;
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        //panel.setBackground(Color.WHITE);
        pane = new JTabbedPaneClosable<String>();
        pane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        workspaceModel.
                getBoxes().
                entrySet().
                stream().
                forEach(entry -> {
                    pane.addClosable(entry.getValue().getName(), entry.getValue().getLayout(), entry.getValue());
                });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(pane, gbc);
    }

    @Override
    public void modelPropertyChanged(IModel sender, IModelPropertyChangeEvent event) {
        if (event.getPropertyName().equals("focusedBox")) {
            WorkspaceModel workspaceModel = (WorkspaceModel) sender;
            IBox focusedBox = workspaceModel.getFocusedBox();
            if (focusedBox != null) {
                if (pane.getSelectedComponent() == null || !focusedBox.getLayout().equals(pane.getSelectedComponent())) {
                    for (Component component : pane.getComponents()) {
                        if (component.equals(focusedBox.getLayout())) {
                            pane.setSelectedComponent(component);
                            return;
                        }
                    }
                    pane.addClosable(focusedBox.getName(), focusedBox.getLayout(), workspaceModel.getPathByBox(focusedBox));
                }
            }

        }
    }
}
