package warp.ast.expr;

import warp.ModuleState;
import warp.ast.ASTNode;
import warp.ast.Expression;
import warp.parse.ParseExpression;

/**
 * 'typeof'
 *      Expression
 *
 *  Results in one of:
 *      "undefined"
 *      "boolean"
 *      "string"
 *      "number"
 *      "object"
 *      "function"
 *      "symbol"
 */
final public class TypeofExpr extends Expression {

    @Override
    public String toString() {
        return "typeof";
    }

    @Override
    public TypeofExpr parse(ModuleState state, ASTNode parent) {
        parent.add(this);
        var tokens = state.tokens;

        tokens.skip("typeof");

        ParseExpression.parse(state, this);

        return this;
    }
}
