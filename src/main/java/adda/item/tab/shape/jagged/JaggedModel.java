package adda.item.tab.shape.jagged;

import adda.base.annotation.BindEnableFrom;
import adda.base.annotation.Viewable;
import adda.item.tab.BooleanFlagModel;

public class JaggedModel  extends BooleanFlagModel {

    public static final String JAGGED_FIELD_NAME = "jagged";
    public static final String JAGGED = "jagged";
    @BindEnableFrom("flag")
    @Viewable(order = 9)
    int jagged = 2;

    public JaggedModel() {
        this.setLabel("Jagged");//todo localization
    }

    public int getJagged() {
        return jagged;
    }

    public void setJagged(int jagged) {
        if (this.jagged == 0 || this.jagged != jagged) {
            this.jagged = jagged;
            notifyObservers(JAGGED_FIELD_NAME, jagged);
        }
    }

    @Override
    protected String getAddaCommand() {
        return JAGGED;
    }

    @Override
    protected String getAddaValue() {
        return String.valueOf(jagged);
    }

    @Override
    protected String getAddaDescription() {
        return getAddaValue();
    }


}