package adda.base.models;

import adda.base.events.IModelPropertyChangeEvent;
import adda.base.models.IModel;

public interface IModelObserver {
    void modelPropertyChanged(IModel sender, IModelPropertyChangeEvent event);
}
