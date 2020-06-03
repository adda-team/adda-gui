package adda.item.tab.internals.accuracy;

import adda.base.AddaOption;
import adda.base.IAddaOption;
import adda.base.annotation.Viewable;
import adda.base.models.ModelBaseAddaOptionsContainer;

import java.util.Arrays;
import java.util.List;

public class AccuracyModel extends ModelBaseAddaOptionsContainer {

    public static final String EPS_FIELD_NAME = "eps";
    public static final String EPS_FORMAT = "1E-%s";
    @Viewable("exp(-eps), eps:")
    protected int eps = 5;

    public AccuracyModel() {
        this.setLabel("Accuracy (iter. solver)");//todo localization
    }

    public int getEps() {
        return eps;
    }

    public void setEps(int eps) {
        if (this.eps != eps) {
            this.eps = eps;
            notifyObservers(EPS_FIELD_NAME, eps);
        }
    }

    @Override
    public void applyDefaultState() {
        setEps(5);
    }

    @Override
    public boolean isDefaultState() {
        return eps == 5;
    }

    @Override
    protected List<IAddaOption> getAddaOptionsInner() {
        IAddaOption addaOption = new AddaOption(EPS_FIELD_NAME, String.valueOf(eps), String.format(EPS_FORMAT, eps));
        return Arrays.asList(addaOption);
    }
}