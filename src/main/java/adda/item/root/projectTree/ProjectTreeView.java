package adda.item.root.projectTree;

import adda.base.events.IModelPropertyChangeEvent;
import adda.base.views.ViewBase;
import adda.base.models.IModel;
import jiconfont.icons.font_awesome.FontAwesome;
import jiconfont.swing.IconFontSwing;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.*;
import java.awt.*;

public class ProjectTreeView extends ViewBase {

    private JTree jtree;

    @Override
    public void initFromModel(IModel model) {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setLayout(new GridBagLayout());
        this.panel = panel;
        jtree = new JTree((ProjectTreeModel)model);
        jtree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        this.panel.add(jtree, gbc);
        IconFontSwing.register(FontAwesome.getIconFont());
        jtree.setCellRenderer(new CustomTreeCellRenderer(
                IconFontSwing.buildIcon(FontAwesome.DESKTOP, 14, Color.black),
                IconFontSwing.buildIcon(FontAwesome.FOLDER_O, 14, Color.black),
                IconFontSwing.buildIcon(FontAwesome.FOLDER_OPEN_O, 14, Color.black)
        ));
        jtree.addTreeWillExpandListener(new TreeWillExpandListener() {
            @Override
            public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {

            }

            @Override
            public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
                ProjectTreeNode node = (ProjectTreeNode) jtree.getLastSelectedPathComponent();
                if (jtree.getModel().getRoot().equals(node)) {
                    throw new ExpandVetoException(event, "Collapsing tree not allowed");
                }
            }
        });
    }

    @Override
    public void modelPropertyChanged(IModel sender, IModelPropertyChangeEvent event) {
        if (ProjectTreeModel.REFRESH.equals(event.getPropertyName())) {
            SwingUtilities.updateComponentTreeUI(jtree);
        }
        if (ProjectTreeModel.SELECTED_PATH_FIELD_NAME.equals(event.getPropertyName())) {
            if (jtree.getLastSelectedPathComponent() != event.getPropertyValue()) {
                for (int i = 0; i < jtree.getRowCount(); i++) {
                    if (jtree.getPathForRow(i).getLastPathComponent().equals(event.getPropertyValue())) {
                        int rowIndex = i;
                        javax.swing.SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                jtree.setSelectionRow(rowIndex);
                            }
                        });
                        break;
                    }
                }

            }
        }
    }

    private class CustomTreeCellRenderer extends DefaultTreeCellRenderer {

        Icon desc;
        Icon folder;
        Icon folderExp;

        public CustomTreeCellRenderer(Icon desc, Icon folder, Icon folderExp) {
            this.desc = desc;
            this.folder = folder;
            this.folderExp = folderExp;
        }

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, selected,expanded, leaf, row, hasFocus);
            ProjectTreeNode node = (ProjectTreeNode) value;

            if (tree.getModel().getRoot().equals(node)) {
                setIcon(desc);
            } else if (node != null && node.isProject()) {
                setIcon(expanded ? folderExp : folder);
            }

            return this;
        }
    }
}
