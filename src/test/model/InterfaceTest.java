package model;

import model.exception.NotFoundException;
import model.exception.SyntaxParseException;
import model.policy.*;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
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
    public void testInterfaceSet() {
        InterfaceSetModel testSet = new InterfaceSetModel();
        testSet.addInterface(i1);
        testSet.addInterface(i2);
        assertEquals(i1, testSet.getInterface("test1"));
        testSet.removeInterface("test1");
        assertThrows(NotFoundException.class, () -> {
            testSet.getInterface("test1");
        });
        assertThrows(NotFoundException.class, () -> {
            testSet.removeInterface("test1");
        });
    }

    @Test
    public void testInterfaceSetToString() {
        InterfaceSetModel testSet = new InterfaceSetModel();
        testSet.addInterface(i1);
        testSet.addInterface(i2);
        assertEquals("interface(`test1',`\n" +
                "')\n" +
                "interface(`test2',`\n" +
                "')\n", testSet.toString());
    }

    @Test
    public void testInterfaceAddRuleSet() {
        i1.addRuleSetModels(r1);
        i1.addRuleSetModels(r3);
        assertEquals(
                "interface(`test1',`\n" +
                        "allow test_s_t test_t_t:test { testact testact2 };\n" +
                        "')",
                i1.toString());
        i1.addRuleSetModels(r1);
        assertEquals(
                "interface(`test1',`\n" +
                        "allow test_s_t test_t_t:test { testact testact2 };\n" +
                        "')",
                i1.toString());
        i2.addRuleSetModels(r3);
        i2.addRuleSetModels(r5);
        assertEquals(
                "interface(`test2',`\n" +
                        "allow test_s_t test_t_t:test { testact testact2 };\n" +
                        "allow test_s2_t test_t2_t:test { testact testact3 testact2 };\n" +
                        "')",
                i2.toString());
    }
    @Test
    public void testInterfaceSetRuleSet() {
        ArrayList<RuleSetModel> rs1 = new ArrayList<>();
        rs1.add(r1);
        rs1.add(r5);
        i1.setRuleSetModels(rs1);
    }

    @Test
    public void testInterfaceParserFOSingleDef() {
        File testFile = new File("./data/testfiles/InterfaceTest/test_interface_singledef");
        String fileContent = "";
        try {
            fileContent = CustomReader.readAsWholeCode(testFile);
        } catch (IOException e) {
            fail("IO Exception, this should not happen & check CustomReaderTest!");
        }
        InterfaceSetModel test = null;
        try {
            test = InterfaceSetModel.parser(fileContent);
        } catch (SyntaxParseException e) {
            fail(e);
        }
        InterfaceModel testInf1 = test.getInterface("lovely_yuuta");
        assertEquals(1, testInf1.getRuleNum());
        assertEquals(
                "interface(`lovely_yuuta',`\n" +
                        "allow yuuta_t winslow_t:winslow { hug };\n" +
                        "')",
                testInf1.toString()
        );
    }

    @Test
    public void testInterfaceParserFOMultiDef() {
        File testFile = new File("./data/testfiles/InterfaceTest/test_interface_multidef");
        InterfaceSetModel test = null;
        String fileContent = "";
        try {
            fileContent = CustomReader.readAsWholeCode(testFile);
        } catch (IOException e) {
            fail("IO Exception, this should not happen & check CustomReaderTest!");
        }
        try {
            test = InterfaceSetModel.parser(fileContent);
        } catch (SyntaxParseException e) {
            fail(e);
        }
        assertEquals(
                "interface(`yuuta',`\n" +
                        "allow yuuta_t winslow_t:winslow { hug };\n" +
                        "')",
                test.getInterface("yuuta").toString());
        assertEquals("interface(`bendan',`\n" +
                        "allow yuuta_$1_t self:yuuta { eat };\n" +
                        "')",
                test.getInterface("bendan").toString());
    }
    @Test
    public void testExcpInterfaceParserFO() {
        File testFile = new File("./data/testfiles/InterfaceTest/test_interface_syntaxerror");
        try {
            String fileContent = CustomReader.readAsWholeCode(testFile);
            assertThrows(SyntaxParseException.class, () -> {
                InterfaceSetModel.parser(fileContent);
            });
            assertThrows(SyntaxParseException.class, () -> {
                InterfaceSetModel.parser("template('x");
            });
            assertThrows(SyntaxParseException.class, () -> {
                InterfaceSetModel.parser("interface('x");
            });
            assertThrows(SyntaxParseException.class, () -> {
                InterfaceSetModel.parser("template(`x',`allow x y:z { a }')");
            });
            assertThrows(SyntaxParseException.class, () -> {
                InterfaceSetModel.parser("template(`x',`allow x y:z { a };");
            });
            assertThrows(SyntaxParseException.class, () -> {
                InterfaceSetModel.parser("interface(`x',`require {}})");
            });
            assertThrows(SyntaxParseException.class, () -> {
                InterfaceSetModel.parser("interface(`x',})");
            });
            assertThrows(SyntaxParseException.class, () -> {
                InterfaceSetModel.parser("interface(`x'x");
            });
            assertThrows(SyntaxParseException.class, () -> {
                InterfaceSetModel.parser("interface(`x`,`')");
            });
            assertThrows(SyntaxParseException.class, () -> {
                InterfaceSetModel.parser("interface`x',`')");
            });
        } catch (IOException e) {
            fail("IO Exception, this should not happen & check CustomReaderTest!");
        }
    }
    @Test
    public void testInterfaceCall() {
        File testFile = new File("./data/testfiles/InterfaceTest/test_interface_multidef");
        InterfaceSetModel test = null;
        String fileContent = "";
        try {
            fileContent = CustomReader.readAsWholeCode(testFile);
        } catch (IOException e) {
            fail("IO Exception, this should not happen & check CustomReaderTest!");
        }
        try {
            test = InterfaceSetModel.parser(fileContent);
        } catch (SyntaxParseException e) {
            fail(e);
        }
        String[] args1 = {};
        String[] args2 = {"head"};

        assertEquals(
                "allow yuuta_t winslow_t:winslow { hug };\n",
                test.getInterface("yuuta").call(args1).toStringRuleOnly());
        assertEquals("allow yuuta_head_t self:yuuta { eat };\n",
                test.getInterface("bendan").call(args2).toStringRuleOnly());
    }
}
