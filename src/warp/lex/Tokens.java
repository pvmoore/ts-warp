package warp.lex;

import warp.parse.ParseError;

import java.util.ArrayList;
import java.util.List;

/**
 * Token storage and iterator.
 */
final public class Tokens {
    private int pos;
    private List<Token> tokens = new ArrayList<>();

    public int length() {
        return tokens.size();
    }
    public void add(Token t) {
        tokens.add(t);
    }
    public Token get() {
        if(pos>=tokens.size()) return Token.EOF;
        return tokens.get(pos);
    }
    public String value() {
        return get().value;
    }
    public Token.Kind kind() {
        return get().kind;
    }
    public int line() {
        return get().line;
    }
    public boolean eof() {
        return pos >= tokens.size();
    }

    public boolean isValue(String value) {
        return get().value.equals(value);
    }
    public boolean isKind(Token.Kind k) {
        return get().kind==k;
    }
    public void next() {
        pos++;
    }
    public Token peek(int offset) {
        if(pos+offset>=tokens.size()) return Token.EOF;
        return tokens.get(pos+offset);
    }
    public void expect(Token.Kind kind) {
        if(get().kind != kind) throw new ParseError("Expecting "+kind);
    }
    public boolean isKeyword(String kw) {
        var t = get();
        return t.kind==Token.Kind.IDENTIFIER && t.value.equals(kw);
    }
    public String toMultilineString() {
        var buf = new StringBuilder("[");
        tokens.forEach((t)-> {
            buf.append("\n\t").append(t.toString());
        });
        return buf.append("\n]").toString();
    }
    @Override public String toString() {
        var buf = new StringBuilder();
        buf.append(tokens);
        return buf.toString();
    }
}
