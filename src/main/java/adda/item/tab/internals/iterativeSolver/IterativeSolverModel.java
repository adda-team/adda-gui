package adda.item.tab.internals.iterativeSolver;

import adda.base.AddaOption;
import adda.base.IAddaOption;
import adda.base.annotation.Viewable;
import adda.item.tab.TabEnumModel;
import adda.utils.StringHelper;

import java.util.ArrayList;
import java.util.List;

public class IterativeSolverModel extends TabEnumModel<IterativeSolverEnum> {

    public static final String IS_RECALC_RESID_FIELD_NAME = "isRecalcResid";
    public static final String ITER = "iter";
    public static final String RECALC_RESID = "recalc_resid";
    public static final String TRUE_RESIDUAL_WILL_BE_CALCULATED = "true residual will be calculated";
    @Viewable(value = "recalc resid", order = 9)
    boolean isRecalcResid = false;

    public IterativeSolverModel() {
        this.setLabel("Iterative solver");//todo localization
        setEnumValue(IterativeSolverEnum.qmr);
        setDefaultEnumValue(IterativeSolverEnum.qmr);
    }

    public boolean getRecalcResid() {
        return isRecalcResid;
    }

    public void setRecalcResid(boolean recalcResid) {
        if(this.isRecalcResid != recalcResid) {
            this.isRecalcResid = recalcResid;
            notifyObservers(IS_RECALC_RESID_FIELD_NAME, this.isRecalcResid);
        }

    }

    @Override
    public boolean isDefaultState() {
        return super.isDefaultState() && !isRecalcResid;
    }

    @Override
    public void applyDefaultState() {
        super.applyDefaultState();
        setRecalcResid(false);
    }

    @Override
    protected List<IAddaOption> getAddaOptionsInner() {
        List<IAddaOption> list = new ArrayList<>();
        if (!super.isDefaultState()) {
            list.add(new AddaOption(ITER, enumValue.toString(), StringHelper.toDisplayString(enumValue)));
        }
        if (isRecalcResid) {
            list.add(new AddaOption(RECALC_RESID, null, StringHelper.toDisplayString(TRUE_RESIDUAL_WILL_BE_CALCULATED)));
        }

        return list;
    }

}
