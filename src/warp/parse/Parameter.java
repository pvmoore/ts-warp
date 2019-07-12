package warp.parse;

import warp.types.Type;

final public class Parameter {
    final public String name;
    final public Type type;
    final public boolean hasInitialiser;

    public Parameter(String name, Type type, boolean hasInitialiser) {
        this.name = name;
        this.type = type;
        this.hasInitialiser = hasInitialiser;
    }

    @Override public String toString() {
        var hi = hasInitialiser ? " initialised" : "";
        return String.format("%s:%s%s", name, type, hi);
    }
}
