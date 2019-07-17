package warp.ast.expr;

import warp.ModuleState;
import warp.Operator;
import warp.ast.ASTNode;

final public class BinaryExpr extends Expression {
    public Operator op;

    @Override public int getPrecedence() {
        return op.precedence;
    }

    @Override
    public String toString() {
        return op.toString();
    }

    @Override
    public BinaryExpr parse(ModuleState state, ASTNode parent) {
        /* Don't add to parent. This is handled in ParseExpression.attach */
        this.op = Operator.parseBinary(state);
        return this;
    }
}
