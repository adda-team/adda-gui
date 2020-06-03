package adda.item.tab.output.theta;

import adda.base.AddaOption;
import adda.base.IAddaOption;
import adda.base.annotation.Viewable;
import adda.base.models.ModelBase;
import adda.item.tab.TabEnumModel;

import java.util.Arrays;
import java.util.List;

public class ThetaSaveModel extends TabEnumModel<ThetaSaveEnum> {

    public static final String NTHETA = "ntheta";

    public ThetaSaveModel() {
        this.setLabel("Theta count");//todo localization
        setEnumValue(ThetaSaveEnum.n_default);
        setDefaultEnumValue(ThetaSaveEnum.n_default);
    }

    @Override
    protected List<IAddaOption> getAddaOptionsInner() {
        final String value = enumValue.toString().replaceFirst("n_", "");
        IAddaOption addaOption = new AddaOption(NTHETA, value, value);
        return Arrays.asList(addaOption);
    }
}