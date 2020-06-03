package adda.item.tab.output.qabs;

import adda.base.annotation.BindEnableFrom;
import adda.item.tab.BooleanFlagModel;

public class QabsSaveModel extends BooleanFlagModel {

    public QabsSaveModel() {
        this.setLabel("Absorbtion cross section");//todo localization
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