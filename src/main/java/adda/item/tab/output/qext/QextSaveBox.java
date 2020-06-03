package adda.item.tab.output.qext;

import adda.base.annotation.*;
import adda.base.boxes.BoxBase;
import adda.item.tab.output.qabs.QabsSaveView;

@BindModel
@BindView(QabsSaveView.class)
@BindController
public class QextSaveBox extends BoxBase {

    public QextSaveBox() {
        needInitSelf = true;
    }
}
