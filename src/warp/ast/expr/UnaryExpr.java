package warp.ast.expr;

import warp.ModuleState;
import warp.Operator;
import warp.ast.ASTNode;
import warp.parse.ParseExpression;

/**
 * UnaryExpr
 *      Expression
 */
final public class UnaryExpr extends Expression {
    public Operator op;

    @Override public int getPrecedence() {
        return op.precedence;
    }

    @Override
    public String toString() {
        return op.toString();
    }

    @Override
    public UnaryExpr parse(ModuleState state, ASTNode parent) {
        parent.add(this);

        this.op = Operator.parseUnary(state);

        ParseExpression.parse(state, this);

        return this;
    }
}
