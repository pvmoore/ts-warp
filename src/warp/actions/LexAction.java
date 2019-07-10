package warp.actions;

import org.apache.log4j.Logger;
import warp.ModuleState;
import warp.lex.Token;
import warp.lex.Tokens;

import java.nio.file.Files;

final public class LexAction {
    private static Logger log = Logger.getLogger(LexAction.class);
    private int pos;
    private int line;
    private int lineStart;
    private StringBuilder buf = new StringBuilder();
    private ModuleState state;

    public void run(ModuleState state) throws Exception {
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
                case '+':
                    if(peek(1) == '=') {
                        addToken(Token.Kind.PLUS_EQ, 2);
                        pos++;
                    } else if(!isNumber(ch)) {
                        addToken(Token.Kind.PLUS, 1);
                    }
                    break;
                case '-':
                    if(peek(1) == '=') {
                        addToken(Token.Kind.MINUS_EQ, 2);
                        pos++;
                    } else if(!isNumber(ch)) {
                        addToken(Token.Kind.MINUS, 1);
                    }
                    break;
                case '/':
                    if(peek(1)=='/') {
                        handleLineComment();
                    } else if(peek(1)=='*') {
                        handleBlockComment();
                    } else if(peek(1)=='=') {
                        addToken(Token.Kind.FWD_SLASH_EQ, 2);
                        pos++;
                    } else {
                        addToken(Token.Kind.FWD_SLASH, 1);
                    }
                    break;
                case '*':
                    if(peek(1)=='=') {
                        addToken(Token.Kind.ASTERISK_EQ, 2);
                        pos++;
                    } else {
                        addToken(Token.Kind.ASTERISK, 1);
                    }
                    break;
                case '%':
                    if(peek(1)=='=') {
                        addToken(Token.Kind.PERCENT_EQ, 2);
                        pos++;
                    } else {
                        addToken(Token.Kind.PERCENT, 1);
                    }
                    break;
                case '&':
                    if(peek(1)=='=') {
                        addToken(Token.Kind.AMPERSAND_EQ, 2);
                        pos++;
                    } else {
                        addToken(Token.Kind.AMPERSAND, 1);
                    }
                    break;
                case '|':
                    if(peek(1)=='=') {
                        addToken(Token.Kind.PIPE_EQ, 2);
                        pos++;
                    } else {
                        addToken(Token.Kind.PIPE, 1);
                    }
                    break;
                case '^':
                    if(peek(1)=='=') {
                        addToken(Token.Kind.HAT_EQ, 2);
                        pos++;
                    } else {
                        addToken(Token.Kind.HAT, 1);
                    }
                    break;
                case '<':
                    // <    /* Keep multiple '<' as individuals for now */
                    // <=
                    // <<=
                    if(peek(1)=='<' && peek(2)=='=') {
                        addToken(Token.Kind.SHL_EQ, 3);
                        pos+=2;
                    } else if(peek(1)=='=') {
                        addToken(Token.Kind.LANGLE_EQ, 2);
                        pos++;
                    } else {
                        addToken(Token.Kind.LANGLE, 1);
                    }
                    break;
                case '>':
                    // >   /* Keep multiple '<' as individuals for now */
                    // >=
                    // >>=
                    // >>>=
                    if(peek(1)=='>' && peek(2)=='>' && peek(3)=='=') {
                        addToken(Token.Kind.USHR_EQ, 4);
                        pos+=3;
                    } else if(peek(1)=='>' && peek(2)=='=') {
                        addToken(Token.Kind.SHR_EQ, 3);
                        pos += 2;
                    } else if(peek(1)=='=') {
                        addToken(Token.Kind.RANGLE_EQ, 2);
                        pos++;
                    } else {
                        addToken(Token.Kind.RANGLE, 1);
                    }
                    break;

                //                case '\"':
//                case '\'':
//                case '`':
//                    handleString();
//                    break;
                case ':':
                    addToken(Token.Kind.COLON, 1);
                    break;
                case ';':
                    addToken(Token.Kind.SEMICOLON, 1);
                    break;
                case '.':
                    if(buf.length()>0 && isNumber(buf.charAt(0))) {
                        // this is part of a number
                    } else {
                        addToken(Token.Kind.DOT, 1);
                    }
                    break;
                case '=':
                    if(peek(1)=='=' && peek(2)=='=') {
                        addToken(Token.Kind.TPL_EQUALS, 3);
                        pos+=2;
                    } else if(peek(1)=='=') {
                        addToken(Token.Kind.DBL_EQUALS, 2);
                        pos++;
                    } else {
                        addToken(Token.Kind.EQUALS, 1);
                    }
                    break;
                case '!':
                    if(peek(1)=='=' && peek(2)=='=') {
                        addToken(Token.Kind.EXCLAMATION_DBL_EQ, 1);
                        pos += 2;
                    } else if(peek(1)=='=') {
                        addToken(Token.Kind.EXCLAMATION_EQ, 1);
                        pos++;
                    } else {
                        addToken(Token.Kind.EXCLAMATION, 1);
                    }
                    break;
                case ',':
                    addToken(Token.Kind.COMMA, 1);
                    break;
                case '(':
                    addToken(Token.Kind.LBR, 1);
                    break;
                case ')':
                    addToken(Token.Kind.RBR, 1);
                    break;
                case '[':
                    addToken(Token.Kind.LSQBR, 1);
                    break;
                case ']':
                    addToken(Token.Kind.RSQBR, 1);
                    break;
                case '{':
                    addToken(Token.Kind.LCURLY, 1);
                    break;
                case '}':
                    addToken(Token.Kind.RCURLY, 1);
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
//    private void handleString() {
//        addToken();
//
//        var q = peek(0);
//        buf.append(q);
//        pos++;
//
//        while(pos<state.source.length()) {
//            var ch = peek(0);
//            buf.append(ch);
//
//            if(ch==q && peek(-1)!='\\') {
//                addToken();
//                return;
//            }
//            handleNewLine();
//            pos++;
//        }
//        addToken();
//    }
    private boolean isNumber(char ch) {
        return (ch>='0' && ch<='9') || ch=='-';
    }
    private Token.Kind determineTokenKind(String value) {
        assert(value.length()>0);

        var ch = value.charAt(0);
        if(isNumber(ch)) {
            return Token.Kind.NUMBER;
        }
        if(ch=='\"' || ch=='\'' || ch=='`') {
            return Token.Kind.STRING;
        }
        // todo - handle regex

        return Token.Kind.IDENTIFIER;
    }
}
