package warp.ast.decl.var;

import warp.ast.decl.Declaration;
import warp.types.Type;

abstract public class AbsVariableDecl extends Declaration {
    public String name;
    public Type type = Type.UNKNOWN;

    public boolean isInitialised() {
        return firstChild() != null;
    }
}