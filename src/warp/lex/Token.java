package warp.lex;

final public class Token {
    public enum Kind {
        IDENTIFIER,
        NUMBER,
        STRING,

        LBR,            // (
        RBR,            // )
        LSQBR,          // [
        RSQBR,          // ]
        LCURLY,         // {
        RCURLY,         // }
        LANGLE,         // <
        RANGLE,         // >
        RARROW,         // =>

        COLON,          // :
        SEMICOLON,      // ;
        COMMA,          // ,
        DOT,            // .

        FWD_SLASH,      // /
        EQUALS,         // =
        PLUS,           // +
        MINUS,          // -
        ASTERISK,       // *
        PERCENT,        // %
        EXCLAMATION,    // !
        AMPERSAND,      // &
        HAT,            // ^
        PIPE,           // |
        QUESTION,       // ?

        DBL_EQUALS,     // ==
        NOT_EQUALS,     // !=
        TPL_EQUALS,     // ===
        NOT_DBL_EQUALS, // !==
        DBL_AMPERSAND,  // &&
        DBL_PIPE,       // ||

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

    @Override public String toString() {
        var s = kind==Kind.IDENTIFIER ? String.format("'%s'", value) : kind.toString();

        return String.format("%s [Line %d:%d]", s, line, column);
    }
}
