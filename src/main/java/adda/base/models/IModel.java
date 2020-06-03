package adda.base.models;

import java.util.Map;

public interface IModel extends Cloneable {

    String getGroupName();

    String getLabel();

    Map<String, Class> getViewableProperties();
    Map<String, String> getBindProperties();
    String getViewableLabel(String field);

    void addObserver(IModelObserver observer);

    void removeObserver(IModelObserver observer);

    void copyProperties(IModel model);

    void applyDefaultState();

    boolean isDefaultState();

    Object clone() throws CloneNotSupportedException;
//    void notifyObservers();


}
