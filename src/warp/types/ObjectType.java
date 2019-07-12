package warp.types;

import java.util.ArrayList;
import java.util.List;

final public class ObjectType extends Type {
    public List<Type> subtypes = new ArrayList<>();

    public ObjectType() {
        super(Kind.OBJECT);
    }

    @Override public String toString() {
        return "object";
    }
}
