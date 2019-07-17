package warp.lex;

import org.apache.log4j.Logger;
import warp.parse.ParseError;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Token storage and iterator.
 */
final public class Tokens {
    private static Logger log = Logger.getLogger(Tokens.class);
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
    /**
     * @return true if _tokens_ contains any tokens other than a Kind.TSDIRECTIVE
     */
    public boolean containsNonTSDIRECTIVETokens() {
        for(var t : tokens) {
            if(t.kind!= Token.Kind.TSDIRECTIVE) return true;
        }
        return false;
    }
    public boolean isValue(String value) {
        return get().value.equals(value);
    }
    public boolean isKeyword(String kw) {
        var t = get();
        return t.kind==Token.Kind.IDENTIFIER && t.value.equals(kw);
    }
    public boolean isKind(Token.Kind k) {
        return get().kind==k;
    }
    public void next() {
        pos++;
    }
    public void skip(Token.Kind k) {
        expect(k);
        next();
    }
    public void skip(String kw) {
        expect(kw);
        next();
    }
    public void skipIf(Token.Kind k) {
        if(kind()==k) {
            next();
        }
    }
    public Token peek(int offset) {
        if(pos+offset>=tokens.size()) return Token.EOF;
        return tokens.get(pos+offset);
    }
    public void expect(Token.Kind... kinds) {
        var actual = get().kind;

        if(Arrays.stream(kinds).noneMatch((k)->k==actual)) {
            if(kinds.length==1)
                throw new ParseError("Expecting "+kinds[0]);
            throw new ParseError("Expecting one of "+Arrays.toString(kinds));
        }
    }
    public void expect(String kw) {
        if(!kw.equals(value())) {
            throw new ParseError("Expecting "+kw);
        }
    }
    /**
     * Find offset of closing bracket taking into account scopes.
     * Assumes we are currently at the opening bracket or before it.
     *
     * @return offset of closing bracket or -1 if not found
     */
    public int findClosingBr() {
        int brackets = 0, square = 0, curly = 0, angle = 0;
        for(var offset=0; pos+offset<tokens.size(); offset++) {
            var k = peek(offset).kind;
            switch(peek(offset).kind) {
                case LSQBR: square++; break;
                case RSQBR: square--; break;
                case LCURLY: curly++; break;
                case RCURLY: curly--; break;
                case LANGLE: angle++; break;
                case RANGLE: angle--; break;
                case LBR: brackets++; break;
                case RBR:
                    brackets--;
                    if(brackets==0 && square==0 && curly==0 && angle==0) return offset;
            }
        }
        return -1;
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
    public int scopeIndexOf(String kw, int offset) {
        log.trace("indexOf");

        int i = 0;
        for(var t : scopeIterator(offset)) {
            if(kw.equals(t.value)) return i;
            i++;
        }
        return -1;
    }
    /**
     * @return an Iterable that will iterate over all tokens at the current scope
     *         level ie. nothing inside any inner bracketed scopes and ending
     *         when the current scope ends or the end of the file is reached.
     *
     *         eg. 10 "hello" [ a ( b ) ] "there"
     *         --> 10 "hello"             "there"
     */
    private Iterable<Token> scopeIterator(int startOffset) {
        log.trace("scopeIterator");
        return () -> new Iterator<>() {
            private int offset = startOffset;
            private int brackets = 0, square = 0, curly = 0, angle = 0;
            private Token next;

            private void getNext() {
                if(next != null) return;

                lp:for( ; pos+offset<tokens.size(); offset++) {
                    switch(peek(offset).kind) {
                        case LSQBR: square++; break;
                        case RSQBR: square--; if(square<0) break lp; break;
                        case LCURLY: curly++; break;
                        case RCURLY: curly--; if(curly<0) break lp; break;
                        case LANGLE: angle++; break;
                        case RANGLE: angle--; if(angle<0) break lp; break;
                        case LBR: brackets++; break;
                        case RBR: brackets--; if(brackets<0) break lp; break;
                        default:
                            if(brackets==0 && square==0 && curly==0 && angle==0) {
                                next = peek(offset);
                                offset++;
                                return;
                            }
                            break;
                    }
                }
                log.trace("eof");
                next = Token.EOF;
            }

            @Override public boolean hasNext() {
                getNext();
                return next != Token.EOF;
            }

            @Override public Token next() {
                getNext();
                var n = next;
                if(next!=Token.EOF) next = null;
                return n;
            }
        };
    }
}
