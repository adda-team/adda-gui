package adda.application;

import adda.Context;
import adda.application.controls.VerticalLayout;
import adda.settings.AppSetting;
import adda.settings.Setting;
import adda.settings.SettingsManager;
import adda.utils.OsUtils;
import adda.utils.ReflectionHelper;
import adda.utils.StringHelper;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Arrays;
import java.util.Vector;

public class SettingDialog extends JDialog {
    protected JPanel wrapperPanel;
    protected JButton buttonOK;
    protected JButton buttonCancel;


    protected JPanel contentPanel;
    protected boolean isOkPressed = false;
    protected AppSetting appSetting;



    public boolean isOkPressed() {
        return isOkPressed;
    }


    public SettingDialog(AppSetting appSetting) {
        super(Context.getInstance().getMainFrame(), StringHelper.toDisplayString("Settings"), ModalityType.APPLICATION_MODAL);
        setLocationRelativeTo(Context.getInstance().getMainFrame());
        this.appSetting = appSetting;

        setMinimumSize(new Dimension(600, 400));
        setContentPane(wrapperPanel);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        wrapperPanel.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        buttonOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });


        JTabbedPane tabs = new JTabbedPane(JTabbedPane.LEFT);

        JPanel addaPanel = new JPanel(new VerticalLayout());

        addaPanel.add(createAddaExecConfigPanel("addaExecSeq"));
        addaPanel.add(createAddaExecConfigPanel("addaExecMpi"));
        addaPanel.add(createAddaExecConfigPanel("addaExecGpu"));

        tabs.addTab("ADDA", addaPanel);

        JPanel appearancePanel = new JPanel(new VerticalLayout());

        JPanel panel = new JPanel(new BorderLayout());

        final JLabel label = new JLabel("Font size ");
        label.setPreferredSize(new Dimension(100, 20));
        panel.add(label, BorderLayout.WEST);

        JComboBox fontCombo = new JComboBox(new Vector<String>(Arrays.asList("12")));
        fontCombo.setPreferredSize(new Dimension(50, 20));
        panel.add(fontCombo);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        appearancePanel.add(panel);


        panel = new JPanel(new BorderLayout());

        final JLabel languageLabel = new JLabel("Language ");
        languageLabel.setPreferredSize(new Dimension(100, 20));
        panel.add(languageLabel, BorderLayout.WEST);

        JComboBox langCombo = new JComboBox(new Vector<String>(Arrays.asList("EN")));
        langCombo.setPreferredSize(new Dimension(50, 20));
        panel.add(langCombo);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        appearancePanel.add(panel);


        tabs.addTab("Appearance", appearancePanel);

        contentPanel.add(tabs);


    }

    private JPanel createAddaExecConfigPanel(String field) {
        JPanel panel = new JPanel(new BorderLayout());

        final JLabel label = new JLabel(field + " ");
        label.setPreferredSize(new Dimension(100, 20));
        panel.add(label, BorderLayout.WEST);

        JTextField addaTextField = new JTextField();
        final String path = String.valueOf(ReflectionHelper.getPropertyValue(appSetting, field));
        addaTextField.setText(path);
        addaTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                update();
            }

            private void update() {
                ReflectionHelper.setPropertyValue(appSetting, field, addaTextField.getText());
            }
        });
        addaTextField.setPreferredSize(new Dimension(350, 20));
        panel.add(addaTextField);

        JButton addaButton = new JButton("...");

        addaButton.addActionListener(e -> {

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle(field);//todo localization
            if (OsUtils.isWindows()) {
                FileNameExtensionFilter filter = new FileNameExtensionFilter("ADDA Executable", "exe");
                fileChooser.setFileFilter(filter);
            }


            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            if (!StringHelper.isEmpty(path)) {
                fileChooser.setSelectedFile(new File(path));
            }


            int result = fileChooser.showOpenDialog(Context.getInstance().getMainFrame());
            if (result == JFileChooser.APPROVE_OPTION) {
                addaTextField.setText(fileChooser.getSelectedFile().getAbsolutePath());
                ReflectionHelper.setPropertyValue(appSetting, field, fileChooser.getSelectedFile().getAbsolutePath());
            }


        });

        panel.add(addaButton, BorderLayout.EAST);

        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        return panel;
    }


    protected JPanel surroundWithPanel(Component component) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(component);
        return panel;
    }

    public JPanel getDialogContentPanel() {
        return contentPanel;
    }


    private void onOK() {
        isOkPressed = true;
        setVisible(false);
        Setting setting = SettingsManager.getSettings();
        setting.setAppSetting(this.appSetting);
        SettingsManager.saveSettings(setting);

        dispose();
    }

    private void onCancel() {
        isOkPressed = false;
        setVisible(false);

        dispose();
    }

//    public void modelPropertyChanged(IModel sender, IModelPropertyChangeEvent event) {
//
//    }

    {
        wrapperPanel = new JPanel();
        wrapperPanel.setLayout(new GridBagLayout());
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout(0, 0));
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        wrapperPanel.add(panel1, gbc);
        panel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), null));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridBagLayout());
        panel1.add(panel2, BorderLayout.EAST);
        buttonOK = new JButton();
        buttonOK.setText("OK");//todo localization
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(buttonOK, gbc);
        buttonCancel = new JButton();
        buttonCancel.setText("Cancel");//todo localization
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(buttonCancel, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(spacer1, gbc);
        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout(0, 0));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        wrapperPanel.add(contentPanel, gbc);
    }




}
