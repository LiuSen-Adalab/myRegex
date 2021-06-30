import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("SpellCheckingInspection")
class RegexTest {
    String testPattern = "a(b|c)*a+d?";
    private Regex regex;

    @BeforeEach
    void beforeCompile() {
        regex = Regex.compile(testPattern);
    }

    @Test
    void compile() {
        assertNotNull(regex);
        assertNotNull(regex.nfaGraph.start);
        assertNotNull(regex.nfaGraph.end);
    }

    @ParameterizedTest
    @CsvSource({
            "aaaaaa", "abbcca","abbccad", "abcaaa"
    })
    void match1(String str) {
        LinkedList<String> match1 = regex.match(str);
        assertEquals(1, match1.size());
        assertEquals(str, match1.poll());
    }

    @ParameterizedTest
    @CsvSource({
            "abcadabcad", "aaadaa"
    })
     void match2(String str) {
        LinkedList<String> match1 = regex.match(str);
        assertEquals(2, match1.size());
    }


    @Test
    void isMatch() {
        String[] match = new String[]{
                "abca", "abcad", "abad", "aba", "abaa", "aa", "aaad"
        };
        String[] notMatch = new String[]{
                "bc", "abc", "ad", "abd", "bad"
        };

        for (String s : match) {
            assertTrue(regex.isMatch(s), "failed: " + s);
        }
        for (String s : notMatch) {
            assertFalse(regex.isMatch(s),"failed: " + s);
        }
    }

    void abc(){
        String pattern = "a(b|c)*";
        String test = "abbbccabc";
        Regex regex = Regex.compile(testPattern);
        boolean ismatch = regex.isMatch(test); // false
        List<String> matchStrs = regex.match(test); // {"abbbcc", "abc"}
    }
}