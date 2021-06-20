package adda.item.root.lineChart;

import adda.base.annotation.*;
import adda.base.boxes.BoxBase;

@BindModel
@BindView
@BindController
public class LineChartBox extends BoxBase {

    public LineChartBox(String name) {
        this();
        this.name = name;
    }
    public LineChartBox() {
        needInitSelf = true;
    }
}
