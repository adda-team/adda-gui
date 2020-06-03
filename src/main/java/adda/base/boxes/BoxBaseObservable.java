package adda.base.boxes;

import adda.base.events.IBoxChangeEvent;

import java.util.LinkedList;
import java.util.List;

public class BoxBaseObservable extends BoxBase implements IBoxObservable {
    protected List<IBoxObserver> observers;

    public BoxBaseObservable() {
        observers = new LinkedList<>();
    }

    @Override
    public void addObserver(IBoxObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(IBoxObserver observer) {
        observers.remove(observer);
    }

    protected void notifyObservers(IBoxChangeEvent event) {
        for (IBoxObserver observer : observers)
            observer.boxChanged(this, event);
    }
}
