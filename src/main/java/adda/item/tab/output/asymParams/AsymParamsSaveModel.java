package adda.item.tab.output.asymParams;

import adda.base.AddaOption;
import adda.base.IAddaOption;
import adda.base.IAddaOptionsContainer;
import adda.base.annotation.Viewable;
import adda.base.models.ModelBase;
import adda.base.models.ModelBaseAddaOptionsContainer;
import adda.item.tab.output.geometry.GeometrySaveEnum;
import adda.utils.StringHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AsymParamsSaveModel extends ModelBaseAddaOptionsContainer {

    public static final String ASYM = "asym";
    public static final String USUAL = "usual";
    public static final String VEC = "vec";
    public static final String NORM = "norm";
    public static final String IS_NORM_FIELD_NAME = "isNorm";
    public static final String IS_USUAL_FIELD_NAME = "isUsual";

    @Viewable(NORM)
    boolean isNorm = false;

    @Viewable("usual")
    boolean isUsual = false;

    public AsymParamsSaveModel() {
        this.setLabel("Assymetry parameter");//todo localization
    }

    public boolean isNorm() {
        return isNorm;
    }

    public void setNorm(boolean norm) {
        if(isNorm != norm) {
            this.isNorm = norm;
            notifyObservers(IS_NORM_FIELD_NAME, isNorm);
        }
    }

    public boolean isUsual() {
        return isUsual;
    }

    public void setUsual(boolean usual) {
        if(isUsual != usual) {
            this.isUsual = usual;
            notifyObservers(IS_USUAL_FIELD_NAME, isUsual);
        }
    }

    @Override
    public boolean isDefaultState() {
        return !isNorm && !isUsual;
    }

    @Override
    public void applyDefaultState() {
        setNorm(false);
        setUsual(false);
    }


    @Override
    protected List<IAddaOption> getAddaOptionsInner() {
        List<IAddaOption> list = new ArrayList<>();
        if (isUsual) {
            list.add(new AddaOption(ASYM, null, StringHelper.toDisplayString(USUAL)));
        }
        if (isNorm) {
            list.add(new AddaOption(VEC, null, StringHelper.toDisplayString(NORM)));
        }
        return list;
    }

}