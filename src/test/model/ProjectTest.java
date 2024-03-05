package model;

import model.exception.DuplicateException;
import model.exception.NotFoundException;
import model.exception.SyntaxParseException;
import model.exception.UnknownCapabilityException;
import model.policy.AccessVectorModel;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class ProjectTest {

    @Test
    public void testStrToCapability() {
        for (ProjectModel.PolicyCapabilities p : ProjectModel.PolicyCapabilities.values()) {
            try {
                assertEquals(p, ProjectModel.strToCapability(p.toString()));
            } catch (UnknownCapabilityException e) {
                fail(e);
            }
        }
    }

    @Test
    public void testCapabilityParser() {
        File testFile = new File("./data/testfiles/ProjectTest/policy_capabilities");
        HashMap<ProjectModel.PolicyCapabilities, Boolean> expected =
                new HashMap<ProjectModel.PolicyCapabilities, Boolean>();
        expected.put(ProjectModel.PolicyCapabilities.network_peer_controls, true);
        expected.put(ProjectModel.PolicyCapabilities.open_perms, true);
        expected.put(ProjectModel.PolicyCapabilities.extended_socket_class, true);
        expected.put(ProjectModel.PolicyCapabilities.cgroup_seclabel, true);
        expected.put(ProjectModel.PolicyCapabilities.nnp_nosuid_transition, true);

        String content = null;
        try {
            content = CustomReader.readAsWholeCode(testFile).getFirst();
        } catch (IOException e) {
            fail("IO Exception, should not happen! " + e);
        }
        try {
            assertEquals(expected, ProjectModel.capabilitiesParser(content));
        } catch (SyntaxParseException | UnknownCapabilityException e) {
            fail(e);
        }

        testFile = new File("./data/testfiles/ProjectTest/policy_capabilities_full");

        try {
            content = CustomReader.readAsWholeCode(testFile).getFirst();
        } catch (IOException e) {
            fail("IO Exception, should not happen! " + e);
        }
        ProjectModel testProj = new ProjectModel("test", "/");
        testProj.setCapabilities(expected);
        assertTrue(testProj.checkCapability(ProjectModel.PolicyCapabilities.cgroup_seclabel));
        assertFalse(testProj.checkCapability(ProjectModel.PolicyCapabilities.always_check_network));

        expected.put(ProjectModel.PolicyCapabilities.always_check_network, true);
        expected.put(ProjectModel.PolicyCapabilities.genfs_seclabel_symlinks, true);
        expected.put(ProjectModel.PolicyCapabilities.ioctl_skip_cloexec, true);
        try {
            assertEquals(expected, ProjectModel.capabilitiesParser(content));
        } catch (SyntaxParseException | UnknownCapabilityException e) {
            fail(e);
        }
    }

    @Test
    public void testExcpCapabilityParser() {
        File testFile = new File("./data/testfiles/ProjectTest/fail_policy_capabilities");
        try {
            String content = CustomReader.readAsWholeCode(testFile).getFirst();
            assertThrows(UnknownCapabilityException.class, () ->
            {
                ProjectModel.capabilitiesParser(content);
            });
        } catch (IOException e) {
            fail("IO Exception, should not happen! " + e);
        }

        testFile = new File("./data/testfiles/ProjectTest/fail_policy_capabilities_token");
        try {
            String content = CustomReader.readAsWholeCode(testFile).getFirst();
            assertThrows(SyntaxParseException.class, () ->
            {
                ProjectModel.capabilitiesParser(content);
            });
        } catch (IOException e) {
            fail("IO Exception, should not happen! " + e);
        }
    }

    @Test
    public void testTempProject() {
        TempProjectModel testProj = new TempProjectModel("test");
        assertNull(testProj.getProjectPath());
        assertEquals(testProj.getName(), "test");
        assertEquals(testProj.toString(),
                "Project name: test" +
                        "\nLayers: " +
                        "test\n" +
                        "\nYOU ARE WORKING ON A TEST PROJECT, YOUR PROGRESS WILL NOT BE SAVED.");
    }

    @Test
    public void testAddRemoveLayer() {
        ProjectModel testProj = new TempProjectModel("test");
        testProj.addLayer("test1");
        assertThrows(DuplicateException.class, () -> {
            testProj.addLayer("test1");
        });
        assertThrows(NotFoundException.class, () -> {
            testProj.getLayer("test2");
        });
        testProj.removeLayer("test1");
        assertThrows(NotFoundException.class, () -> {
            testProj.removeLayer("test1");
        });
    }

    @Test
    public void testAddRemoveModule() {
        ProjectModel testProj = new TempProjectModel("test");
        testProj.addModule("test", "yuuta");
        assertEquals("yuuta",
                testProj.getLayer("test").getPolicyModule("yuuta").getName());
    }

    @Test
    public void testAddRemoveInterface() {
        ProjectModel testProj = new TempProjectModel("test");
        testProj.addInterface("test", "test_module", "testinf");
        assertEquals("testinf",
                testProj.getLayer("test").getPolicyModule("test_module").getInterface("testinf").getName());
    }

    @Test
    public void testProjectMainMisc() {
        // This test is to fix the coverage in ProjectModel
        // In phase 1, no saving functionality is done, but for future development
        // purpose, there is a ProjectModel although currently only TempProjectModel which extends it are used
        // The methods in ProjectModel, although not abstract, doesn't currently do ANYTHING
        // Therefore a placeholder test is required for coverage
        ProjectModel testProj = new ProjectModel("test", "/");
        testProj.addModule("test", "test");
        testProj.removeModule("test", "test");
        testProj.addInterface("test", "test", "test");
        testProj.removeInterface("test", "test", "test");

        AccessVectorModel testAV = new AccessVectorModel();
        testProj.setAccessVectors(testAV);
        assertEquals(testAV, testProj.getAccessVectors());
    }

    @Test
    public void testProjectTempMisc() {
        // TempProject has actually functionality to test for Phase 1
        ProjectModel testProj = new TempProjectModel("test");
        testProj.addInterface("test", "test_module", "yuuta");
        assertThrows(NotFoundException.class, () -> {
            testProj.addInterface("test", "excp", "yuuta");
            // Adding to a non-exist module
        });
        testProj.removeInterface("test", "test_module", "yuuta");
        testProj.removeModule("test", "test_module");
        assertEquals(0, testProj.getLayer("test").getModulesNum());
    }
}
