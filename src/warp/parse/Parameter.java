package warp.parse;

import warp.Access;
import warp.types.Type;

final public class Parameter {
    final Access access;
    final public String name;
    final public Type type;
    final public boolean hasInitialiser;

    public Parameter(String name, Type type, Access access, boolean hasInitialiser) {
        this.name = name;
        this.type = type;
        this.access = access;
        this.hasInitialiser = hasInitialiser;
    }

    @Override public String toString() {
        var hi = hasInitialiser ? " initialised" : "";
        return String.format("%s:%s%s", name, type, hi);
    }
}
