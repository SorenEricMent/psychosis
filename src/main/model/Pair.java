package model;

// A 2-tuple
// It is a huge pain that java doesn't have Tuple
public class Pair<X, Y> {
    private final X first;
    private final Y second;

    // EFFECTS: init the 2-tuple with two elements
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
