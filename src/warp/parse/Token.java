package warp.parse;

final public class Token {
    public enum Kind {
        IDENTIFIER,
        NUMBER,
        STRING,

        COLON,      // :
        SEMICOLON,  // ;
    }
    public static Token EOF = new Token(Kind.IDENTIFIER, null, -1, -1);

    public Token(Kind kind, String value, int line, int column) {
        this.kind = kind;
        this.value = value;
        this.line = line;
        this.column = column;
    }

    public Kind kind;
    public String value;
    public int line;
    public int column;
}
