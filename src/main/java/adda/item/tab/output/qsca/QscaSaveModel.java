package adda.item.tab.output.qsca;

import adda.base.AddaOption;
import adda.base.IAddaOption;
import adda.base.IAddaOptionsContainer;
import adda.item.tab.BooleanFlagModel;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class QscaSaveModel extends BooleanFlagModel {

    public static final String CSCA = "Csca";

    public QscaSaveModel() {
        this.setLabel("Scattering cross section");//todo localization
    }


    @Override
    protected String getAddaCommand() {
        return CSCA;
    }
}