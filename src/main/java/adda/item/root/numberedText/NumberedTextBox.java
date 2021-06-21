package adda.item.root.numberedText;

import adda.base.annotation.*;
import adda.base.boxes.BoxBase;

@BindModel
@BindView
@BindController
public class NumberedTextBox extends BoxBase {

    public NumberedTextBox(String name) {
        this();
        this.name = name;
    }
    public NumberedTextBox() {
        needInitSelf = true;
    }
}
