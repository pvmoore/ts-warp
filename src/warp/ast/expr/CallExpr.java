package warp.ast.expr;

import warp.ModuleState;
import warp.Operator;
import warp.ast.ASTNode;
import warp.lex.Token;
import warp.parse.ParseExpression;

/**
 * CallExpr
 *      Expression (0 or more arguments)
 */
final public class CallExpr extends Expression {
    public String name;

    @Override
    public int getPrecedence() {
        return Operator.CALL.precedence;
    }

    @Override public String toString() {
        return name + "()";
    }

    @Override
    public CallExpr parse(ModuleState state, ASTNode parent) {
        parent.add(this);
        var tokens = state.tokens;

        this.name = tokens.value(); tokens.next();

        tokens.skip(Token.Kind.LBR);

        while(tokens.kind() != Token.Kind.RBR) {

            add(ParseExpression.parseIsolated(state));

            tokens.expect(Token.Kind.COMMA, Token.Kind.RBR);
            tokens.skipIf(Token.Kind.COMMA);
        }

        tokens.skip(Token.Kind.RBR);

        return this;
    }
}
