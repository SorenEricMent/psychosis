package model;

import org.junit.jupiter.api.*;
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

    }
    @Test
    public void testExcpMacroRuleParser() {

    }
}
