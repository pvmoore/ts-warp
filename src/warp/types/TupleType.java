package warp.types;

import java.util.ArrayList;
import java.util.List;

final public class TupleType extends Type {
    public List<Type> subtypes = new ArrayList<>();

    public TupleType() {
        super(Kind.TUPLE);
    }

    @Override public String toString() {
        return "[Tuple "+subtypes+"]";
    }
}
