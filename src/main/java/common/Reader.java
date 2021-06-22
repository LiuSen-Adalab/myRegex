package common;

public class Reader {
    int curPosition;
    char[] input;
    public Reader(String input){
        this.input = input.toCharArray();
    }

    public boolean hasNext(){
        return curPosition < input.length;
    }


    public char next(){
        if (!hasNext()) {
            return '\0';
        }
        char nextChar = input[curPosition];
        curPosition += 1;
        return nextChar;
    }

    public char peek(){
        if (!hasNext()) {
            return '\0';
        }
        return input[curPosition];
    }

    public String getSubRegex(){
        StringBuilder buffer = new StringBuilder();
        while (peek() != ')'){
            buffer.append(next());
        }
        next(); // 跳过")"
        return buffer.toString();
    }

    public String getRemain(){
        StringBuilder buffer = new StringBuilder();
        while (hasNext()) {
            buffer.append(next());
        }
        return buffer.toString();
    }

}
