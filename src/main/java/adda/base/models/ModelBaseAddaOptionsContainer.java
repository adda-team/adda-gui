package adda.base.models;

import adda.base.IAddaOption;
import adda.base.IAddaOptionsContainer;
import adda.base.events.ModelPropertyChangeType;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public abstract class ModelBaseAddaOptionsContainer extends ModelBase implements IAddaOptionsContainer {

    public static final String DELIMITER = ", ";

    protected volatile boolean isNeedCaching = false;
    protected volatile List<IAddaOption> cachedList;
    protected volatile String cachedDescription;

    @Override
    protected void notifyObservers(String propertyName, Object propertyValue) {
        cacheListAndDescription();
        super.notifyObservers(propertyName, propertyValue);
    }




    @Override
    protected void notifyObservers(String propertyName, Object propertyValue, ModelPropertyChangeType propertyChangeType) {
        cachedList = getAddaOptionsInner();
        super.notifyObservers(propertyName, propertyValue, propertyChangeType);
    }

    @Override
    public String getDescription() {
        return cachedDescription;
    }

    @Override
    public List<IAddaOption> getAddaOptions() {

        if(!isDefaultState()) {
            if(cachedList == null) {
                isNeedCaching = true;
                cacheListAndDescription();
            }
            return cachedList;
        }
        return Collections.emptyList();
    }

    protected abstract List<IAddaOption> getAddaOptionsInner();


    private void cacheListAndDescription() {
        if (isNeedCaching) {
            cachedList = getAddaOptionsInner();
            cachedDescription =
                    getAddaOptions()
                    .stream()
                    .map(IAddaOption::getDisplayString)
                    .collect(Collectors.joining(DELIMITER));
        }
    }
}
