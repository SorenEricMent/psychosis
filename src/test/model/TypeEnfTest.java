package model;

import model.policy.InterfaceSetModel;
import model.policy.RuleSetModel;
import model.policy.TypeEnfModel;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import model.exception.SyntaxParseException;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashSet;

public class TypeEnfTest {
    String s1, s2, s3;
    TypeEnfModel t1, t2, t3, t4, t5;
    HashSet<String> act1, act2, act3, act4, act5;
    @BeforeEach
    public void init() {
        try {
            s1 = CustomReader.readAsWholeCode(new File("./data/testfiles/TypeEnfTest/test_te_file"));
            s2 = CustomReader.readAsWholeCode(new File("./data/testfiles/TypeEnfTest/test_te_file_excp"));
            s3 = CustomReader.readAsWholeCode(new File("./data/testfiles/TypeEnfTest/test_te_file_excp_head"));
        } catch (IOException e) {
            fail("Test source file not found, this should not happen!");
        }
        t1 = new TypeEnfModel("t1");
        t2 = new TypeEnfModel("t2");
        t3 = new TypeEnfModel("t3");
        t4 = new TypeEnfModel("t4");
        t5 = new TypeEnfModel("t5");
        act1 = new HashSet<String>();
        act2 = new HashSet<String>();
        act3 = new HashSet<String>();
        act4 = new HashSet<String>();
        act5 = new HashSet<String>();
        act1.add("hug");
        act2.add("hug");
        act2.add("pet");
        act3.add("use");
        act4.add("start");
        act4.add("use");
        act5.add("start");
        t1.addStatement(
                new RuleSetModel(
                        RuleSetModel.RuleType.allow,
                        "yuuta_t",
                        "winslow_t",
                        "winslow",
                        act1));
        t1.addStatement(
                new RuleSetModel(
                        RuleSetModel.RuleType.allow,
                        "yuuta_cute_t",
                        "winslow_t",
                        "winslow",
                        act2));
        t2.addStatement(
                new RuleSetModel(
                        RuleSetModel.RuleType.allow,
                        "yuuta_t",
                        "winslow_t",
                        "winslow",
                        act1));
        t3.addStatement(
                new RuleSetModel(
                        RuleSetModel.RuleType.allow,
                        "yuuta_t",
                        "winslow_t",
                        "winslow",
                        act1));
        t3.addStatement(
                new RuleSetModel(
                        RuleSetModel.RuleType.allow,
                        "yuuta_cute_t",
                        "winslow_t",
                        "winslow",
                        act2));
        t4.addStatement(
                new RuleSetModel(
                        RuleSetModel.RuleType.allow,
                        "yuuta_love_t",
                        "winslow_t",
                        "winslow",
                        act2));
        t5.addStatement(
                new RuleSetModel(
                        RuleSetModel.RuleType.allow,
                        "yuuta_love_t",
                        "winslow_t",
                        "winslow",
                        act1));
    }
    @Test
    public void testTypeEnfEqual() {
        assertTrue(t1.equals(t3));
        assertFalse(t1.equals(t2));
        assertFalse(t4.equals(t1));
        assertFalse(t4.equals(t5));
    }
    @Test
    public void testTypeEnfParse() {
        try {
            assertTrue(t1.equals(TypeEnfModel.parser(s1)));
        } catch (SyntaxParseException e) {
            fail(e);
        }
    }
    @Test
    public void testExcpTypeEnfParse() {
        assertThrows(SyntaxParseException.class,
                () -> {
                    TypeEnfModel.parser(s2);
                });
        assertThrows(SyntaxParseException.class,
                () -> {
                    TypeEnfModel.parser(s3);
                });
        assertThrows(SyntaxParseException.class,
                () -> {
                    TypeEnfModel.parser("policy_module(test)\nbendan(}");
                });
        assertThrows(SyntaxParseException.class,
                () -> {
                    TypeEnfModel.parser("policy_module(test)\nbendan(");
                });
        assertThrows(SyntaxParseException.class,
                () -> {
                    TypeEnfModel.parser("policy_module(test)\nrequire }");
                });
    }
    @Test
    public void testTypeEnfToString() {
        assertEquals("policy_module(t2)\n\nallow yuuta_t winslow_t:winslow { hug };\n", t2.toString(
                new InterfaceSetModel()
        ));
    }
    @Test
    public void testAddRemoveStatement() {
        t1.addStatement(new RuleSetModel(
                RuleSetModel.RuleType.allow, "yuuta_t", "winslow_laptop_t", "system", act4));
        HashSet<String> res = t1.removeStatement(new RuleSetModel(
                RuleSetModel.RuleType.allow, "yuuta_t", "winslow_laptop_t", "system", act3));
        TypeEnfModel check1 = new TypeEnfModel("t1");
        t1.addStatement(new RuleSetModel(
                RuleSetModel.RuleType.allow, "yuuta_t", "winslow_laptop_t", "system", act5));
        assertEquals(act5, res);
        assertTrue(check1.equals(t1));
    }
    @Test
    public void testTypeEnfParseWithInterface() {
        try {
            String testStr = CustomReader.readAsWholeCode(
                    new File("./data/testfiles/InterfaceTest/test_te_call_interface_static"));
            TypeEnfModel t = TypeEnfModel.parser(testStr);

            InterfaceSetModel i = InterfaceSetModel.parser(CustomReader.readAsWholeCode(
                    new File("./data/testfiles/InterfaceTest/test_interface_multidef")
            ));

            assertEquals("policy_module(yuuta)\n" +
                    "\n" +
                    "allow yuuta_t winslow_t:body { hug };\n" +
                    "yuuta()\n", t.toString());
            assertEquals("policy_module(yuuta)\n" +
                    "\n" +
                    "allow yuuta_t winslow_t:body { hug };\n" +
                    "allow yuuta_t winslow_t:winslow { hug };\n" +
                    "\n", t.toString(i));
        } catch (IOException e) {
            fail(e);
        } catch (SyntaxParseException e) {
            fail(e);
        }
    }
    @Test
    public void testTypeEnfParseWithInterfaceVar() {
        try {
            String testStr = CustomReader.readAsWholeCode(
                    new File("./data/testfiles/InterfaceTest/test_te_call_interface_variable"));
            TypeEnfModel t = TypeEnfModel.parser(testStr);

            InterfaceSetModel i = InterfaceSetModel.parser(CustomReader.readAsWholeCode(
                    new File("./data/testfiles/InterfaceTest/test_interface_multidef")
            ));

            assertEquals("policy_module(yuuta)\n" +
                    "\n" +
                    "allow yuuta_t winslow_t:winslow { hug };\n" +
                    "bendan(head)\n", t.toString());
            assertEquals("policy_module(yuuta)\n" +
                    "\n" +
                    "allow yuuta_t winslow_t:winslow { hug };\n" +
                    "allow yuuta_head_t self:yuuta { eat };\n" +
                    "\n", t.toString(i));
        } catch (IOException e) {
            fail(e);
        } catch (SyntaxParseException e) {
            fail(e);
        }
    }
}
