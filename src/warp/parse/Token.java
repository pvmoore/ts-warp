package warp.parse;

final public class Token {
    public enum Kind {
        KEYWORD;



    }

    public Kind kind;
    public String value;
    public int line;
    public int column;
}
