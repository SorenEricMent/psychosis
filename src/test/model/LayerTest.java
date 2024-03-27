package model;

import model.exception.DuplicateException;
import model.exception.NotFoundException;
import model.policy.LayerModel;
import model.policy.PolicyModuleModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LayerTest {

    LayerModel l1, l2, l3;

    PolicyModuleModel p1;

    @BeforeEach
    public void init() {
        l1 = new LayerModel("t1");
        l2 = new DummyLayerModel("t2");
        l3 = new DummyLayerModel("t3");

        p1 = new PolicyModuleModel("p1");

        l1.addPolicyModule(p1);
        l3.addPolicyModule(p1);
    }

    @Test
    public void testDupAdd() {
        ProjectModel proj = new TempProjectModel("cute yuuta");
        assertThrows(DuplicateException.class, () -> {
            proj.addLayer("test");
        });
    }

    @Test
    public void testLayerAndModule() {
        assertEquals(p1, l1.getPolicyModule("p1"));
        assertThrows(NotFoundException.class, () -> {
            l1.getPolicyModule("not_exist");
        });
        assertThrows(NotFoundException.class, () -> {
            l1.removePolicyModule("not_exist");
        });
        l1.addPolicyModule(new PolicyModuleModel("test_module"));
        assertThrows(DuplicateException.class, () -> {
            l1.addPolicyModule(new PolicyModuleModel("test_module"));
        });
    }

    @Test
    public void testDummyLayerAndModule() {
        assertEquals(p1, l3.getPolicyModule("p1"));
    }

    @Test
    public void testLayerToString() {
        String expected1 = "Layer name: t1\nModule list: \np1 ";
        String expected2 = "Layer name: t2\nModule list: \n";
        assertEquals(expected1, l1.toString());
        assertEquals(expected2, l2.toString());
    }

    @Test
    public void testLayerModuleNum() {
        assertEquals(1, l1.getModulesNum());
        assertEquals(0, l2.getModulesNum());
        assertEquals(1, l3.getModulesNum());
    }
}
