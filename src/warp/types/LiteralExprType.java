package warp.types;

import warp.ModuleState;
import warp.ast.expr.Expression;
import warp.parse.ParseExpression;

/**
 *  Must be a literal string, boolean, number, array, tuple or object.
 *  eg.
 *      "str", 10, true, [10,20], ["str", 10],  {prop:10}
 *
 */
final public class LiteralExprType extends Type {
    public Expression expr;

    public LiteralExprType() {
        super(Kind.EXPRESSION);
    }

    @Override
    public String toString() {
        return expr.toString();
    }

    public LiteralExprType parse(ModuleState state) {

        this.expr = ParseExpression.parseFirstIsolated(state);

        return this;
    }
}
