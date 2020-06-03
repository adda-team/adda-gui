package adda.item.tab.output.scatteringMatrix;

import adda.base.AddaOption;
import adda.base.IAddaOption;
import adda.item.tab.TabEnumModel;
import adda.item.tab.base.dplGrid.DplGridEnum;
import adda.utils.StringHelper;

import java.util.Arrays;
import java.util.List;

public class ScatteringMatrixSaveModel extends TabEnumModel<ScatteringMatrixEnum> {

    public static final String SCAT_MATR = "scat_matr";

    public ScatteringMatrixSaveModel() {
        this.setLabel("Scattering matrix");//todo localization
        setEnumValue(ScatteringMatrixEnum.muel);
        setDefaultEnumValue(ScatteringMatrixEnum.muel);
    }

    @Override
    protected List<IAddaOption> getAddaOptionsInner() {
        IAddaOption addaOption = new AddaOption(SCAT_MATR,enumValue.toString(), StringHelper.toDisplayString(enumValue));
        return Arrays.asList(addaOption);
    }

}