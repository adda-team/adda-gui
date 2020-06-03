package adda.item.tab.output.granul;

import adda.base.AddaOption;
import adda.base.IAddaOption;
import adda.base.IAddaOptionsContainer;
import adda.base.events.IModelPropertyChangeEvent;
import adda.base.models.IModel;
import adda.base.models.IModelObserver;
import adda.item.tab.BooleanFlagModel;
import adda.item.tab.shape.granules.GranulesModel;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GranulSaveModel extends BooleanFlagModel implements IModelObserver {

    public static final String STORE_GRANS = "store_grans";

    public GranulSaveModel() {
        this.setLabel("Granul centers");//todo localization
    }

    @Override
    protected String getAddaCommand() {
        return STORE_GRANS;
    }

    @Override
    public void modelPropertyChanged(IModel sender, IModelPropertyChangeEvent event) {
        if (sender instanceof GranulesModel) {
            setFlag(((GranulesModel) sender).isSave());
        }
    }
}