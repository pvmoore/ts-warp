package warp.ast.expr;

import warp.ModuleState;
import warp.ast.ASTNode;
import warp.parse.ParseExpression;

/**
 * 'typeof'
 *      Identifier
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

    @Override public int getPrecedence() {
        return 16;
    }
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
