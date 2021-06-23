import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;


public class UnitTest {

    @ParameterizedTest(name = "匹配：{0}")
    @CsvSource({
            "abc", "abcd", "a", "abbbbc","bb","za"
    })
    void NFAGraph(String str){
        Regex regex = Regex.compile("a(b|c)*");
        int countA = (int) str.chars().filter(c -> c == 97).count();
        Assertions.assertEquals(countA, regex.match(str).size());
    }
}
