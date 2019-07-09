package warp.types;

import java.util.ArrayList;
import java.util.List;

/**
 * Type | Type
 */
final public class UnionType extends Type {
    public List<Type> subtypes = new ArrayList<>();

    public UnionType() {
        super(Kind.UNION);
    }

    @Override public String toString() {
        return "[Union "+subtypes+"]";
    }
}
