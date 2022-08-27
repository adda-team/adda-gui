package adda.item.tab.shape.selector.params;

import adda.base.models.ModelBase;
import javax.media.j3d.*;
import javax.swing.*;
import javax.vecmath.*;
import java.util.ArrayList;
import java.util.List;

public abstract class ModelShapeParam extends ModelBase {

    public static final String FIRST_PARAM = "firstParam";
    public static final String SECOND_PARAM = "secondParam";
    public static final String THIRD_PARAM = "thirdParam";
    public static final String FOURTH_PARAM = "fourthParam";
    public static final String FIFTH_PARAM = "fifthParam";

    public abstract List<String> getParamsList();

    public void createSurfaceShape(TransformGroup transformGroup) {
        /// TODO temporary, must be abstract
    }

    public void createVoxelizedShape(TransformGroup transformGroup) {
        /// TODO temporary, must be abstract
    }
}