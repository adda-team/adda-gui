package adda.base.boxes;

import adda.base.models.IModel;
import adda.base.IModelState;

import javax.swing.*;
import java.util.List;

public interface IBox {
    String getName();

    JPanel getLayout();

    void init();

    IModelState getState();

    boolean setState(IModelState model);

    void setParent(IBox parent);

    IBox getParent();

    IModel getModel();

    List<IBox> getChildren();

    void addChild(IBox child);

    void removeChild(IBox child);

    void clearChildren();


}
