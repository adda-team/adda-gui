package adda;

import adda.application.MainForm;
import adda.base.boxes.IBox;
import adda.base.models.IModel;
import adda.item.root.projectArea.ProjectAreaBox;
import adda.item.root.projectArea.ProjectAreaModel;
import adda.item.root.projectTree.ProjectTreeModel;
import adda.item.root.workspace.WorkspaceModel;
import adda.item.tab.base.refractiveIndexAggregator.RefractiveIndexAggregatorModel;
import adda.settings.formatters.json.JsonFormatter;
import adda.settings.formatters.plaintext.PlainTextFormatter;
import adda.settings.serializer.AddaSerializer;
import adda.settings.serializer.ISerializer;

import javax.swing.*;

public class Context {

    protected JFrame mainFrame;

    protected MainForm mainForm;

    protected WorkspaceModel workspaceModel;

    protected ProjectTreeModel projectTreeModel;

    protected String runPath;

    public String getRunPath() {
        return runPath;
    }

    public WorkspaceModel getWorkspaceModel() {
        return workspaceModel;
    }

    public ProjectTreeModel getProjectTreeModel() {
        return projectTreeModel;
    }

    public MainForm getMainForm() {
        return mainForm;
    }

    public JFrame getMainFrame() {
        return mainFrame;
    }

    public ISerializer getXmlSerializer() {
        return xmlSerializer;
    }

    public ISerializer getJsonSerializer() {
        return jsonSerializer;
    }

    public ISerializer getTextSerializer() {
        return textSerializer;
    }

    ISerializer xmlSerializer = new AddaSerializer();
    ISerializer jsonSerializer = new AddaSerializer(new JsonFormatter());
    ISerializer textSerializer = new AddaSerializer(new PlainTextFormatter());



    private static volatile Context instance;

    public static Context getInstance() {
        Context localInstance = instance;
        if (localInstance == null) {
            synchronized (Context.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new Context();
                }
            }
        }
        return localInstance;
    }

    public IModel getChildModelFromSelectedBox(Class<?> clazz) {

        IBox box = getWorkspaceModel().getFocusedBox();
        if (box instanceof ProjectAreaBox) {
            return  ((ProjectAreaModel) box.getModel())
                            .getNestedModelList()
                            .stream()
                            .filter(model -> model != null && model.getClass().equals(clazz))
                            .findFirst()
                            .get();
        }
        return null;
    }


}
