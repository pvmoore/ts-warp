package warp.types;

import warp.ModuleState;


final public class TypeofType extends Type {
    public Type type;
    public String identifier;

    public TypeofType() {
        super(Kind.UNKNOWN);
    }

    @Override public String toString() {
        return String.format("typeof %s", identifier);
    }

    /**
     * 'typeof' identifier
     */
    public TypeofType parse(ModuleState state) {
        var tokens = state.tokens;

        tokens.skip("typeof");

        this.identifier = tokens.value(); tokens.next();

        return this;
    }
}