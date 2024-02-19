package model;

import model.exception.SyntaxParseException;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class MacroProcessorTest {

    MacroProcessor m1, m2;
    String s1, s2;
    @BeforeEach
    public void init() {
        m1 = new MacroProcessor();
        m2 = new MacroProcessor();
        m1.addMacro("yuuta", "bendan");
        m2.addMacro("yuuta", "bendan");
        m2.addMacro("fishtart", "yuutah");
        s1 = "winslow eat yuuta";
        s2 = "winslow eat yuuta with fishtart";
    }
    @Test
    public void testSingleProcess() {
        s1 = m1.process(s1);
        assertEquals("winslow eat bendan", s1);
    }
    @Test
    public void testMultiProcess() {
        s2 = m2.process(s2);
        assertEquals("winslow eat bendan with yuutah", s2);
    }
    @Test
    public void testMacroRuleParser() {
        File testFile = new File("./data/testfiles/MacroProcessorTest/test_macrofile");
        String content = "";

        try {
            content = CustomReader.readAsWholeCode(testFile);
        } catch (FileNotFoundException e) {
            fail("Test source file not found, this should not happen!");
        } catch (IOException e) {
            fail(e);
        }
        MacroProcessor testProcessor = null;
        try {
            testProcessor = MacroProcessor.macroRuleParser(content);
        } catch (SyntaxParseException e) {
            fail(e);
        }
        String testStr = "yuuta";
        String testStr3= "shiftnyuuta";

        assertEquals("bendan", testProcessor.process(testStr));
        assertEquals("yuucuta", testProcessor.process("yuucuta"));
        assertEquals("shiftnbendan", testProcessor.process(testStr3));
    }
    @Test
    public void testExcpMacroRuleParser() {
        File testFile = new File("./data/testfiles/MacroProcessorTest/test_macrofile_excp");
        assertThrows(SyntaxParseException.class, () -> {
            MacroProcessor.macroRuleParser(CustomReader.readAsWholeCode(testFile));
        });
        assertThrows(SyntaxParseException.class, () -> {
            MacroProcessor.macroRuleParser("define(`broken");
        });
        assertThrows(SyntaxParseException.class, () -> {
            MacroProcessor.macroRuleParser("definee(broken`)");
        });
    }
}
