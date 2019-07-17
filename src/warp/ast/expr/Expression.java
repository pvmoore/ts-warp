package warp.ast.expr;

import warp.ast.stmt.Statement;

public abstract class Expression extends Statement {

    public abstract int getPrecedence();
}
