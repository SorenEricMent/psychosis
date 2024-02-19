package model;

import model.policy.AttributeModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;

public class AttributeTest {
    // Psychosis dropped attribute support
    @Test
    public void testSkipAttribute() {
        AttributeModel attributes = new AttributeModel();
        assertNull(attributes.queryAttributeWithType(""));
        assertNull(attributes.queryTypeWithAttributes(""));
    }
}
