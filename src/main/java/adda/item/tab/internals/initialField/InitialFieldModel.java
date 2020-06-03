package adda.item.tab.internals.initialField;

import adda.base.AddaOption;
import adda.base.IAddaOption;
import adda.item.tab.TabEnumModel;
import adda.utils.StringHelper;

import java.util.Arrays;
import java.util.List;

public class InitialFieldModel extends TabEnumModel<InitialFieldEnum> {

    public static final String INIT_FIELD = "init_field";

    public InitialFieldModel() {
        this.setLabel("Initial field");//todo localization
        setEnumValue(InitialFieldEnum.auto);
        setDefaultEnumValue(InitialFieldEnum.auto);
    }

    @Override
    protected List<IAddaOption> getAddaOptionsInner() {
        IAddaOption addaOption = new AddaOption(INIT_FIELD, enumValue.toString(), StringHelper.toDisplayString(enumValue));
        return Arrays.asList(addaOption);
    }
}