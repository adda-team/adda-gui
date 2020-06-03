package adda.item.tab.base.beam;

import adda.base.AddaOption;
import adda.base.IAddaOption;
import adda.base.models.ModelBase;
import adda.item.tab.TabEnumModel;
import adda.item.tab.base.dplGrid.DplGridEnum;
import adda.utils.StringHelper;

import java.util.Arrays;
import java.util.List;

public class BeamModel extends TabEnumModel<BeamEnum> {

    public static final String BEAM = "beam";

    public BeamModel() {
        setLabel("Beam");//todo localization
        setEnumValue(BeamEnum.plane);
        setDefaultEnumValue(BeamEnum.plane);
    }

    @Override
    protected List<IAddaOption> getAddaOptionsInner() {
        return Arrays.asList(new AddaOption(BEAM, enumValue.toString(), StringHelper.toDisplayString(enumValue)));
    }
}