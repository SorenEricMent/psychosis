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

        while (fileReader.hasNextLine()) {
            try {
                accessVectorModel.addSecurityClass(
                        AccessVectorModel.securityClassParser(fileReader.nextLine())
                );
            } catch (SyntaxParseException e) {
                fail(e);
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
        //TODO
        // AccessVectorModel.securityClassParser(fileReader.nextLine());
        fail("Expected syntax parsing error but passed");

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
