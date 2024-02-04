package model;

import java.io.File;
import java.io.IOException;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class CustomReaderTest {
    Path currentPath;
    File testReader, testReaderBreak;
    @BeforeEach
    public void init() {
        currentPath = Paths.get("");
        testReader = new File("./src/test/model/testfiles/CustomReaderTest/test_reader");
        testReaderBreak = new File("./src/test/model/testfiles/CustomReaderTest/test_reader_break");
    }
    @Test
    public void testReadAsWhole() {
        String fileResult1;
        String fileResult2;
        try {
            fileResult1 = CustomReader.readAsWhole(testReader);
            assertEquals(fileResult1, "Per ardua ad astra.");
        } catch (IOException e) {
            fail("Failed to read existing file, current working path: " + currentPath.toAbsolutePath().toString());
        }
        try {
            fileResult2 = CustomReader.readAsWhole(testReaderBreak);
            assertEquals(fileResult2,"Per ardua ad astra?\nPer ardua ad morbis!\n\nĉi tiu estas whatever a test.");
        } catch (IOException e) {
            fail("Failed to read existing file, current working path: " + currentPath.toAbsolutePath().toString());
        }
    }
    @Test
    public void testReadAsCompact() {

    }
}