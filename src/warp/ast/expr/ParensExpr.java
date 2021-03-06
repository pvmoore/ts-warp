package warp.ast.expr;

import warp.ModuleState;
import warp.ast.ASTNode;
import warp.lex.Token;
import warp.parse.ParseExpression;

/**
 * '(' Expression ')'
 */
final public class ParensExpr extends Expression {

    @Override public int getPrecedence() {
        return 1;
    }
    @Override public String toString() {
        return "()";
    }

    @Override
    public ParensExpr parse(ModuleState state, ASTNode parent) {
        log.trace("parseBinary "+state.tokens.get());
        parent.add(this);

        var tokens = state.tokens;

        tokens.skip(Token.Kind.LBR);

        while(tokens.kind() != Token.Kind.RBR) {

            ParseExpression.parse(state, this);

            tokens.expect(Token.Kind.RBR, Token.Kind.COMMA);
            tokens.skipIf(Token.Kind.COMMA);
        }

        tokens.skip(Token.Kind.RBR);

        return this;
    }
}
