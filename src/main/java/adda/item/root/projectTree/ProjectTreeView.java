package adda.item.root.projectTree;

import adda.base.views.ViewBase;
import adda.base.models.IModel;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;

public class ProjectTreeView extends ViewBase {

    @Override
    public void initFromModel(IModel model) {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setLayout(new GridBagLayout());
        this.panel = panel;
        JTree jtree = new JTree((ProjectTreeModel)model);
        jtree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        this.panel.add(jtree, gbc);
    }
}
