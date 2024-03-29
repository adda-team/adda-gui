package adda.base.models;

import java.util.List;
import java.util.Map;

public interface IModel extends Cloneable {

    String getGroupName();

    String getLabel();

    boolean validate();

    void forceVerify();

    boolean isVisibleIfDisabled();

    Map<String, String> getValidationErrors();

    Map<String, Class> getViewableProperties();

    Map<String, String> getBindProperties();

    String getViewableLabel(String field);

    void addObserver(IModelObserver observer);

    void removeObserver(IModelObserver observer);

    void copyProperties(IModel model);

    boolean isUnderCopy();

    void applyDefaultState();

    boolean isDefaultState();

    Object clone() throws CloneNotSupportedException;
//    void notifyObservers();
}
