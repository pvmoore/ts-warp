package warp.ast.expr;

import warp.ModuleState;
import warp.ast.ASTNode;
import warp.lex.Token;

/**
 * NewExpr
 *      ParensExpr  (optional)
 *          (1 or more Expressions)
 */
final public class NewExpr extends Expression {
    public String name;

    @Override
    public int getPrecedence() {
        return hasChildren() ? 18 : 19;
    }

    @Override public String toString() {
        return "new "+name;
    }

    /**
     * 'new' Identifier [ '(' ARGS ') ]
     */
    @Override
    public NewExpr parse(ModuleState state, ASTNode parent) {
        parent.add(this);
        var tokens = state.tokens;

        tokens.skip("new");

        this.name = tokens.value(); tokens.next();

        if(tokens.kind() == Token.Kind.LBR) {

            new ParensExpr().parse(state, this);

        }

        return this;
    }
}
