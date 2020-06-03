package adda.item.tab.output.radiationForce;

import adda.item.tab.BooleanFlagModel;

public class RadiationForceSaveModel extends BooleanFlagModel {

    public static final String STORE_FORCE = "store_force";

    public RadiationForceSaveModel() {
        this.setLabel("Total radiation force");//todo localization
    }

    @Override
    protected String getAddaCommand() {
        return STORE_FORCE;
    }
}