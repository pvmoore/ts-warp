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

    @Override public BlockStmt parse(ModuleState state, ASTNode parent) {
        var tokens = state.tokens;

        tokens.skip(Token.Kind.LCURLY);

        ParseStatement.parseMultiple(state, parent);

        tokens.skip(Token.Kind.RCURLY);

        return this;
    }
}
