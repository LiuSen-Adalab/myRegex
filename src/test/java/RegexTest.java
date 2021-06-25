import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class RegexTest {
    String testPattern = "a(b|c)*";
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
            "a", "ab", "ac", "abb", "abcd", "ba", "bca"
    })
    void match(String str) {
        int count = (int) str.chars().filter(c -> c == 'a').count();
        assertEquals(count, regex.match(str).size());
    }

    @Test
    void isMatch() {
        String[] isMatch = new String[]{
                "a", "ac", "abc", "abbc"
        };
        String[] notMatch = new String[]{
                "aba", "b", "aa", "abcd", "ba"
        };

        for (String item : isMatch) {
            assertTrue(regex.isMatch(item), "failed: " + item);
        }
        for (String item : notMatch) {
            assertFalse(regex.isMatch(item), "failed: " + item);
        }
    }
}