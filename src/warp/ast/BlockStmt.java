package warp.ast;

import warp.ModuleState;
import warp.lex.Token;
import warp.parse.ParseStatement;

/**
 * '{'
 *      { Statement }
 * '}'
 */
final public class BlockStmt extends Statement  {

    @Override public String toString() {
        return "{}";
    }

    @Override public BlockStmt parse(ModuleState state, ASTNode parent) {
        parent.add(this);
        var tokens = state.tokens;

        tokens.skip(Token.Kind.LCURLY);

        ParseStatement.parseMultiple(state, this);

        tokens.skip(Token.Kind.RCURLY);

        return this;
    }
}
