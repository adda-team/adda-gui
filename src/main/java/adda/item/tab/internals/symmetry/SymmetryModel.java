package adda.item.tab.internals.symmetry;

import adda.base.AddaOption;
import adda.base.IAddaOption;
import adda.item.tab.TabEnumModel;
import adda.utils.StringHelper;

import java.util.Arrays;
import java.util.List;

public class SymmetryModel extends TabEnumModel<SymmetryEnum> {

    public static final String SYM = "sym";

    public SymmetryModel() {
        this.setLabel("Shape symmetry type");//todo localization
        setEnumValue(SymmetryEnum.auto);
        setDefaultEnumValue(SymmetryEnum.auto);
    }

    @Override
    protected List<IAddaOption> getAddaOptionsInner() {
        IAddaOption addaOption = new AddaOption(SYM, enumValue.toString(), StringHelper.toDisplayString(enumValue));
        return Arrays.asList(addaOption);
    }
}