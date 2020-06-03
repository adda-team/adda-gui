package adda.base.views;

import adda.base.models.IModel;
import adda.base.models.IModelObserver;

import javax.swing.*;

public interface IView extends IModelObserver {
    void refresh();

    JComponent getRootComponent();

    void initFromModel(IModel model);
}
