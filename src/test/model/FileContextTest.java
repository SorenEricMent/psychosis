package model;

import model.policy.FileContextModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


public class FileContextTest {
    FileContextModel fc1;

    @BeforeEach
    public void init() {
        fc1 = new FileContextModel();
    }

    @Test
    public void testFileContext() {
        assertNull(fc1.toString());
        assertNull(FileContextModel.parser(""));
        assertEquals(0, fc1.lineCount());
    }
}
