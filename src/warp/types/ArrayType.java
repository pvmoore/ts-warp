package warp.types;

/**
 * todo - this is also an object
 */
final public class ArrayType extends Type {
    public Type subtype;

    public ArrayType(Type subtype) {
        super(Kind.ARRAY);
        this.subtype = subtype;
    }

    @Override public String toString() {
        return String.format("%s[]", subtype);
    }
}
