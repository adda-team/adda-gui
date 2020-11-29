package adda;

import adda.application.MainForm;
import adda.item.root.projectTree.*;
import adda.item.root.workspace.WorkspaceModel;
import adda.utils.Binder;
import adda.item.root.shortcut.ShortcutsBox;
import adda.item.root.workspace.WorkspaceBox;
import com.formdev.flatlaf.FlatLightLaf;

import javax.help.CSH;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.help.HelpSetException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class Main {
//    static {
//        System.loadLibrary("flatlaf");
//    }
    public static void main(String[] args) {

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
//                    BasicLookAndFeel darcula = new DarculaLaf();
                    UIManager.setLookAndFeel(new FlatLightLaf());
//                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//                    UIManager.put("Label.disabledForeground",Color.lightGray);
                } catch (/*ClassNotFoundException |
                        IllegalAccessException |*/
                        UnsupportedLookAndFeelException /*|
                        InstantiationException*/ e) {
                    e.printStackTrace();
                }



                JFrame frame = new JFrame("   ADDA GUI");
                MainForm app = new MainForm();
                app.setLoadingVisible(true);
                app.getShortcutPanel().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.lightGray));

                //todo initialization boxes put into context
                ProjectTreeBox treeBox = new ProjectTreeBox();
                treeBox.init();

                WorkspaceBox workspaceBox = new WorkspaceBox();
                workspaceBox.init();

                ShortcutsBox shortcutsBox = new ShortcutsBox();
                shortcutsBox.init();

                //todo initialization boxes put into context
                Binder.bind(treeBox, workspaceBox);

                app.getLeftPanel().add(treeBox.getLayout());
                app.getCenterPanel().add(workspaceBox.getLayout());
                app.getShortcutPanel().add(shortcutsBox.getLayout());

                frame.setContentPane(app.getMainPanel());
                frame.setMinimumSize(new Dimension(1024, 700));
                frame.setPreferredSize(new Dimension(1200, 700));
                frame.setJMenuBar(getMenuBar());
                //set custom menubar between app icon and close button in header https://medium.com/swlh/customizing-the-title-bar-of-an-application-window-50a4ac3ed27e
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);

                Context.getInstance().mainFrame = frame;
                Context.getInstance().mainForm = app;
                Context.getInstance().workspaceModel = (WorkspaceModel) workspaceBox.getModel();
                Context.getInstance().projectTreeModel = (ProjectTreeModel) treeBox.getModel();





                //Binder.bind(Context.getInstance().getWorkspaceModel(), shortcutsBox.getModel());

                String path = "image/adda_logo.png";

                java.net.URL imgURL = Main.class.getClassLoader().getResource(path);
                if (imgURL != null) {
                    ImageIcon img = new ImageIcon(imgURL);
                    frame.setIconImage(img.getImage());
                }
                final HelpBroker helpBroker = Context.getInstance().getHelpBroker();
                helpBroker.enableHelpKey(app.getMainPanel(), "introduction", helpBroker.getHelpSet());


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        javax.swing.SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                if (Context.getInstance().projectTreeModel.getChildCount(Context.getInstance().projectTreeModel.getRoot()) > 0) {
                                    Context.getInstance().projectTreeModel.setSelectedPath((ProjectTreeNode) Context.getInstance().projectTreeModel.getChild(Context.getInstance().projectTreeModel.getRoot(), 0));
                                }
                                app.setLoadingVisible(false);
                            }
                        });
                    }
                }).start();

            }
        });
    }

    private static JMenuBar getMenuBar() {

        //todo rework mock menu
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");

        JMenu newMenu = new JMenu("New");

        fileMenu.add(newMenu);

        JMenuItem project = new JMenuItem("Project");
        project.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Context.getInstance().getProjectTreeModel().showNewProjectDialog();
            }
        });
        newMenu.add(project);

        fileMenu.addSeparator();

        JMenuItem exitItem = new JMenuItem("Exit");
        fileMenu.add(exitItem);
//        exitItem.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                System.exit(0);
//            }
//        });

        menuBar.add(fileMenu);

        JMenu helpMenu = new JMenu("Help");

        final JMenuItem openHelpSystem = new JMenuItem("Open help system");

        openHelpSystem.addActionListener(new CSH.DisplayHelpFromSource(Context.getInstance().getHelpBroker()));

        helpMenu.add(openHelpSystem);
        helpMenu.add(new JMenuItem("About"));

        menuBar.add(helpMenu);


        return menuBar;
    }

}
