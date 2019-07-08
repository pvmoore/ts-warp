package warp.parse;

import warp.util.ThreadSafe;

import java.util.ArrayList;
import java.util.List;

/**
 * Token storage and iterator.
 */
@ThreadSafe(false)
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
}
