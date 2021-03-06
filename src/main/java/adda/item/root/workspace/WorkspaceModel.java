package adda.item.root.workspace;

import adda.Context;
import adda.base.boxes.BoxBase;
import adda.base.boxes.IBox;
import adda.base.events.IModelPropertyChangeEvent;
import adda.base.models.IModel;
import adda.base.models.ModelBase;
import adda.base.models.IModelObserver;
import adda.item.root.lineChart.LineChartBox;
import adda.item.root.lineChart.LineChartModel;
import adda.item.root.numberedText.NumberedTextBox;
import adda.item.root.numberedText.NumberedTextModel;
import adda.item.root.projectArea.ProjectAreaBox;
import adda.item.root.projectArea.ProjectAreaModel;
import adda.item.root.projectTree.ProjectTreeModel;
import adda.item.root.projectTree.ProjectTreeNode;
import adda.item.tab.base.BaseTabBox;
import adda.item.tab.shape.orientation.OrientationEnum;
import adda.item.tab.shape.orientation.OrientationModel;
import adda.utils.SwingUtils;

import javax.swing.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class WorkspaceModel extends ModelBase implements IModelObserver {
    public static final String FOCUSED_BOX_FIELD_NAME = "focusedBox";
    protected Map<ProjectTreeNode, IBox> boxes = new HashMap<>(); //todo may be initialization can delete
    protected List<ProjectTreeNode> keys = new ArrayList<ProjectTreeNode>(); //todo may be initialization can delete
    protected IBox focusedBox;

    public Map<ProjectTreeNode, IBox> getBoxes() {
        return Collections.unmodifiableMap(boxes);
    }

    public IBox getFocusedBox() {
        return focusedBox;
    }

    public void removeBoxByPath(ProjectTreeNode path) {
        IBox box = boxes.get(path);
        boxes.remove(path, box);
        keys.remove(path);//todo if different boxes has the same keys? maybe replace map to other data structure
    }

    public ProjectTreeNode getPathByBox(IBox box) {
        if (box == null) {
            return null;
        }
        Optional<ProjectTreeNode> result = boxes.entrySet()
                .stream()
                .filter(entry -> box.equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .findFirst();

        if (!result.isPresent())
            return null;
        ProjectTreeNode path = result.get();
        return path;
    }

    public void selectBoxByPath(ProjectTreeNode path) {
        if (path == null) {
            return;
        }
        Optional<ProjectTreeNode> result = boxes.entrySet()
                .stream()
                .filter(entry -> path.equals(entry.getKey()))
                .map(Map.Entry::getKey)
                .findFirst();

        if (!result.isPresent())
            return;
        IBox box = boxes.get(path);
        setFocusedBox(box);
    }


    public void setFocusedBox(IBox focusedBox) {
        if ((this.focusedBox != null && !this.focusedBox.equals(focusedBox))
                || (this.focusedBox == null && focusedBox != null)) { //todo if condition to func
            setAreaActive(false);
            this.focusedBox = focusedBox;
            notifyObservers(FOCUSED_BOX_FIELD_NAME, focusedBox);
            setAreaActive(true);
            Context.getInstance().getMainForm().getBottomPanel().setVisible(this.focusedBox instanceof ProjectAreaBox);
        }
    }

    private void setAreaActive(boolean areaActive) {
        if (this.focusedBox != null && this.focusedBox.getModel() instanceof ProjectAreaModel) {
            ((ProjectAreaModel) this.focusedBox.getModel()).setActive(areaActive);
        }
    }

    @Override
    public void modelPropertyChanged(IModel sender, IModelPropertyChangeEvent event) {
        if (sender instanceof ProjectTreeModel) {
            if (event.getPropertyName().equals("selectedPath")) {
//                javax.swing.SwingUtilities.invokeLater(new Runnable() {
//                    public void run() {
//                        Context.getInstance().getMainForm().setLoadingVisible(true);
//                    }
//                });
                ProjectTreeModel projectTreeModel = (ProjectTreeModel) sender;

                final IBox focusedBox;
                if (boxes.containsKey(projectTreeModel.getSelectedPath())) {
                    focusedBox = boxes.get(projectTreeModel.getSelectedPath());
                } else {
                    //todo get selected item type then get right box (may be fabric method)
                    if ((projectTreeModel.getSelectedPath().isPath())) {
                        focusedBox = new ProjectAreaBox(projectTreeModel.getSelectedPath().getName());

                        focusedBox.init();
                        final ProjectAreaModel projectAreaModel = (ProjectAreaModel) focusedBox.getModel();
                        projectAreaModel.setPathToState(projectTreeModel.getSelectedPath().getFolder());
                        projectAreaModel.loadNestedModelList();
                        if (!projectTreeModel.getSelectedPath().isProject()) {

                            OrientationModel orientationModel = (OrientationModel) projectAreaModel.getNestedModelList().stream()
                                    .filter(model -> model != null && model.getClass().equals(OrientationModel.class))
                                    .findFirst()
                                    .get();

                            if (OrientationEnum.Average.equals(orientationModel.getEnumValue())) {
                                orientationModel.setPathToRun(projectAreaModel.getPathToState());

                            }

                            javax.swing.SwingUtilities.invokeLater(new Runnable() {

                                public void run() {
                                    Context.getInstance().getMainForm().setLoadingVisible(true);
                                    focusedBox
                                            .getChildren()
                                            .forEach(child -> SwingUtils.setBoxEnabled(child, false));
                                    Context.getInstance().getMainForm().setLoadingVisible(false);
                                }
                            });
                        }
                    } else if ("mueller".equals(projectTreeModel.getSelectedPath().getName()) || "ampl".equals(projectTreeModel.getSelectedPath().getName()) ) {
                        focusedBox = new LineChartBox(projectTreeModel.getSelectedPath().getName());
                        focusedBox.init();
                        final LineChartModel lineChartModel = (LineChartModel) focusedBox.getModel();
                        lineChartModel.setDisplayName(projectTreeModel.getSelectedPath().getName());
                        lineChartModel.setDescription(projectTreeModel.getSelectedPath().getFolder());
                        lineChartModel.setLog("mueller".equals(projectTreeModel.getSelectedPath().getName()));
                        lineChartModel.loadFromFileAsync(projectTreeModel.getSelectedPath().getFolder());
                    } else {
                        focusedBox = new NumberedTextBox(projectTreeModel.getSelectedPath().getName());
                        focusedBox.init();
                        final NumberedTextModel numberedTextModel = (NumberedTextModel) focusedBox.getModel();
                        numberedTextModel.setDisplayName(projectTreeModel.getSelectedPath().getName());
                        numberedTextModel.setDescription(projectTreeModel.getSelectedPath().getFolder());
                        numberedTextModel.bindWithFile(projectTreeModel.getSelectedPath().getFolder());
                    }



                    boxes.put(projectTreeModel.getSelectedPath(), focusedBox);
                    keys.add(projectTreeModel.getSelectedPath());
                }
                setFocusedBox(focusedBox);
                javax.swing.SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        Context.getInstance().getMainForm().setLoadingVisible(false);
                    }
                });
            }

        }

    }
}
