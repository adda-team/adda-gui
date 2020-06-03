package adda.base.events;

public interface IModelPropertyChangeEvent {
    String getPropertyName();
    Object getPropertyValue();
    ModelPropertyChangeType getPropertyChangeType();
}


