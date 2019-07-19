package warp.types;

/**
 * Type '|' Type
 */
final public class UnionType extends Type {
    public Type left, right;

    public UnionType(Type left, Type right) {
        super(Kind.UNION);

        this.left = left;
        this.right = right;
    }

    @Override public String toString() {
        return left.toString() + " | " + right.toString();
    }
}
