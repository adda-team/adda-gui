package adda.item.tab.internals.volCorrection;

import adda.item.tab.BooleanFlagModel;

public class VolCorrectionModel extends BooleanFlagModel {

    public static final String NO_VOL_COR = "no_vol_cor";

    public VolCorrectionModel() {
        this.setLabel("Volume correction");//todo localization
        setFlag(true);
    }

    @Override
    public void applyDefaultState() {
        setFlag(true);
    }

    @Override
    public boolean isDefaultState() {
        return getFlag();
    }

    @Override
    protected String getAddaCommand() {
        return NO_VOL_COR;
    }
}