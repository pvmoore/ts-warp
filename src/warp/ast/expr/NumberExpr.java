package warp.ast.expr;

import warp.ModuleState;
import warp.ast.ASTNode;
import warp.ast.Expression;

final public class NumberExpr extends Expression {
    public double value;

    @Override public String toString() {
        return String.format("[number %s]", value);
    }
    /**
     *  123
     *  -123
     *  0xff
     *  -0xff
     *  0o44
     *  -0o44
     *  0b1010
     *  -ob111
     *  123.45
     *  123e5
     *  123e-5
     */
    @Override public NumberExpr parse(ModuleState state, ASTNode parent) {
        var s = state.tokens.value().toLowerCase();
        state.tokens.next();

        boolean neg = s.startsWith("-");
        if(neg) s = s.substring(1);

        if(s.length()>1) {
            if(s.startsWith("0x")) {
                this.value = Long.parseLong(s.substring(2), 16);
            } else if(s.startsWith("0o")) {
                this.value = Long.parseLong(s.substring(2), 8);
            } else if(s.startsWith("0b")) {
                this.value = Long.parseLong(s.substring(2), 2);
            } else if(s.contains(".")) {
                this.value = Double.parseDouble(s);
            } else if(s.contains("e")) {
                /* scientific notation */
                this.value = Double.parseDouble(s);
            } else {
                this.value = Long.parseLong(s);
            }
        } else {
            /* Single digit must be an int */
            this.value = Integer.parseInt(s);
        }
        if(neg) this.value = -this.value;

        parent.add(this);
        return this;
    }
}
