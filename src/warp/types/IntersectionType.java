package warp.types;

/**
 * Type '&' Type
 */
final public class IntersectionType extends Type {
    public Type left, right;

    public IntersectionType(Type left, Type right) {
        super(Kind.INTERSECTION);

        this.left = left;
        this.right = right;
    }

    @Override public String toString() {
        return left.toString() + " & " + right.toString();
    }
}
