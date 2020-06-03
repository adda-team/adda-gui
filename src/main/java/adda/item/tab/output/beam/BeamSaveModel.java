package adda.item.tab.output.beam;

import adda.base.AddaOption;
import adda.base.IAddaOption;
import adda.base.IAddaOptionsContainer;
import adda.item.tab.BooleanFlagModel;
import adda.item.tab.output.geometry.GeometrySaveEnum;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BeamSaveModel extends BooleanFlagModel {

    public static final String SAVE_BEAM = "save_beam";

    public BeamSaveModel() {
        this.setLabel("Beam");//todo localization
    }


    @Override
    protected String getAddaCommand() {
        return SAVE_BEAM;
    }
}
