package model;

import model.policy.InterfaceModel;
import model.policy.PolicyModuleModel;
import model.policy.RuleSetModel;
import org.junit.jupiter.api.Test;
import persistence.ProjectSL;
import persistence.Workspace;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

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
        ArrayList<Pair<String, String[]>> teCallRes = res.getFirst().getLayer("example_layer")
                .getPolicyModule("example_module")
                .getTypeEnf().getInterfaceCall();
        assertTrue(teStatementRes.get(0).equals(
                new RuleSetModel(
                        RuleSetModel.RuleType.allow,
                        "winslow_t", "yuuta_t", "body",
                        new HashSet<>(Arrays.asList("pet", "hug"))
                )
        ));
        assertEquals("yuuta", teCallRes.get(0).getFirst());
        String[] expectedActions1 = {"winslow", "chocolate"};
        assertArrayEquals(expectedActions1, teCallRes.get(0).getSecond());

        InterfaceModel testInterface = res.getFirst().getLayer("example_layer")
                .getPolicyModule("example_module").getInterfaceSet().getInterface("yuuta");
        assertEquals("yuuta", testInterface.getName());
        assertFalse(testInterface.getIsUserDefined());
        assertTrue(testInterface.getRuleSetModels().get(0).equals(
                new RuleSetModel(
                        RuleSetModel.RuleType.allow,
                        "$1",
                        "$2",
                        "candy",
                        new HashSet<>(List.of("eat")))
        ));
    }

    @Test
    public void testProjectSaveCompiled() {
        ProjectModel testProject = new TempProjectModel("example_proj", true);
        testProject.addLayer("example_layer");
        PolicyModuleModel testModule = new PolicyModuleModel("example_module");
        testModule.getTypeEnf().addStatement(
                new RuleSetModel(RuleSetModel.RuleType.allow, "winslow_t", "yuuta_t", "body",
                        new HashSet<>(Arrays.asList("pet", "hug")))
        );
        testModule.getTypeEnf().addInterfaceCall("yuuta", Arrays.asList("winslow", "chocolate").toArray(String[]::new));
        testProject.getLayer("example_layer").addPolicyModule(
                testModule
        );
        testProject.addInterface("example_layer", "example_module", "yuuta");
        testModule.getInterface("yuuta").addRuleSetModels(
                new RuleSetModel(RuleSetModel.RuleType.allow, "$1", "$2", "candy",
                        new HashSet<>(List.of("eat")))
        );

        ProjectModel loadResult = ProjectSL
                .loadProjectFromJsonCompiled(ProjectSL.saveProjectToJsonCompiled(testProject)).getFirst();

        assertEquals("example_proj", loadResult.getName());
        assertEquals("example_layer", loadResult.getLayer("example_layer").getName());
        assertEquals("example_module", loadResult.getLayer("example_layer").getPolicyModule("example_module").getName());

        ArrayList<RuleSetModel> teStatementRes = loadResult.getLayer("example_layer")
                .getPolicyModule("example_module")
                .getTypeEnf().getStatementsFO();
        ArrayList<Pair<String, String[]>> teCallRes = loadResult.getLayer("example_layer")
                .getPolicyModule("example_module")
                .getTypeEnf().getInterfaceCall();
        assertTrue(teStatementRes.get(0).equals(
                new RuleSetModel(
                        RuleSetModel.RuleType.allow,
                        "winslow_t", "yuuta_t", "body",
                        new HashSet<>(Arrays.asList("pet", "hug"))
                )
        ));
        assertEquals("yuuta", teCallRes.get(0).getFirst());
        String[] expectedActions1 = {"winslow", "chocolate"};
        assertArrayEquals(expectedActions1, teCallRes.get(0).getSecond());

        InterfaceModel testInterface = loadResult.getLayer("example_layer")
                .getPolicyModule("example_module").getInterfaceSet().getInterface("yuuta");
        assertEquals("yuuta", testInterface.getName());
        assertFalse(testInterface.getIsUserDefined());
        assertTrue(testInterface.getRuleSetModels().get(0).equals(
                new RuleSetModel(
                        RuleSetModel.RuleType.allow,
                        "$1",
                        "$2",
                        "candy",
                        new HashSet<>(List.of("eat")))
        ));
    }

    @Test
    public void testWorkspaceLoadCompiled() {
        Workspace testWorkspace1 = new Workspace("tw1", 0);

    }

    @Test
    public void testWorkspaceSaveCompiled() {
        Workspace testWorkspace1 = new Workspace("tw1", 0);

    }
}
