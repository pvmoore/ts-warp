package warp.ast.expr;

import warp.ModuleState;
import warp.ast.ASTNode;
import warp.ast.Expression;
import warp.lex.Token;
import warp.parse.ParseExpression;

/**
 * '(' Expression ')'
 */
final public class ParensExpr extends Expression {

    @Override public ParensExpr parse(ModuleState state, ASTNode parent) {
        log.trace("parse "+state.tokens.get());
        parent.add(this);

        var tokens = state.tokens;

        tokens.skip(Token.Kind.LBR);

        ParseExpression.parse(state, this);

        tokens.skip(Token.Kind.RBR);

        return this;
    }
}
