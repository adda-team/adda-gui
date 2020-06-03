package adda.utils;

import adda.base.boxes.IBox;
import adda.base.models.IModel;
import adda.base.models.IModelObserver;

public class Binder {
    public static void bind(IBox source, IBox observer) {
        if (source != null && observer != null && source.getModel() != null && observer.getModel() instanceof IModelObserver) {
            source.getModel().addObserver((IModelObserver)observer.getModel());
        }
    }

    public static void unbind(IBox source, IBox observer) {
        if (source != null && observer != null && source.getModel() != null && observer.getModel() instanceof IModelObserver) {
            source.getModel().removeObserver((IModelObserver)observer.getModel());
        }
    }

    public static void bindBoth(IBox first, IBox second) {
        bind(first, second);
        bind(second, first);
    }

    public static void unbindBoth(IBox first, IBox second) {
        unbind(first, second);
        unbind(second, first);
    }

    public static void bind(IModel source, IModel observer) {
        if (source != null && observer instanceof IModelObserver) {
            source.addObserver((IModelObserver)observer);
        }
    }

    public static void unbind(IModel source, IModel observer) {
        if (source != null && observer instanceof IModelObserver) {
            source.removeObserver((IModelObserver)observer);
        }
    }

    public static void bindBoth(IModel first, IModel second) {
        bind(first, second);
        bind(second, first);
    }

    public static void unbindBoth(IModel first, IModel second) {
        bind(first, second);
        bind(second, first);
    }
}
