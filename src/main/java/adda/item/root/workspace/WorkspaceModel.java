package adda.item.root.workspace;

import adda.base.boxes.BoxBase;
import adda.base.boxes.IBox;
import adda.base.events.IModelPropertyChangeEvent;
import adda.base.models.IModel;
import adda.base.models.ModelBase;
import adda.base.models.IModelObserver;
import adda.item.root.projectArea.ProjectAreaBox;
import adda.item.root.projectArea.ProjectAreaModel;
import adda.item.root.projectTree.ProjectTreeModel;

import java.util.*;

public class WorkspaceModel extends ModelBase implements IModelObserver {
    protected Map<String, IBox> boxes = new HashMap<>(); //todo may be initialization can delete
    protected List<String> keys = new ArrayList<String>(); //todo may be initialization can delete
    protected IBox focusedBox;
    public Map<String, IBox> getBoxes() {
        return Collections.unmodifiableMap(boxes);
    }
    public IBox getFocusedBox() {
        return focusedBox;
    }

    public void removeBoxByPath(String path) {
        IBox box = boxes.get(path);
        boxes.remove(path, box);
        keys.remove(path);//todo if different boxes has the same keys? maybe replace map to other data structure
    }

    public String getPathByBox(IBox box) {
        if (box == null) {
            return null;
        }
        Optional<String> result = boxes.entrySet()
                .stream()
                .filter(entry -> box.equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .findFirst();

        if (!result.isPresent())
            return null;
        String path = result.get();
        return path;
    }

    public void selectBoxByPath(String path) {
        if (path == null) {
            return;
        }
        Optional<String> result = boxes.entrySet()
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
        if((this.focusedBox != null && !this.focusedBox.equals(focusedBox))
                || (this.focusedBox == null && focusedBox != null)) { //todo if condition to func
            setAreaActive(false);
            this.focusedBox = focusedBox;
            notifyObservers("focusedBox", focusedBox);
            setAreaActive(true);
        }
    }

    private void setAreaActive(boolean areaActive) {
        if (this.focusedBox != null && this.focusedBox.getModel() instanceof ProjectAreaModel) {
            ((ProjectAreaModel) this.focusedBox.getModel()).setActive(areaActive);
        }
    }

    @Override
    public void modelPropertyChanged(IModel sender, IModelPropertyChangeEvent event) {
        if(sender instanceof ProjectTreeModel) {
            if (event.getPropertyName().equals("selectedPath")) {
                ProjectTreeModel projectTreeModel = (ProjectTreeModel) sender;
                IBox focusedBox;
                if (boxes.containsKey(projectTreeModel.getSelectedPath())) {
                    focusedBox = boxes.get(projectTreeModel.getSelectedPath());

                } else {
                    //todo get selected item type then get right box (may be fabric method)
                    focusedBox = new ProjectAreaBox(projectTreeModel.getSelectedPath());
                    focusedBox.init();
                    boxes.put(projectTreeModel.getSelectedPath(), focusedBox);
                    keys.add(projectTreeModel.getSelectedPath());
                }
                setFocusedBox(focusedBox);
            }
        }
    }
}
