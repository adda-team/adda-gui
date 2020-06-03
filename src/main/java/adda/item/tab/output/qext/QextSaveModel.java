package adda.item.tab.output.qext;

import adda.base.annotation.BindEnableFrom;
import adda.item.tab.BooleanFlagModel;

public class QextSaveModel extends BooleanFlagModel {

    public QextSaveModel() {
        this.setLabel("Extinction cross section");//todo localization
        setFlag(true);
    }

    @Override
    protected String getAddaCommand() {
        return null;
    }

    @Override
    public boolean isDefaultState() {
        return true;
    }

    @Override
    public void applyDefaultState() {

    }
}