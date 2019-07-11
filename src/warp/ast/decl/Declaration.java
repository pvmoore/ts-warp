package warp.ast.decl;

import warp.ast.Statement;

public abstract class Declaration extends Statement {

    /** true if preceded by 'declare' keyword */
    public boolean isAmbient;


}
