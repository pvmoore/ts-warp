package warp.types;

final public class ArrayType extends Type {
    public Type subtype;

    public ArrayType() {
        super(Kind.ARRAY);
    }

    @Override public String toString() {
        return "[Array of " + kind.toString() + "]";
    }
}
