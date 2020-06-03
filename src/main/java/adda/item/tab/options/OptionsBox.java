package adda.item.tab.options;

import adda.base.annotation.*;
import adda.base.boxes.BoxBase;
import adda.base.boxes.IBox;

import javax.swing.*;
import java.awt.*;

@BindModel
@BindView
@BindController
public class OptionsBox extends BoxBase {

    @Override
    protected void initChildren() {

    }

    @Override
    protected JPanel getPanel() {
        return new JPanel(new BorderLayout());
    }


    public OptionsBox() {
        needInitSelf = true;
    }
}
