import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UnitTest {


    @ParameterizedTest(name = "匹配：{0}")
    @CsvSource({
            "abc", "abcd", "a", "abbbbc",
    })
    public void NFAGraph1(String str){
        Regex regex = Regex.compile("a(b|c)*");
        assertEquals(2, regex.match(str).size());
    }
}
