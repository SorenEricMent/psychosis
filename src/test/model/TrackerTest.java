package model;

import model.exception.NotFoundException;
import model.policy.InterfaceModel;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TrackerTest {
    private TrackerModel testTracker1;
    private InterfaceModel testInf1, testInf2;
    @BeforeEach
    public void init() {
        testTracker1 = new TrackerModel();
        testInf1 = new InterfaceModel("testinf1", false);
        testInf2 = new InterfaceModel("testinf2", false);
    }
    @Test
    public void testLabelTracker() {
        // Fundamentally there is no difference
        // between SL and TL, so they are integrated into one test

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
        testTracker1.insertInterfaceWithSLabel("test2_t", testInf1);
        testTracker1.insertInterfaceWithTLabel("test_t", testInf2);
    }
    @Test
    public void testTagTracker() {
        ArrayList<InterfaceModel> expected1 = new ArrayList<InterfaceModel>();
        ArrayList<InterfaceModel> expected2 = new ArrayList<InterfaceModel>();
        expected1.add(testInf1);
        expected1.add(testInf2);
        expected2.add(testInf2);

        testTracker1.insertInterfaceWithTag("testtag1", testInf1);
        testTracker1.insertInterfaceWithTag("testtag1", testInf2);
        testTracker1.insertInterfaceWithTag("testtag2", testInf2);

        assertThrows(NotFoundException.class, () -> {
          testTracker1.queryInterfaceWithTag("tag");
        });

        assertEquals(expected1, testTracker1.queryInterfaceWithTag("testtag1"));
        assertEquals(expected2, testTracker1.queryInterfaceWithTag("testtag2"));

    }
}