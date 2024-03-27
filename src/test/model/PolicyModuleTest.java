package model;

import model.policy.FileContextModel;
import model.policy.InterfaceSetModel;
import model.policy.PolicyModuleModel;
import model.policy.TypeEnfModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PolicyModuleTest {
    PolicyModuleModel p1, p2;

    @BeforeEach
    public void init() {
        p1 = new PolicyModuleModel("p1");
        p2 = new PolicyModuleModel("p2",
                new TypeEnfModel("p2"),
                new InterfaceSetModel(),
                new FileContextModel()
        );
    }

    @Test
    public void testPolicyModuleToString() {
        assertEquals("Module name: p1\n" +
                "Rule statements: 0\n" +
                "Declared interfaces: 0\n" +
                "File context: not implemented. \n", p1.toString());
        p1.getInterfaceSet(); // InterfaceSet is internally managed and cannot test equals
        assertEquals("p1", p1.getTypeEnf().getName());
        assertEquals("p2", p2.getTypeEnf().getName());
    }

    @Test
    public void testSetTypeEnf() {
        TypeEnfModel test = new TypeEnfModel("test");
        p1.setTypeEnfObject(test);
        assertEquals(test, p1.getTypeEnf());
    }

    @Test
    public void testSetInfSet() {
        InterfaceSetModel test = new InterfaceSetModel();
        p1.setInterfaceObject(test);
        assertEquals(test, p1.getInterfaceSet());
    }
}
