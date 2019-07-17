package warp.lex;

final public class Token {
    public enum Kind {
        EOF("EOF"),
        IDENTIFIER("Identifier"),
        NUMBER("number"),
        STRING("string"),
        TSDIRECTIVE("TSD"),     //  /// <... />

        LBR("("),
        RBR(")"),
        LSQBR("["),
        RSQBR("]"),
        LCURLY("{"),
        RCURLY("}"),

        LANGLE("<"),
        LANGLE_EQ("<="),
        LANGE2_EQ("<<="),

        RANGLE(">"),
        RANGLE_EQ(">="),
        RANGLE2_EQ(">>="),
        RANGLE3_EQ(">>>="),
        RARROW("=>"),

        COLON(":"),
        SEMICOLON(";"),
        COMMA(","),
        DOT("."),
        DOT3("..."),

        FWD_SLASH("/"),
        FWD_SLASH_EQ("/="),
        PLUS("+"),
        PLUS2("++"),
        PLUS_EQ("+="),
        MINUS("-"),
        MINUS2("--"),
        MINUS_EQ("-="),
        ASTERISK("*"),
        ASTERISK_EQ("*="),
        PERCENT("%"),
        PERCENT_EQ("%="),
        AMPERSAND("&"),
        AMPERSAND_EQ("&="),
        HAT("^"),
        HAT_EQ("^="),
        PIPE("|"),
        PIPE_EQ("|="),
        QUESTION("?"),
        TILDE("~"),

        EQ("="),
        EQ2("=="),
        EQ3("==="),
        EXCLAMATION("!"),
        EXCLAMATION_EQ("!="),
        EXCLAMATION_EQ2("!=="),
        AMPERSAND2("&&"),
        PIPE2("||"),

        ;

        final private String value;

        Kind(String value) {
            this.value = value;
        }

        @Override public String toString() {
            return this.value;
        }
    }
    public static final Token EOF = new Token(Kind.EOF, "", -1, -1);

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
