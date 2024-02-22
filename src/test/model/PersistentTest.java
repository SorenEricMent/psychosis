package model;

import org.junit.jupiter.api.*;
import persistent.Workspace;

import static org.junit.jupiter.api.Assertions.*;

public class PersistentTest {
    Workspace t1;
    @BeforeEach
    public void init() {
        t1 = new Workspace();
    }
    @Test
    public void testPersistentParse() {

    }
}
