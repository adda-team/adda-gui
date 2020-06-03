package adda.item.root.projectTree;

import adda.base.controllers.ControllerBase;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.*;

public class ProjectTreeController extends ControllerBase {
    @Override
    protected void createAndBindListenersFromView() {
        if(view == null) {
            return;
        }

        for (Component component : view.getRootComponent().getComponents()) {
            if (component.getClass().equals(JTree.class)) {
                JTree jtree = (JTree) component;
                TreeWillExpandListener treeWillExpandListener = new TreeWillExpandListener() {
                    public void treeWillCollapse(TreeExpansionEvent treeExpansionEvent) throws ExpandVetoException {
                    }

                    public void treeWillExpand(TreeExpansionEvent treeExpansionEvent) throws ExpandVetoException {
                        TreePath path = treeExpansionEvent.getPath();
                        Object node = path.getLastPathComponent();
                        (jtree.getModel()).getChildCount(node);
                    }
                };

                jtree.addTreeWillExpandListener(treeWillExpandListener);

                MouseListener adapter = new MouseAdapter() {
                    public void mousePressed(MouseEvent e) {
                        int selRow = jtree.getRowForLocation(e.getX(), e.getY());
                        TreePath selPath = jtree.getPathForLocation(e.getX(), e.getY());

                        if (SwingUtilities.isRightMouseButton(e)) {
                            jtree.setSelectionPath(selPath);
                            if (selRow>-1){
                                jtree.setSelectionRow(selRow);
                            }
                            JPopupMenu popup = new JPopupMenu();
                            JMenuItem open = new JMenuItem("Open");
                            open.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent actionEvent) {
                                    //todo refactor copypaste!!!!!!!
                                    ProjectTreeNode node = (ProjectTreeNode) jtree.getLastSelectedPathComponent();
                                    if (node == null || model == null) {
                                        return;
                                    }
                                    ((ProjectTreeModel) model).setSelectedPath(String.format("%s", selPath.toString()));

                                }
                            });
                            popup.add(open);
                            popup.show(jtree, e.getX(), e.getY());
                        } else {

                            if (selRow != -1) {
                                if (e.getClickCount() == 2) {
                                    //todo refactor copypaste!!!!!!!
                                    ProjectTreeNode node = (ProjectTreeNode) jtree.getLastSelectedPathComponent();
                                    if (node == null || model == null) {
                                        return;
                                    }
                                    ((ProjectTreeModel) model).setSelectedPath(String.format("%s", selPath.toString()));
                                }
                            }
                        }
                    }
                };
                jtree.addMouseListener(adapter);






//                jtree.addTreeSelectionListener(new TreeSelectionListener() {
//                    @Override
//                    public void valueChanged(TreeSelectionEvent treeSelectionEvent) {
//                        ProjectTreeModel.NodeData node = (ProjectTreeModel.NodeData) jtree.getLastSelectedPathComponent();
//                        if (node == null || model == null) {
//                            return;
//                        }
//                        ((ProjectTreeModel)model).setSelectedPath(node.toString());
//                        //Object nodeInfo = node.getUserObject();
//                    }
//                });
            }
        }


    }
}
