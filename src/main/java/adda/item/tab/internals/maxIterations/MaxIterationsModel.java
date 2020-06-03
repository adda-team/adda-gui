package adda.item.tab.internals.maxIterations;

import adda.base.annotation.BindEnableFrom;
import adda.base.annotation.Viewable;
import adda.item.tab.BooleanFlagModel;

import java.util.*;

public class MaxIterationsModel extends BooleanFlagModel {

    public static final String MAX_ITERATIONS_FIELD_NAME = "maxIterations";
    public static final String MAXITER = "maxiter";
    @BindEnableFrom("flag")
    @Viewable(order = 9)
    int maxIterations = 1000;

    public MaxIterationsModel() {
        this.setLabel("Maximum iterations");//todo localization
    }

    public int getMaxIterations() {
        return maxIterations;
    }

    public void setMaxIterations(int maxIterations) {
        if (this.maxIterations == 0 || this.maxIterations != maxIterations) {
            this.maxIterations = maxIterations;
            notifyObservers(MAX_ITERATIONS_FIELD_NAME, maxIterations);
        }
    }

    @Override
    protected String getAddaCommand() {
        return MAXITER;
    }

    @Override
    protected String getAddaValue() {
        return String.valueOf(maxIterations);
    }

    @Override
    protected String getAddaDescription() {
        return getAddaValue();
    }

    //    @Override
//    public Map<String, Class> getViewableProperties() {
//        LinkedHashMap<String, Class> reversedMap = new LinkedHashMap<>();
//        List<String> reverseOrderedKeys = new ArrayList<>(typeMap.keySet());
//        Collections.reverse(reverseOrderedKeys);
//        reverseOrderedKeys.forEach((key)->reversedMap.put(key,typeMap.get(key)));
//        return reversedMap;
//    }
}