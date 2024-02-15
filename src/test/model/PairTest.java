package model;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class PairTest {

    Pair<String, String> p1;
    Pair<String, Integer> p2;
    @BeforeEach
    public void init() {
        p1 = new Pair<String, String>("Yuuta", "Bendan");
        p2 = new Pair<String, Integer>("Yuuta_cute", 100);
    }

    @Test
    public void testPairTuple() {
        assertEquals(p1.getFirst(), "Yuuta");
        assertEquals(p1.getSecond(), "Bendan");
        assertEquals(p2.getFirst(), "Yuuta_cute");
        assertEquals(p2.getSecond(), 100);
    }
}
