package model;

import model.policy.RuleSetModel;
import model.policy.TypeEnfModel;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import model.exception.SyntaxParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;

public class TypeEnfTest {
    String s1, s2;
    TypeEnfModel t1, t2, t3;
    HashSet<String> act1, act2, act3;
    @BeforeEach
    public void init() {
        try {
            s1 = CustomReader.readAsWholeCode(new File("./data/testfiles/TypeEnfTest/test_te_file"));
            s2 = CustomReader.readAsWholeCode(new File("./data/testfiles/TypeEnfTest/test_te_file_excp"));
        } catch (IOException e) {
            fail("Test source file not found, this should not happen!");
        }
        t1 = new TypeEnfModel();
        t2 = new TypeEnfModel();
        t3 = new TypeEnfModel();
        act1 = new HashSet<String>();
        act2 = new HashSet<String>();
        act3 = new HashSet<String>();
        act1.add("hug");
        act2.add("hug");
        act2.add("pet");
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
    }
    @Test
    public void testTypeEnfEqual() {
        assertTrue(t1.equals(t3));
        assertFalse(t1.equals(t2));
    }
    @Test
    public void testTypeEnfParse() {
        try {
            assertTrue(t1.equals(TypeEnfModel.typeEnfParser(s1)));
        } catch (SyntaxParseException e) {
            fail(e);
        }
    }
    @Test
    public void testExcpTypeEnfParse() {
        assertThrows(SyntaxParseException.class,
                () -> {
                    TypeEnfModel.typeEnfParser(s2);
                });
    }
}
