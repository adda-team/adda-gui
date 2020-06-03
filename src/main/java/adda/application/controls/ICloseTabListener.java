package adda.application.controls;

import java.util.EventListener;

public interface ICloseTabListener extends EventListener {
    void tabClosed(CloseTabEvent closeTabEvent);
}
