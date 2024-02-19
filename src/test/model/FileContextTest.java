package model;

import model.policy.FileContextModel;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;


public class FileContextTest {
    FileContextModel fc1;
    @BeforeEach
    public void init() {
        fc1 = new FileContextModel();
    }
    @Test
    public void testFileContext() {
        assertNull(fc1.toString());
        assertNull(fc1.parser(""));
        assertEquals(0, fc1.lineCount());
    }
}
