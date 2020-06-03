package adda.base.events;

public class BoxChangeEvent implements IBoxChangeEvent {

    private Object newValue;
    private Object oldValue;

    public BoxChangeEvent(Object newValue, Object oldValue) {
        this.newValue = newValue;
        this.oldValue = oldValue;
    }

    @Override
    public Object getNewValue() {
        return newValue;
    }

    @Override
    public Object getOldValue() {
        return oldValue;
    }
}
