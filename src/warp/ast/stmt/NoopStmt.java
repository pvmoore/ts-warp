package warp.ast.stmt;

import warp.ModuleState;
import warp.ast.ASTNode;
import warp.lex.Token;

final public class NoopStmt extends Statement {

    @Override public String toString() {
        return "no-op";
    }

    @Override
    public NoopStmt parse(ModuleState state, ASTNode parent) {
        parent.add(this);
        var tokens = state.tokens;

        tokens.skip(Token.Kind.SEMICOLON);

        return this;
    }
}
