package model;

import model.policy.PolicyModuleModel;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class PolicyModuleTest {
    PolicyModuleModel p1;
    @BeforeEach
    public void init() {
        p1 = new PolicyModuleModel("p1");
    }
    @Test
    public void testPolicyModuleToString() {
        assertEquals("Module name: p1\n" +
                "Rule statements: 0\n" +
                "Declared interfaces: 0\n" +
                "File context: not implemented. \n", p1.toString());
        p1.getInterfaceSet(); // InterfaceSet is internally managed and cannot test equals
        assertEquals("p1", p1.getTypeEnf().getName());
    }
}
