package model;

import org.junit.jupiter.api.*;
import persistence.ProjectSL;
import persistence.Workspace;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class PersistenceTest {

    Workspace testWorkspace1 = new Workspace("tw1");

    @Test
    public void testProjectLoadCompiled() {
        String content = "";
        try {
            content = CustomReader.readAsWhole(new File("./data/testfiles/PersistenceTest/example_xpscp.1.json"));
        } catch (Exception e) {
            fail("Failed to load test file, this should not happen!");
        }
        Pair<ProjectModel, TrackerModel> res = ProjectSL.loadProjectFromJsonCompiled(content);

        assertEquals("example_proj", res.getFirst().getName());
        assertEquals("example_layer", res.getFirst().getLayer("example_layer").getName());
        assertEquals("example_module", res.getFirst().getLayer("example_layer").getPolicyModule("example_module").getName());


    }
    @Test
    public void testProjectSaveCompiled() {

    }

    @BeforeEach
    public void init() {

    }
    @Test
    public void testWorkspaceLoadCompiled() {

    }
    @Test
    public void testWorkspaceSaveCompiled() {

    }
}
