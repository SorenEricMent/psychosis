package model;

import model.policy.AccessVectorModel;
import model.policy.InterfaceModel;
import model.policy.RuleSetModel;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class InterfaceTest {
    InterfaceModel i1, i2, i3;
    RuleSetModel r1, r2, r3, r5;
    HashSet<String> act1, act2, act3, act4;
    @BeforeEach
    public void init() {
        act1 = new HashSet<>();
        act2 = new HashSet<>();
        act3 = new HashSet<>();
        act4 = new HashSet<>();
        i1 = new InterfaceModel("test1", false);
        i2 = new InterfaceModel("test2", false);
        i3 = new InterfaceModel("test3", true);
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
        r5 = new RuleSetModel(RuleSetModel.RuleType.allow,
                "test_s2_t",
                "test_t2_t",
                "test",
                act4);
    }

    @Test
    public void testInterface() {
        assertFalse(i1.getIsUserDefined());
        assertTrue(i3.getIsUserDefined());
        assertEquals("", i1.getDescription());
        i2.setDescription("fish tart");
        assertEquals("fish tart", i2.getDescription());
    }

    @Test
    public void testInterfaceAddRuleSet() {
        i1.addRuleSetModels(r1);
    }
    @Test
    public void testInterfaceSetRuleSet() {
        ArrayList<RuleSetModel> rs1 = new ArrayList<>();
        rs1.add(r1);
        rs1.add(r5);
        i1.setRuleSetModels(rs1);

    }

    @Test
    public void testInterfaceParserFO() {
        // TODO
    }
}
