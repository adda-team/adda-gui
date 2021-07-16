package adda.application;

import adda.Context;
import adda.application.controls.VerticalLayout;
import adda.application.runner.Downloader;
import adda.settings.AppSetting;
import adda.settings.Setting;
import adda.settings.SettingsManager;
import adda.utils.OsUtils;
import adda.utils.ReflectionHelper;
import adda.utils.StringHelper;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.help.CSH;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

public class SettingDialog extends JDialog {
    protected JPanel wrapperPanel;
    protected JButton buttonOK;
    protected JButton buttonCancel;


    protected JPanel contentPanel;
    protected boolean isOkPressed = false;
    protected boolean isDownloading = false;
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

        JButton button = new JButton("<HTML><FONT color=\"#000099\"><U>" + StringHelper.toDisplayString("download latest ADDA release from github") + "</U></FONT></HTML>");
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setFocusable(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        //button.setHorizontalAlignment(JButton.LEFT);

        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.add(button, BorderLayout.WEST);
        buttonPanel.setPreferredSize(new Dimension(450, 20));


        addaPanel.add(buttonPanel);

        JTextArea textArea = new JTextArea();

        textArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        textArea.setOpaque(true);
        textArea.setEditable(false);


        textArea.setFocusable(true);
        textArea.setBackground(Color.white);
        textArea.setForeground(Color.black);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        JScrollPane scroll = new JScrollPane(textArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        JPanel terminalPanel = new JPanel(new BorderLayout());
        terminalPanel.add(scroll, BorderLayout.CENTER);
        terminalPanel.setPreferredSize(new Dimension(450, 300));
        terminalPanel.setVisible(false);
        addaPanel.add(terminalPanel);

        button.addActionListener(e -> {
            if (!isDownloading && !StringHelper.isEmpty(appSetting.getGitPath())) {
                isDownloading = true;
                javax.swing.SwingUtilities.invokeLater(() -> terminalPanel.setVisible(true));
                javax.swing.SwingUtilities.invokeLater(() -> terminalPanel.revalidate());
                javax.swing.SwingUtilities.invokeLater(() -> terminalPanel.repaint());
                javax.swing.SwingUtilities.invokeLater(() -> textArea.append("Retrieve data from  " + appSetting.getGitPath() + "\n"));
                Thread t = new Thread(() -> {
                    try {
                        BufferedReader reader = null;
                        String response;
                        try {
                            URL url = new URL(appSetting.getGitPath());
                            reader = new BufferedReader(new InputStreamReader(url.openStream()));
                            StringBuffer buffer = new StringBuffer();
                            int read;
                            char[] chars = new char[1024];
                            while ((read = reader.read(chars)) != -1)
                                buffer.append(chars, 0, read);

                            response = buffer.toString();
                        } finally {
                            if (reader != null)
                                reader.close();
                        }

                        JSONObject json = new JSONObject(response);

                        String zipUrl = String.valueOf(json.get("zipball_url"));

                        if (OsUtils.isWindows()) {
                            zipUrl = String.valueOf(((JSONObject) ((JSONArray) json.get("assets")).get(0)).get("browser_download_url"));
                        }

                        String finalZipUrl = zipUrl;
                        javax.swing.SwingUtilities.invokeLater(() -> textArea.append("Latest release is  " + finalZipUrl + "\n"));

                        String downloadDir = System.getProperty("user.dir") + "/download";
                        OsUtils.createFolder(downloadDir);

                        Date now = new Date();
                        SimpleDateFormat pattern = new SimpleDateFormat("MMddHHmmss");
                        String path = downloadDir + "/adda_release_" + pattern.format(now);
                        OsUtils.createFolder(path);

                        String zipFileName = path + "/release.zip";

                        javax.swing.SwingUtilities.invokeLater(() -> textArea.append("Downloading to " + zipFileName + "\n"));
                        Downloader downloader = new Downloader(new URL(zipUrl), zipFileName);
                        downloader.run();
                        Thread.sleep(100);
                        while (downloader.getStatus() == 0) {
                            Thread.sleep(100);
                            javax.swing.SwingUtilities.invokeLater(() -> textArea.append("Downloaded " + downloader.getDownloaded() + " from " + downloader.getSize()));
                        }

                        javax.swing.SwingUtilities.invokeLater(() -> textArea.append("Downloaded \nUnzipping \n"));

                        OsUtils.unzip(zipFileName, path);

                        javax.swing.SwingUtilities.invokeLater(() -> textArea.append("Unzipped to " + path + " \n"));

                        Optional<File> releaseDirOptional = Arrays.stream((new File(path)).listFiles()).filter(File::isDirectory).findFirst();
                        if (releaseDirOptional.isPresent()) {

                            if (OsUtils.isWindows()) {
                                javax.swing.SwingUtilities.invokeLater(() -> textArea.append("Succesfully finished \n"));
                                String win64Dir = releaseDirOptional.get().getAbsolutePath() + "\\win64";

                                final JTextField seq = map.get("addaExecSeq");
                                seq.setText(win64Dir + "\\adda.exe");
                                seq.setCaretPosition(seq.getText().length());
                                final JTextField mpi = map.get("addaExecMpi");
                                mpi.setText(win64Dir + "\\adda_mpi.exe");
                                mpi.setCaretPosition(mpi.getText().length());
                                final JTextField gpu = map.get("addaExecGpu");
                                gpu.setText(win64Dir + "\\adda_ocl.exe");
                                gpu.setCaretPosition(gpu.getText().length());


                            } else {
                                String srcPath = releaseDirOptional.get().getAbsolutePath() + "/src";
                                javax.swing.SwingUtilities.invokeLater(() ->
                                {
                                    textArea.append("Succesfully prepared \n");
                                    textArea.append("Now we have to compile ADDA from src code \n");
                                    textArea.append("ADDA required gcc, libfftw3-dev (http://www.fftw.org/) and gfortran  \n");
                                    textArea.append("----------------  \n");
                                    textArea.append("\n");
                                    textArea.append("sudo apt-get install gcc  \n");
                                    textArea.append("sudo apt-get install gfortran  \n");
                                    textArea.append("sudo apt-get install libfftw3-dev  \n");

                                    textArea.append("\n");
                                    textArea.append("----------------  \n");
                                    textArea.append("after reqiured libs installation go to " + srcPath + " and execute\n");

                                    textArea.append("----------------  \n");
                                    textArea.append("\n");
                                    textArea.append("cd " + srcPath + "  \n");
                                    textArea.append("make seq\n");
                                    textArea.append("\n");
                                    textArea.append("----------------  \n");
                                });


                                String seqPath = releaseDirOptional.get().getAbsolutePath() + "/seq";
                                final JTextField seq = map.get("addaExecSeq");
                                seq.setText(seqPath + "/adda");
                                seq.setCaretPosition(seq.getText().length());

                                try {
                                    cmd("gnome-terminal", seqPath);
                                } catch (Exception ignored) {
                                    cmd(System.getenv().get("TERM"), seqPath);
                                }
                            }
                        }


                    } catch (Exception e1) {
                        javax.swing.SwingUtilities.invokeLater(() -> textArea.append(e1.getMessage()));
                        javax.swing.SwingUtilities.invokeLater(() -> textArea.append("\n"));
                    } finally {
                        isDownloading = false;
                    }
                });
                t.start();

            }
        });

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

    private void cmd(String cmd, String srcPath) throws IOException {
        String[] cmdArray = {cmd, "-e", "cd " + srcPath + " && sudo apt-get install gcc && sudo apt-get install gfortran && sudo apt-get install libfftw3-dev && make seq"};
        Runtime rt = Runtime.getRuntime();
        rt.exec(cmdArray);
    }

    Map<String, JTextField> map = new HashMap<>();

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

        map.put(field, addaTextField);

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
