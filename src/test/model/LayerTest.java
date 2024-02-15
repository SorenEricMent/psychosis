package model;

import model.policy.LayerModel;
import model.policy.PolicyModuleModel;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

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
    public void testLayer() {
        assertEquals(p1, l1.getPolicyModule("p1"));
    }
    @Test
    public void testDummyLayer() {
        assertEquals(p1, l3.getPolicyModule("p1"));
    }
}
