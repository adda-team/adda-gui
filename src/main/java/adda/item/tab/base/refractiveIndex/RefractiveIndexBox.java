package adda.item.tab.base.refractiveIndex;

import adda.base.annotation.*;
import adda.base.boxes.BoxBase;

import java.io.Serializable;

@BindModel
@BindView
@BindController
public class RefractiveIndexBox extends BoxBase implements Serializable {

    public RefractiveIndexBox() {
        needInitSelf = true;
    }
}
