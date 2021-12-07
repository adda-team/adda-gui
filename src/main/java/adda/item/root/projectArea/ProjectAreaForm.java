package adda.item.root.projectArea;

import adda.utils.StringHelper;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class ProjectAreaForm {
    private JPanel panelBasic;
    private JPanel panelOutput;
    private JPanel panelDdaInternal;
    private JPanel panelShape;
    private JPanel panelOptions;
    private JPanel panelMain;
    private JPanel centerPanel;
    private JScrollPane scrollBasic;

    public JScrollPane getScrollDdaInternal() {
        return scrollDdaInternal;
    }

    private JScrollPane scrollDdaInternal;
    private JScrollPane scrollOutput;
    private JScrollPane scrollShape;

    //private JButton clearButton;

    public JPanel getPanelMain() {
        return panelMain;
    }

    public JPanel getPanelBasic() {
        return panelBasic;
    }

    public JPanel getPanelOutput() {
        return panelOutput;
    }

    public JPanel getPanelDdaInternal() {
        return panelDdaInternal;
    }

    public JPanel getPanelShape() {
        return panelShape;
    }

    public JPanel getPanelOptions() {
        return panelOptions;
    }

    public ProjectAreaForm() {
        //centerPanel.setMinimumSize(new Dimension(250, 100));
        scrollShape.setMinimumSize(new Dimension(250, 100));
        panelShape.setMinimumSize(new Dimension(250, 100));
        panelOptions.setMinimumSize(new Dimension(100, 100));
//        panelOutput.setMaximumSize(new Dimension(220, 9999));
//        panelDdaInternal.setMaximumSize(new Dimension(220, 9999));
//        panelBasic.setMaximumSize(new Dimension(220, 9999));
//        scrollOutput.setMaximumSize(new Dimension(220, 9999));
//        scrollDdaInternal.setMaximumSize(new Dimension(220, 9999));
//        scrollBasic.setMaximumSize(new Dimension(220, 9999));

        scrollBasic.getVerticalScrollBar().setUnitIncrement(16);
        scrollDdaInternal.getVerticalScrollBar().setUnitIncrement(16);
        scrollOutput.getVerticalScrollBar().setUnitIncrement(16);
        scrollOutput.getVerticalScrollBar().setUnitIncrement(16);
        scrollShape.getVerticalScrollBar().setUnitIncrement(16);

//        final JLabel label = new JLabel("Configured Options");
//        label.setHorizontalAlignment(JLabel.CENTER);
//        label.setVerticalAlignment(JLabel.CENTER);
//        label.setPreferredSize(new Dimension(150, 25));
//
//        //clearButton = new JButton(StringHelper.toDisplayString("clear all"));
//
//        panelOptions.add(label, BorderLayout.NORTH);
        scrollShape.setMinimumSize(new Dimension(60, 300));


    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panelMain = new JPanel();
        panelMain.setLayout(new GridBagLayout());
        panelMain.setBackground(new Color(-855310));
        centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());
        centerPanel.setBackground(new Color(-855310));
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panelMain.add(centerPanel, gbc);
        final JTabbedPane tabbedPane1 = new JTabbedPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        centerPanel.add(tabbedPane1, gbc);
        scrollBasic = new JScrollPane();
        scrollBasic.setHorizontalScrollBarPolicy(31);
        tabbedPane1.addTab("Basic", scrollBasic);
        panelBasic = new JPanel();
        panelBasic.setLayout(new BorderLayout(0, 0));
        scrollBasic.setViewportView(panelBasic);
        scrollDdaInternal = new JScrollPane();
        scrollDdaInternal.setHorizontalScrollBarPolicy(31);
        tabbedPane1.addTab("DDA Internal", scrollDdaInternal);
        panelDdaInternal = new JPanel();
        panelDdaInternal.setLayout(new BorderLayout(0, 0));
        scrollDdaInternal.setViewportView(panelDdaInternal);
        scrollOutput = new JScrollPane();
        scrollOutput.setHorizontalScrollBarPolicy(31);
        tabbedPane1.addTab("Output", scrollOutput);
        panelOutput = new JPanel();
        panelOutput.setLayout(new BorderLayout(0, 0));
        scrollOutput.setViewportView(panelOutput);
        scrollShape = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panelMain.add(scrollShape, gbc);
        panelShape = new JPanel();
        panelShape.setLayout(new BorderLayout(0, 0));
        scrollShape.setViewportView(panelShape);
        panelOptions = new JPanel();
        panelOptions.setLayout(new BorderLayout(0, 0));
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panelMain.add(panelOptions, gbc);
        panelOptions.setBorder(BorderFactory.createTitledBorder(""));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panelMain;
    }

}
