package warp.ast.decl.prop;

import warp.ast.decl.Declaration;
import warp.types.Type;

abstract public class AbsPropertyDecl extends Declaration {
    public String name;
    public Type type = new Type(Type.Kind.UNKNOWN);
    public boolean isReadonly;
    public boolean isOptional;

    public boolean isInitialised() {
        return firstChild() != null;
    }
}
