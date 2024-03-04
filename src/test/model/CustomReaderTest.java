package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class CustomReaderTest {
    Path currentPath;
    File testReader, testReaderBreak, testReadCommented, testReadEmpty;

    @BeforeEach
    public void init() {
        currentPath = Paths.get("");
        testReader = new File("./data/testfiles/CustomReaderTest/test_reader");
        testReaderBreak = new File("./data/testfiles/CustomReaderTest/test_reader_break");
        testReadCommented = new File("./data/testfiles/AccessVectorTest/test_security_classes");
        testReadEmpty = new File("./data/testfiles/CustomReaderTest/test_reader_empty");
    }

    @Test
    public void testReadAsWhole() {
        String fileResult1;
        String fileResult2;
        try {
            fileResult1 = CustomReader.readAsWhole(testReader);
            assertEquals(fileResult1, "Per ardua ad astra.");
        } catch (IOException e) {
            fail("Failed to read existing file, current working path: " + currentPath.toAbsolutePath());
        }
        try {
            fileResult2 = CustomReader.readAsWhole(testReaderBreak);
            assertEquals(fileResult2, "Per ardua ad astra?\nPer ardua ad morbis!\n\ncxi tiu estas whatever a test.");
        } catch (IOException e) {
            fail("Failed to read existing file, current working path: " + currentPath.toAbsolutePath());
        }
    }

    @Test
    public void testReadAsWholeCode() {
        String fileResult1;
        String expected = "\n" +
                "\n" +
                "\n" +
                "\n" +
                "class something\n" +
                "\n" +
                "class cpsc210\n" +
                "\n" +
                "class ubc";
        try {
            fileResult1 = CustomReader.readAsWholeCode(testReadCommented).getFirst();
            assertEquals(expected, fileResult1);
        } catch (IOException e) {
            fail("Failed to read existing file, current working path: " + currentPath.toAbsolutePath());
        }
    }

    @Test
    public void testReadEmptyFile() {
        try {
            assertEquals("", CustomReader.readAsWhole(testReadEmpty));
            assertEquals("", CustomReader.readAsWholeCode(testReadEmpty).getFirst());
        } catch (IOException e) {
            fail(e);
        }
    }
}