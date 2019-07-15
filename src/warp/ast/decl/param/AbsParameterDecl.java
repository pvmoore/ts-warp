package warp.ast.decl.param;

import warp.ast.decl.Declaration;

public abstract class AbsParameterDecl extends Declaration {

    public boolean isInitialised() {
        return firstChild() != null;
    }
}
