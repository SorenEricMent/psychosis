package model;

import model.exception.SyntaxParseException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MiscTest {
    // Misc tests to cover some lines that do not need tests but is not being considered covered (like Decodeable)
    @Test
    public void testMisc() {
        try {
            assertNull(Decodeable.parser(""));
        } catch (SyntaxParseException e) {
            fail(e);
        }
        CommonUtil commonUtil = new CommonUtil();
        // All methods in commonUtil are static but Jacoco complains about it is not being newed.
    }
}
