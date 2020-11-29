package adda.item.root.projectTree;

import adda.Context;
import adda.application.MainForm;
import adda.base.controllers.ControllerBase;
import jiconfont.icons.font_awesome.FontAwesome;
import jiconfont.swing.IconFontSwing;

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

                        boolean showPopup = false;

                        if (SwingUtilities.isRightMouseButton(e)) {
                            jtree.setSelectionPath(selPath);
                            if (selRow>-1){
                                jtree.setSelectionRow(selRow);
                            }
                            showPopup = true;

                        } else {

                            if (selRow != -1) {
                                if (e.getClickCount() == 2) {

                                    final MainForm mainForm = Context.getInstance().getMainForm();
                                    if (mainForm != null) {
                                        mainForm.setLoadingVisible(true);
                                    }

                                    //todo refactor copypaste!!!!!!!
                                    ProjectTreeNode node = (ProjectTreeNode) jtree.getLastSelectedPathComponent();
                                    if (node == null || model == null) {
                                        return;
                                    }
                                    if (jtree.getModel().getRoot().equals(node)) {
                                        showPopup = true;
                                    } else {
                                        javax.swing.SwingUtilities.invokeLater(new Runnable() {
                                            public void run() {
                                                ((ProjectTreeModel) model).setSelectedPath(node);
                                                if (mainForm != null) {
                                                    mainForm.setLoadingVisible(false);
                                                }
                                            }
                                        });
                                    }

                                }
                            }
                        }

                        if (showPopup) {
                            ProjectTreeNode node = (ProjectTreeNode) jtree.getLastSelectedPathComponent();
                            JPopupMenu popup = new JPopupMenu();
                            if (jtree.getModel().getRoot().equals(node)) {
                                JMenuItem open = new JMenuItem("Add new project");
                                open.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent actionEvent) {
                                        ((ProjectTreeModel) jtree.getModel()).showNewProjectDialog();
                                    }
                                });
                                popup.add(open);
                            } else {
                                JMenuItem open = new JMenuItem("Open");
                                open.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent actionEvent) {
                                        //todo refactor copypaste!!!!!!!
                                        ProjectTreeNode node = (ProjectTreeNode) jtree.getLastSelectedPathComponent();
                                        if (node == null || model == null) {
                                            return;
                                        }
                                        ((ProjectTreeModel) model).setSelectedPath(node);
                                    }
                                });
                                popup.add(open);
                            }
                            popup.show(jtree, e.getX(), e.getY());
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
