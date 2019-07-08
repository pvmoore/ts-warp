package warp.actions;

import org.apache.log4j.Logger;
import warp.State;
import warp.parse.Token;
import warp.parse.Tokens;
import warp.util.VoidFn;

import java.nio.file.Files;

final public class LexAction {
    private static Logger log = Logger.getLogger(LexAction.class);
    private int pos;
    private int line;
    private int column;
    private StringBuilder buf = new StringBuilder();
    private State state;

    public void run(State state) throws Exception {
        log.debug("Lexing "+state.file);

        this.state = state;

        state.tokens = new Tokens();
        state.source = Files.readString(state.file.toPath());
        //log.trace("source:\n"+state.source);

        VoidFn handleLineComment = ()-> {};
        VoidFn handleBlockComment = ()-> {};
        VoidFn handleStringLiteral = ()-> {};
        VoidFn handleNumber = ()-> {};

        while(pos<state.source.length()) {

            var ch = peek(0);

            if(Character.isWhitespace(ch)) {
                addToken();
                handleNewLine(ch);
            } else switch(ch) {
                case '/':
                    if(peek(1)=='/') {

                    } else if(peek(1)=='*') {

                    } else {

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
                default:
                    buf.append(ch);
                    break;
            }
            pos++;
            column++;
        }
        addToken();

        log.debug("Tokens: "+ state.tokens);
    }
    private char peek(int offset) {
        return state.source.charAt(pos+offset);
    }
    private void addToken() {
        if(buf.length()>0) {
            state.tokens.add(new Token(Token.Kind.IDENTIFIER, buf.toString(), line, column));
            buf.setLength(0);
        }
    }
    private void addToken(Token.Kind kind, int length) {
        addToken();
        if(buf.length()>0) {
            state.tokens.add(new Token(kind, kind.toString(), line, column));
        }
    }
    private void handleNewLine(char ch) {
        if(ch==10 || ch==13) {
            line++;
            column = 0;
        }
    }
}
