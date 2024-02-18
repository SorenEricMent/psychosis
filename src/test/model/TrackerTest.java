package model;

import model.policy.InterfaceModel;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TrackerTest {
    private TrackerModel testTracker1;
    @BeforeEach
    public void init() {
        testTracker1 = new TrackerModel();
    }
    @Test
    public void testTracker() {
        // Fundamentally there is no difference
        // between SL and TL, so they are integrated into one test
        InterfaceModel testInf1 = new InterfaceModel("testinf1", false);
        InterfaceModel testInf2 = new InterfaceModel("testinf2", false);
        ArrayList<InterfaceModel> expected1 = new ArrayList<InterfaceModel>();
        ArrayList<InterfaceModel> expected2 = new ArrayList<InterfaceModel>();
        expected1.add(testInf1);
        expected2.add(testInf2);

        testTracker1.insertInterfaceWithSLabel("test_t", testInf1);
        testTracker1.insertInterfaceWithTLabel("test2_t", testInf2);

        assertNull(testTracker1.queryInterfaceWithSLabel("nonexist"));
        assertEquals(expected1,
                testTracker1.queryInterfaceWithSLabel("test_t"));
        assertEquals(expected2,
                testTracker1.queryInterfaceWithTLabel("test2_t"));
    }
}