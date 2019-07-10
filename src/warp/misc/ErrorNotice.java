package warp.misc;

import warp.lex.Token;

final public class ErrorNotice {
    public String msg;
    public Token token;
    public int line;

    public ErrorNotice(String msg, Token token) {
        this.msg = msg;
        this.token = token;
        this.line = token.line;
    }
    public ErrorNotice(String msg, int line) {
        this.msg = msg;
        this.line = line;
    }

    @Override public String toString() {
        return String.format("Line %d: %s", line+1, msg);
    }
}
