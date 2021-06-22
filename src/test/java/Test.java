import java.util.LinkedList;

public class Test {
    public static void main(String[] args) {
        Regex regex = Regex.compile("a(b|c)*");

        LinkedList<String> strings = regex.match("abbbbbcabda");
        for (String string : strings) {
            System.out.println(string);
        }
//        System.out.println(regex.isMatch("abbbb"));
//        regex.printNFAGraph();
    }
}
