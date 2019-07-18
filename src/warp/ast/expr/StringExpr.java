package warp.ast.expr;

import warp.ModuleState;
import warp.ast.ASTNode;

final public class StringExpr extends Expression {
    public String value;
    public char quote;      // " ' or `
    boolean isTemplated;    /* `dfdf ${dfd} sdsd` */

    @Override public int getPrecedence() {
        return 1;
    }
    @Override
    public String toString() {
        var s = quote + value + quote;
        return String.format("%s", s);
    }
    /**
     *  ( "fdsfsdsdf" | 'sdfsdfsdf' | `sdfsdfs ${val} dfdf` )
     */
    @Override
    public StringExpr parse(ModuleState state, ASTNode parent) {
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
