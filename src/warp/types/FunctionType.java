package warp.types;

import java.util.ArrayList;
import java.util.List;

final public class FunctionType extends Type {
    public List<Type> paramTypes = new ArrayList<>();
    public List<String> paramNames = new ArrayList<>();
    public Type returnType = new Type(Kind.VOID);

    public FunctionType() {
        super(Kind.FUNCTION);
    }

    @Override public String toString() {
        return "[Function]";
    }
}
