package model;

import model.exception.SyntaxParseException;
import model.policy.AccessVectorModel;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;


// Dependency on the success of CustomReaderTest!
public class AccessVectorTest {
    String fileContent;
    Path currentPath = Paths.get("");
    AccessVectorModel accessVectorModel;
    @BeforeEach
    public void init() {
       accessVectorModel = new AccessVectorModel();
    }
    @Test
    public void testSecurityClassParse() {
        File testFile = new File("./src/test/model/testfiles/AccessVectorTest/test_security_classes");
        Scanner fileReader = null;

        try {
            fileReader = new Scanner(testFile);
        } catch (FileNotFoundException e) {
            fail("Test source file not found, this should not happen!");
        }

        String expected[] = {null, null, null, null, "something", null, "cpsc210", null, "ubc"};
        
        for (int i = 0; i < expected.length; i++) {
            String line = fileReader.nextLine();
            try {
                assertEquals(expected[i], AccessVectorModel.securityClassParser(line));
            } catch (SyntaxParseException e) {
                fail("Failed to parse line: " + line);
            }
        }
        fileReader.close();
    }
    @Test
    public void testAccessVectorParse() {
        File testFile = new File("./src/test/model/testfiles/AccessVectorTest/test_access_vectors");
        try {
            fileContent = CustomReader.readAsWhole(testFile);
        } catch (IOException e) {
            fail("IO Exception, this should not happen & check CustomReaderTest!");
        }
    }
    @Test
    public void testExcpSecurityClassParse() {
        File testFile = new File("./src/test/model/testfiles/AccessVectorTest/test_fail_security_classes");

        Scanner fileReader = null;

        try {
            fileReader = new Scanner(testFile);
        } catch (FileNotFoundException e) {
            fail("Test source file not found, this should not happen!");
        }
        ArrayList<String> testContent = new ArrayList<String>();
        while(fileReader.hasNextLine()) {
            testContent.add(fileReader.nextLine());
        }
        // In the test file, line 1, 3, 5, 6, 7 should produce exception
        // while 2 and 4 should pass.
        assertThrows(SyntaxParseException.class,
                () -> {
                    AccessVectorModel.securityClassParser(testContent.get(0));
                });
        assertThrows(SyntaxParseException.class,
                () -> {
                    AccessVectorModel.securityClassParser(testContent.get(2));
                });
        assertThrows(SyntaxParseException.class,
                () -> {
                    AccessVectorModel.securityClassParser(testContent.get(4));
                });
        assertThrows(SyntaxParseException.class,
                () -> {
                    AccessVectorModel.securityClassParser(testContent.get(5));
                });
        assertThrows(SyntaxParseException.class,
                () -> {
                    AccessVectorModel.securityClassParser(testContent.get(6));
                });
        try {
            assertEquals(null, AccessVectorModel.securityClassParser(testContent.get(1)));
        } catch (SyntaxParseException e) {
            fail("Failed to parse no-content line: " + testContent.get(1));
        }
        try {
            assertEquals("thisshouldpass", AccessVectorModel.securityClassParser(testContent.get(3)));
        } catch (SyntaxParseException e) {
            fail("Failed to parse legal syntax:" + testContent.get(3));
        }
        fileReader.close();
    }
    @Test
    public void testExcpAccessVectorParseTest() {
        File testFile = new File("./src/test/model/testfiles/AccessVectorTest/test_fail_access_vectors");
        try {
            fileContent = CustomReader.readAsWhole(testFile);
        } catch (IOException e) {
            fail("IO Exception, this should not happen & check CustomReaderTest!");
        }
    }
}
