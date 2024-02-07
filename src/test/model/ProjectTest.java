package model;

import model.exception.SyntaxParseException;
import model.exception.UnknownCapabilityException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class ProjectTest {
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
            content = CustomReader.readAsWholeCode(testFile);
        } catch (IOException e) {
            fail("IO Exception, should not happen! " + e);
        }
        try {
            assertEquals(expected, ProjectModel.capabilitiesParser(content));
        } catch (SyntaxParseException e) {
            fail(e);
        } catch (UnknownCapabilityException e) {
            fail(e);
        }
    }
    @Test
    public void testExcpCapabilityParser() {
        File testFile = new File("./data/testfiles/ProjectTest/fail_policy_capabilities");
        try {
            String content = CustomReader.readAsWholeCode(testFile);
            assertThrows(UnknownCapabilityException.class, () ->
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
                                "\nLayers: "+
                                "test\n"+
                                "\nYOU ARE WORKING ON A TEST PROJECT, YOUR PROGRESS WILL NOT BE SAVED.");
    }
}
