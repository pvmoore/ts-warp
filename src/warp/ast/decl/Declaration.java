package warp.ast.decl;

import warp.ast.stmt.Statement;

public abstract class Declaration extends Statement {

    /** true if preceded by 'declare' keyword */
    public boolean isAmbient;


}
