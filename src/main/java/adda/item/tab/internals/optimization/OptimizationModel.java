package adda.item.tab.internals.optimization;

import adda.base.AddaOption;
import adda.base.IAddaOption;
import adda.item.tab.TabEnumModel;
import adda.utils.StringHelper;

import java.util.Arrays;
import java.util.List;

public class OptimizationModel extends TabEnumModel<OptimizationEnum> {

    public static final String OPT = "opt";

    public OptimizationModel() {
        this.setLabel("Simulating optimization");//todo localization
        setEnumValue(OptimizationEnum.speed);
        setDefaultEnumValue(OptimizationEnum.speed);
    }

    @Override
    protected List<IAddaOption> getAddaOptionsInner() {
        IAddaOption addaOption = new AddaOption(OPT, enumValue.toString(), StringHelper.toDisplayString(enumValue));
        return Arrays.asList(addaOption);
    }
}