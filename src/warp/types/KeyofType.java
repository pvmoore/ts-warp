package warp.types;

import warp.ModuleState;
import warp.parse.ParseType;

final public class KeyofType extends Type {
    public Type subtype;

    public KeyofType() {
        super(Kind.KEYOF);
    }

    @Override public String toString() {
        return "keyof "+subtype;
    }

    /**
     * 'keyof' Type
     */
    public KeyofType parse(ModuleState state) {
        var tokens = state.tokens;

        tokens.skip("keyof");

        this.subtype = ParseType.parse(state);

        return this;
    }
}
