package model;

public class Pair<X, Y> {
    // It is a huge pain that java doesn't have Tuple
    private final X first;
    private final Y second;

    public Pair(X first, Y second) {
        this.first = first;
        this.second = second;
    }

    public X getFirst() {
        return first;
    }

    public Y getSecond() {
        return second;
    }
}
