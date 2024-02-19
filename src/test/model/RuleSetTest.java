package model;

import model.exception.NotFoundException;
import model.exception.SyntaxParseException;
import model.policy.AccessVectorModel;
import model.policy.RuleSetModel;
import org.junit.jupiter.api.*;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class RuleSetTest {
    RuleSetModel r1, r2, r3, r4, r5, r6, r7;
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
        r6 = new RuleSetModel(RuleSetModel.RuleType.dontaudit,
                "test_s2_t",
                "test_t2_t",
                "test",
                act4);
        r7 = new RuleSetModel(RuleSetModel.RuleType.dontaudit,
                "test_s2_t",
                "test_t_t",
                "test",
                act4);
    }

    @Test
    public void testAddActionSingle() {
        r1.addAction("testact");
        assertTrue(r1.equals(r2));
        r1.addAction("testact2");
        assertTrue(r1.equals(r3));
        assertFalse(r1.equals(r6));
        assertFalse(r6.equals(r7));
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
        assertFalse(RuleSetModel.isEquvStatement(r6, r7));
        assertFalse(RuleSetModel.isEquvStatement(r1, r7));
    }

    @Test
    public void testRuleToString() {
        assertEquals("allow test_s_t test_t_t:test { testact };", r1.toString());
        assertEquals("allow test_s_t test_t_t:test { testact testact2 };", r3.toString());
    }

    @Test
    public void testRuleSetParser() {
        String t1 = "allow yuuta_t winslow_t:winslow { hug };";
        String t2 = "allow yuuta_t winslow_t:winslow { { hug } pet };";
        String t3 = "dontaudit yuuta_t self:mood { happy };";

        act1 = new HashSet<>();
        act2 = new HashSet<>();
        act3 = new HashSet<>();

        act1.add("hug");
        act2.add("hug");
        act2.add("pet");
        act3.add("happy");

        r1 = new RuleSetModel(RuleSetModel.RuleType.allow, "yuuta_t", "winslow_t", "winslow", act1);
        r2 = new RuleSetModel(RuleSetModel.RuleType.allow, "yuuta_t", "winslow_t", "winslow", act2);
        r3 = new RuleSetModel(RuleSetModel.RuleType.dontaudit, "yuuta_t", "self", "mood", act3);
        try {
            assertTrue(RuleSetModel.ruleSetParser(t1).equals(r1));
            assertTrue(RuleSetModel.ruleSetParser(t2).equals(r2));
            assertFalse(RuleSetModel.ruleSetParser(t1).equals(r2));
            assertTrue(RuleSetModel.ruleSetParser(t3).equals(r3));
            // Can't use assertEquals because overriding hashCode is dirty
        } catch (SyntaxParseException e) {
            fail(e);
        }
    }
    @Test
    public void testExcpRuleSetParser() {
        String t1 = "allow yuuta_t winslow_t:winslow{wat}";
        String t2 = "allow yuuta_t winslow_t:winslow { { hug pet };";
        assertThrows(SyntaxParseException.class,
                () -> {
                    RuleSetModel.ruleSetParser(t1);
                });
        assertThrows(SyntaxParseException.class,
                () -> {
                    RuleSetModel.ruleSetParser(t2);
                });
    }
    @Test
    public void testToRuleTypeNotImplemented() {
        assertEquals(RuleSetModel.RuleType.neverallow, RuleSetModel.toRuleType("neverallow"));
        assertEquals(RuleSetModel.RuleType.constrain, RuleSetModel.toRuleType("constrain"));
        assertEquals(RuleSetModel.RuleType.mlsconstrain, RuleSetModel.toRuleType("mlsconstrain"));
        assertThrows(NotFoundException.class, () -> {
            RuleSetModel.toRuleType("yuuta");
        });
    }
    @Test
    public void testTokenIsProcessed() {
        assertTrue(RuleSetModel.isProcessed("allow"));
        assertTrue(RuleSetModel.isProcessed("dontaudit"));
        assertFalse(RuleSetModel.isProcessed("mlsconstrain"));

        assertTrue(RuleSetModel.isStatement("allow"));
        assertTrue(RuleSetModel.isStatement("dontaudit"));
        assertTrue(RuleSetModel.isStatement("mlsconstrain"));
        assertTrue(RuleSetModel.isStatement("neverallow"));
        assertTrue(RuleSetModel.isStatement("constrain"));
        assertFalse(RuleSetModel.isStatement("random"));
    }
}
