package adda.item.tab.output.polarization;

import adda.base.AddaOption;
import adda.base.IAddaOption;
import adda.base.IAddaOptionsContainer;
import adda.item.tab.BooleanFlagModel;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PolarizationSaveModel extends BooleanFlagModel {

    public static final String STORE_DIP_POL = "store_dip_pol";

    public PolarizationSaveModel() {
        this.setLabel("Dipoles polarization");//todo localization
    }


    @Override
    protected String getAddaCommand() {
        return STORE_DIP_POL;
    }
}