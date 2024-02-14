package model;

import model.policy.RuleSetModel;
import org.junit.jupiter.api.*;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class RuleSetTest {
    RuleSetModel r1, r2, r3, r4, r5;
    HashSet<String> act1, act2, act3, act4;
    @BeforeEach
    public void init() {
        act1 = new HashSet<>();
        act2 = new HashSet<>();
        act3 = new HashSet<>();
        act4 = new HashSet<>();
        act1.add("testact");
        act2.add("testact");
        act2.add("testact2");
        act3.add("testact2");
        act3.add("testact3");
        act4.add("testact");
        act4.add("testact2");
        act4.add("testact3");
        r1 = new RuleSetModel(RuleSetModel.RuleType.allow,
                "test_s_t",
                "test_t_t",
                "test",
                act1);
        r2 = new RuleSetModel(RuleSetModel.RuleType.allow,
                "test_s_t",
                "test_t_t",
                "test",
                act1);
        r3 = new RuleSetModel(RuleSetModel.RuleType.allow,
                "test_s_t",
                "test_t_t",
                "test",
                act2);
        r4 = new RuleSetModel(RuleSetModel.RuleType.allow,
                "test_s_t",
                "test_t_t",
                "test",
                act4);
        r5 = new RuleSetModel(RuleSetModel.RuleType.allow,
                "test_s2_t",
                "test_t2_t",
                "test",
                act4);
    }

    @Test
    public void testAddActionSingle() {
        r1.addAction("testact");
        assertTrue(r1.equals(r2));
        r1.addAction("testact2");
        assertTrue(r1.equals(r3));

        assertFalse(r1.equals(r4));
    }

    @Test
    public void testAddActionBatch() {
        r1.addAction(act3);
        assertTrue(r1.equals(r4));
    }

    @Test
    public void testIsEquvStatement() {
        assertTrue(RuleSetModel.isEquvStatement(r1, r4));
        assertFalse(RuleSetModel.isEquvStatement(r1, r5));
    }

    @Test
    public void testRuleToString() {
        assertEquals("allow test_s_t test_t_t:test { testact };", r1.toString());
        assertEquals("allow test_s_t test_t_t:test { testact testact2 };", r3.toString());
    }
}
