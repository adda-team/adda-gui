package adda.base.events;

public class ModelPropertyChangedEvent implements IModelPropertyChangeEvent {

    private Object propertyValue;
    private String propertyName;
    private ModelPropertyChangeType propertyChangeType;

    public ModelPropertyChangedEvent(String propertyName, Object propertyValue) {
        this.propertyValue = propertyValue;
        this.propertyName = propertyName;
        propertyChangeType = ModelPropertyChangeType.CHANGE;
    }

    public ModelPropertyChangedEvent(String propertyName, Object propertyValue, ModelPropertyChangeType propertyChangeType) {
        this.propertyValue = propertyValue;
        this.propertyName = propertyName;
        this.propertyChangeType = propertyChangeType;
    }

    private ModelPropertyChangedEvent() {
    }

    @Override
    public Object getPropertyValue() {
        return propertyValue;
    }

    @Override
    public ModelPropertyChangeType getPropertyChangeType() {
        return propertyChangeType;
    }

    @Override
    public String getPropertyName() {
        return this.propertyName;
    }


}
