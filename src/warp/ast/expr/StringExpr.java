package warp.ast.expr;

import warp.ModuleState;
import warp.ast.ASTNode;
import warp.ast.Expression;

final public class StringExpr extends Expression {
    public String value;
    public char quote;      // " ' or `
    boolean isTemplated;    /* `dfdf ${dfd} sdsd` */

    @Override public String toString() {
        var t = isTemplated ? " template" : "";
        var s = quote + value + quote;
        return String.format("[string %s%s]", s, t);
    }
    /**
     *  ( "fdsfsdsdf" | 'sdfsdfsdf' | `sdfsdfs ${val} dfdf` )
     */
    @Override public StringExpr parse(ModuleState state, ASTNode parent) {
        parent.add(this);

        var v = state.tokens.value();
        this.quote = v.charAt(0);
        state.tokens.next();

        if(quote=='`') {
            isTemplated = true;
        }
        this.value = v.substring(1, v.length()-1);

        return this;
    }
}
