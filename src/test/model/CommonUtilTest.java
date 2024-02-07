package model;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class CommonUtilTest {
    @Test
    public void testCommentLocate() {
        String testStr[] = {
                "",
                "Abc",
                "# comment",
                "test # comment",
                " #comment",
                "todo # comment # 233"
        };
        int[] locations = {-1, -1, 0, 5, 1, 5};
        for(int i = 0; i < testStr.length; i++) {
            assertEquals(CommonUtil.commentLocate(testStr[i]), locations[i]);
        }
    }
    @Test
    public void testBasicTokenizer() {
        String[] testContent = {
            "\ntest\n"
        };
        String[] expected1 = {
                "test"
        };
        for (int i = 0; i < testContent.length; i++) {
            assertArrayEquals(expected1, CommonUtil.basicTokenizer(testContent[i]));
        }
    }
    @Test
    public void testTokenValidate() {
        assertFalse(CommonUtil.tokenValidate(""));
        assertTrue(CommonUtil.tokenValidate("a"));
        assertTrue(CommonUtil.tokenValidate("a1"));
        assertFalse(CommonUtil.tokenValidate("1a"));
        assertTrue(CommonUtil.tokenValidate("c1_t"));
        assertFalse(CommonUtil.tokenValidate("$1_t"));
    }
    @Test
    public void testTokenValidateWeak() {
        assertFalse(CommonUtil.tokenValidateWeak(""));
        assertTrue(CommonUtil.tokenValidateWeak("a"));
        assertTrue(CommonUtil.tokenValidateWeak("a1"));
        assertTrue(CommonUtil.tokenValidateWeak("_a1"));
        assertFalse(CommonUtil.tokenValidateWeak("1a"));
        assertFalse(CommonUtil.tokenValidateWeak("$a"));
        assertFalse(CommonUtil.tokenValidateWeak("$"));
        assertTrue(CommonUtil.tokenValidateWeak("$0"));
        assertTrue(CommonUtil.tokenValidateWeak("$1_t"));
    }
}
