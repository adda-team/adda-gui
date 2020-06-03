package adda.item.tab.output.internalField;

import adda.base.AddaOption;
import adda.base.IAddaOption;
import adda.base.IAddaOptionsContainer;
import adda.item.tab.BooleanFlagModel;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InternalFieldSaveModel extends BooleanFlagModel {

    public static final String STORE_INT_FIELD = "store_int_field";

    public InternalFieldSaveModel() {
        this.setLabel("Internal field");//todo localization
    }


    @Override
    protected String getAddaCommand() {
        return STORE_INT_FIELD;
    }
}