package adda.item.tab.options;

import adda.Context;
import adda.base.IAddaOptionsContainer;
import adda.base.events.IModelPropertyChangeEvent;
import adda.base.events.ModelPropertyChangeType;
import adda.base.models.IModel;
import adda.base.models.IModelObserver;
import adda.base.models.ModelBase;
import adda.item.root.projectArea.ProjectAreaForm;
import adda.item.root.projectArea.ProjectAreaModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OptionsModel extends ModelBase implements IModelObserver {


    public static final String ADDA_OPTIONS_FIELD_NAME = "addaOptions";
    public static final String DELIMITER = " ";
    private List<IAddaOptionsContainer> containers = new ArrayList<>();

    public void add(IAddaOptionsContainer addaOptionsContainer) {
        if (addaOptionsContainer == null) return;

        containers.add(addaOptionsContainer);
        notifyObservers(ADDA_OPTIONS_FIELD_NAME, addaOptionsContainer, ModelPropertyChangeType.ADD);
    }

    public void remove(IAddaOptionsContainer addaOptionsContainer) {
        if(!containers.contains(addaOptionsContainer)) return;

        containers.remove(addaOptionsContainer);
        notifyObservers(ADDA_OPTIONS_FIELD_NAME, addaOptionsContainer, ModelPropertyChangeType.REMOVE);
    }

    public List<IAddaOptionsContainer> getContainers() {
        return containers;//todo immutable
    }

    @Override
    public void modelPropertyChanged(IModel sender, IModelPropertyChangeEvent event) {
        if (sender instanceof IAddaOptionsContainer) {
            IAddaOptionsContainer container = (IAddaOptionsContainer) sender;

            final boolean contains = containers.contains(container);
            if (sender.isDefaultState()) {
                if (contains) {
                    remove(container);
                    setCommandLineToArea();
                }
            } else if (!contains) {
                add(container);
                setCommandLineToArea();
            } else {
                notifyObservers(ADDA_OPTIONS_FIELD_NAME, container);
                setCommandLineToArea();
            }
        }
        if (sender instanceof ProjectAreaModel) {
            if (((ProjectAreaModel) sender).isActive()) {
                setCommandLineToArea();
            } else {
                Context.getInstance()
                        .getMainForm()
                        .getActualCommandLineTextArea()
                        .setText("");
            }
        }
    }

    public void setCommandLineToArea() {
        Context.getInstance()
                .getMainForm()
                .getActualCommandLineTextArea()
                .setText(getActualCommandLine());
    }

    public String getActualCommandLine() {
        return containers
                .stream()
                .map(addaOptionsContainer -> addaOptionsContainer.getAddaOptions())
                .flatMap(List::stream)
                .map(addaOption -> addaOption.getFormatted())
                .collect(Collectors.joining(DELIMITER));
    }
}