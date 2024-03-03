package model;

import model.policy.RuleSetModel;
import org.junit.jupiter.api.*;
import persistence.ProjectSL;
import persistence.Workspace;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class PersistenceTest {
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

        ArrayList<RuleSetModel> teStatementRes = res.getFirst().getLayer("example_layer")
                .getPolicyModule("example_module")
                .getTypeEnf().getStatementsFO();
        assertTrue(teStatementRes.get(0).equals(
                new RuleSetModel(
                        RuleSetModel.RuleType.allow,
                        "winslow_t", "yuuta_t", "body",
                        new HashSet<>(Arrays.asList("pet", "hug"))
                )
        ));
    }
    @Test
    public void testProjectSaveCompiled() {
        ProjectModel testProj = new TempProjectModel("testproj");

    }

    @Test
    public void testWorkspaceLoadCompiled() {
        Workspace testWorkspace1 = new Workspace("tw1");

    }
    @Test
    public void testWorkspaceSaveCompiled() {
        Workspace testWorkspace1 = new Workspace("tw1");

    }
}
