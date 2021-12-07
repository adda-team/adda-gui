package adda.item.tab.internals.reducedFFT;

import adda.base.models.ModelBase;
import adda.item.tab.BooleanFlagModel;

public class ReducedFftModel  extends BooleanFlagModel {

    public static final String NO_REDUCED_FFT = "no_reduced_fft";

    public ReducedFftModel() {
        this.setLabel("Reduce FFT");//<html>Use symmetry of <br>interaction matrix<br> to reduce FFT</html>todo localization
        setFlag(true);
    }

    @Override
    public void applyDefaultState() {
        setFlag(true);
    }

    @Override
    public boolean isDefaultState() {
        return getFlag();
    }

    @Override
    protected String getAddaCommand() {
        return NO_REDUCED_FFT;
    }
}