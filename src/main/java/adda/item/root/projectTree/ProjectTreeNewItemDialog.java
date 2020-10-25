package adda.item.root.projectTree;

import adda.Context;
import adda.application.controls.CustomOkCancelModalDialog;
import adda.application.controls.JNumericField;
import adda.base.events.IModelPropertyChangeEvent;
import adda.base.models.IModel;
import adda.item.tab.shape.orientation.OrientationModel;
import adda.item.tab.shape.orientation.avarage.OrientationAverageModel;
import adda.utils.ListenerHelper;
import adda.utils.StringHelper;
import jiconfont.icons.font_awesome.FontAwesome;
import jiconfont.swing.IconFontSwing;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class ProjectTreeNewItemDialog  extends CustomOkCancelModalDialog {
    public ProjectTreeNewItemDialog(IModel model) {
        super(model, StringHelper.toDisplayString("Create new project"));

        if (!(model instanceof ProjectTreeNewItemModel)) return;
        ProjectTreeNewItemModel projectTreeNewItemModel = (ProjectTreeNewItemModel) model;

        JPanel rootPanel = new JPanel(new GridBagLayout());
        rootPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.ipady = 1;
        rootPanel.add(surroundWithPanel(new JLabel(StringHelper.toDisplayString("Name"))), gbc);

        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(100, 20));
        field.setText(projectTreeNewItemModel.getDisplayName());
        field.getDocument().addDocumentListener(ListenerHelper.getSimpleDocumentListener(() -> {
            projectTreeNewItemModel.setDisplayName(field.getText());
        }));

        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.ipady = 1;
        rootPanel.add(surroundWithPanel(field), gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.ipady = 1;
        rootPanel.add(surroundWithPanel(new JLabel(StringHelper.toDisplayString("Folder"))), gbc);

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));


        IconFontSwing.register(FontAwesome.getIconFont());

        Icon fileOpenIcon = IconFontSwing.buildIcon(FontAwesome.FOLDER_OPEN, 17, Color.DARK_GRAY);
        JButton fileOpenButton = new JButton(fileOpenIcon);
        fileOpenButton.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 5));
        fileOpenButton.setBorderPainted(false);
        fileOpenButton.setOpaque(false);
        fileOpenButton.setContentAreaFilled(false);




        panel.add(fileOpenButton);

        JTextArea textArea = new JTextArea();
        textArea.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        textArea.setText(projectTreeNewItemModel.getDirectory());
        textArea.setWrapStyleWord(false);
        textArea.setLineWrap(false);
        textArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        textArea.setOpaque(false);
        textArea.setEditable(false);
        textArea.setFocusable(true);
        textArea.setBackground(UIManager.getColor("Label.background"));
        textArea.setFont(UIManager.getFont("Label.font"));
        textArea.setBorder(UIManager.getBorder("Label.border"));
        textArea.setAlignmentY(Component.CENTER_ALIGNMENT);

        JScrollPane scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setMaximumSize(new Dimension(240, 25));
        scrollPane.setPreferredSize(new Dimension(240, 25));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());



        scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 5));
        scrollPane.getHorizontalScrollBar().setUI(new BasicScrollBarUI()
        {
            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton jbutton = new JButton();
                jbutton.setPreferredSize(new Dimension(0, 0));
                jbutton.setMinimumSize(new Dimension(0, 0));
                jbutton.setMaximumSize(new Dimension(0, 0));
                return jbutton;
            }
        });


        fileOpenButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("ADDA orientation average");//todo localization
            FileNameExtensionFilter filter = new FileNameExtensionFilter("ADDA config", "dat");
            fileChooser.setFileFilter(filter);

            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int result = fileChooser.showOpenDialog(Context.getInstance().getMainFrame());
            if (result == JFileChooser.APPROVE_OPTION) {
                projectTreeNewItemModel.setDirectory(fileChooser.getSelectedFile().getAbsolutePath());
                textArea.setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });

        panel.add(scrollPane);


        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.ipady = 1;
        rootPanel.add(panel, gbc);

        getDialogContentPanel().add(rootPanel);

    }

    @Override
    public void modelPropertyChanged(IModel sender, IModelPropertyChangeEvent event) {

    }
}
