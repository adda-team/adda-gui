package adda.base.boxes;

import adda.base.boxes.IBox;
import adda.base.events.IBoxChangeEvent;

public interface IBoxObserver {
    void boxChanged(IBox sender, IBoxChangeEvent event);
}
