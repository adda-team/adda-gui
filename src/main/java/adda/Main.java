package adda;

import adda.application.MainForm;
import adda.utils.Binder;
import adda.item.root.projectTree.ProjectTreeBox;
import adda.item.root.shortcut.ShortcutsBox;
import adda.item.root.workspace.WorkspaceBox;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.plaf.basic.BasicLookAndFeel;
import java.awt.*;

public class Main {

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

                JFrame frame = new JFrame("ADDA GUI");
                MainForm app = new MainForm();

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
            }
        });
    }

    private static JMenuBar getMenuBar() {

        //todo rework mock menu
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");

        JMenu newMenu = new JMenu("New");
        fileMenu.add(newMenu);

        JMenuItem txtFileItem = new JMenuItem("Project");
        newMenu.add(txtFileItem);

        JMenuItem imgFileItem = new JMenuItem("View");
        newMenu.add(imgFileItem);

        JMenuItem openItem = new JMenuItem("Open");
        fileMenu.add(openItem);

        fileMenu.addSeparator();

        JMenuItem exitItem = new JMenuItem("Exit");
        fileMenu.add(exitItem);
//        exitItem.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                System.exit(0);
//            }
//        });

        menuBar.add(fileMenu);

        JMenu runMenu = new JMenu("Run");

        runMenu.add(new JMenuItem("Run 'proj1'"));//todo get project name from context
        runMenu.add(new JMenuItem("Prognosis for 'proj1'"));

        menuBar.add(runMenu);

        JMenu helpMenu = new JMenu("Help");

        helpMenu.add(new JMenuItem("Open help system"));
        helpMenu.add(new JMenuItem("About"));

        menuBar.add(helpMenu);


        return menuBar;
    }

}
