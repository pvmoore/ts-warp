package warp.types;

import java.util.ArrayList;
import java.util.List;

/**
 * Type & Type
 */
final public class IntersectionType extends Type {
    public List<Type> subtypes = new ArrayList<>();

    public IntersectionType() {
        super(Kind.INTERSECTION);
    }

    @Override public String toString() {
        return "[Intersection "+subtypes+"]";
    }
}
