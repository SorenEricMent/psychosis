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

    }
    @Test
    public void testTokenValidate() {

    }
    @Test
    public void testTokenValidateWeak() {

    }
}
