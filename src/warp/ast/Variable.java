package warp.ast;

import warp.types.Type;

final public class Variable extends Statement {
    public String name;
    public boolean isConst;
    public Type type;
}
