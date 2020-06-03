package adda.application.controls;

import java.util.EventObject;

public class  CloseTabEvent <T> extends EventObject {

    private T assotiatedObject;
    private int index;

    public int getIndex() {
        return index;
    }

    public Object getAssotiatedObject() {
        return assotiatedObject;
    }

    public CloseTabEvent(Object source, T assotiatedObject, int index) {
        super(source);
        this.assotiatedObject = assotiatedObject;
        this.index = index;
    }
}
