package warp.actions;

import org.apache.log4j.Logger;
import warp.State;
import warp.lex.Token;
import warp.lex.Tokens;

import java.nio.file.Files;

final public class LexAction {
    private static Logger log = Logger.getLogger(LexAction.class);
    private int pos;
    private int line;
    private int lineStart;
    private StringBuilder buf = new StringBuilder();
    private State state;

    public void run(State state) throws Exception {
        log.debug("Lexing "+state.file);

        this.state = state;

        state.tokens = new Tokens();
        state.source = Files.readString(state.file.toPath());
        //log.trace("source:\n"+state.source);

        while(pos<state.source.length()) {

            var ch = peek(0);

            if(Character.isWhitespace(ch)) {
                addToken();
                handleNewLine();
            } else switch(ch) {
                case '/':
                    if(peek(1)=='/') {
                        handleLineComment();
                    } else if(peek(1)=='*') {
                        handleBlockComment();
                    } else {
                        addToken(Token.Kind.FWD_SLASH, 1);
                    }
                    break;
                case '\"':
                    addToken();
                    break;
                case ':':
                    addToken(Token.Kind.COLON, 1);
                    break;
                case ';':
                    addToken(Token.Kind.SEMICOLON, 1);
                    break;
                case '=':
                    addToken(Token.Kind.EQUALS, 1);
                    break;
                default:
                    buf.append(ch);
                    break;
            }
            pos++;
        }
        addToken();
    }
    private char peek(int offset) {
        if(pos+offset>=state.source.length()) return '\0';
        return state.source.charAt(pos+offset);
    }
    private void addToken() {
        if(buf.length()>0) {
            var s = buf.toString();
            var kind = determineTokenKind(s);

            var column = (pos-buf.length())-lineStart;

            state.tokens.add(new Token(kind, s, line, column));
            buf.setLength(0);
        }
    }
    private void addToken(Token.Kind kind, int length) {
        addToken();
        var column = pos-lineStart;
        state.tokens.add(new Token(kind, kind.toString(), line, column));
    }
    private boolean handleNewLine() {
        var ch = peek(0);

        if(ch==10 || ch==13) {
            var next = peek(1);
            if(next==10 || next==13) pos++;
            line++;

            lineStart = pos+1;
            return true;
        }
        return false;
    }
    private void handleLineComment() {
        assert(peek(0)=='/' && peek(1)=='/');

        while(pos<state.source.length()) {
            if(handleNewLine()) return;

            pos++;
        }
    }
    private void handleBlockComment() {
        assert(peek(0)=='/' && peek(1)=='*');

        while(pos<state.source.length()) {
            var ch = peek(0);
            if(ch=='*' && peek(1)=='/') {
                pos++;
                return;
            }
            handleNewLine();
            pos++;
        }
    }
    private Token.Kind determineTokenKind(String value) {
        assert(value.length()>0);

        var ch = value.charAt(0);
        if(ch>='0' && ch<='9') {
            return Token.Kind.NUMBER;
        }
        if(ch=='\"' || ch=='\'' || ch=='`') {
            return Token.Kind.STRING;
        }
        // todo - handle regex

        return Token.Kind.IDENTIFIER;
    }
}
