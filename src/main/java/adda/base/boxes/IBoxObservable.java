package adda.base.boxes;

public interface IBoxObservable {
    void addObserver(IBoxObserver observer);

    void removeObserver(IBoxObserver observer);
}
