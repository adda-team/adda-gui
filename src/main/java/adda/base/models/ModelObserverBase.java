package adda.base.models;

import adda.base.models.IModel;
import adda.base.events.IModelPropertyChangeEvent;
import adda.base.models.IModelObserver;

public class ModelObserverBase implements IModelObserver {
    @Override
    public void modelPropertyChanged(IModel sender, IModelPropertyChangeEvent event) {

    }
}
