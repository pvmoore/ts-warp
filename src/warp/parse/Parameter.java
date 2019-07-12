package warp.parse;

import warp.types.Type;

final public class Parameter {
    public String name;
    public Type type;

    public Parameter(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    @Override public String toString() {
        return String.format("%s:%s", name, type);
    }
}
