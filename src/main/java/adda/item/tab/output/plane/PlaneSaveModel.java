package adda.item.tab.output.plane;

import adda.base.AddaOption;
import adda.base.IAddaOption;
import adda.base.annotation.Viewable;
import adda.base.models.ModelBase;
import adda.item.tab.TabEnumModel;

import java.util.Arrays;
import java.util.List;

public class PlaneSaveModel extends TabEnumModel<PlaneSaveEnum> {

    public PlaneSaveModel() {
        this.setLabel("Scattering plane");//todo localization
        setEnumValue(PlaneSaveEnum.xy);
        setDefaultEnumValue(PlaneSaveEnum.xy);
    }

    @Override
    protected List<IAddaOption> getAddaOptionsInner() {
        IAddaOption addaOption = new AddaOption(enumValue.toString(), null, enumValue.toString());
        return Arrays.asList(addaOption);
    }
}