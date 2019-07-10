package warp.lex;

final public class Token {
    public enum Kind {
        EOF,
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
        LANGLE_EQ,      // <=
        SHL_EQ,         // <<=
        RANGLE,         // >
        RANGLE_EQ,      // >=
        SHR_EQ,         // >>=
        USHR_EQ,        // >>>=
        RARROW,         // =>

        COLON,          // :
        SEMICOLON,      // ;
        COMMA,          // ,
        DOT,            // .

        FWD_SLASH,      // /
        FWD_SLASH_EQ,   // /=
        EQUALS,         // =
        PLUS,           // +
        PLUS_EQ,        // +=
        MINUS,          // -
        MINUS_EQ,       // -=
        ASTERISK,       // *
        ASTERISK_EQ,    // *=
        PERCENT,        // %
        PERCENT_EQ,     // %=
        AMPERSAND,      // &
        AMPERSAND_EQ,   // &=
        HAT,            // ^
        HAT_EQ,         // ^=
        PIPE,           // |
        PIPE_EQ,        // |=
        QUESTION,       // ?

        DBL_EQUALS,         // ==
        TPL_EQUALS,         // ===
        EXCLAMATION,        // !
        EXCLAMATION_EQ,     // !=
        EXCLAMATION_DBL_EQ, // !==
        DBL_AMPERSAND,      // &&
        DBL_PIPE,           // ||

    }
    public static Token EOF = new Token(Kind.EOF, "", -1, -1);

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
