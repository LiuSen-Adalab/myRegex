package regex;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;


public class UnitTest {

    @ParameterizedTest(name = "匹配：{0}")
    @CsvSource({
            "abc", "abcd", "a", "abbbbc",
    })
    void NFAGraph1(String str){
        Regex regex = Regex.compile("a(b|c)*");
        Assertions.assertEquals(1, regex.match(str).size());
    }
}
