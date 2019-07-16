package warp.types;

import warp.ModuleState;

final public class AliasType extends Type {
    public String name;

    public AliasType() {
        super(Kind.ALIAS);
    }

    @Override
    public String toString() {
        return name;
    }

    public AliasType parse(ModuleState state) {
        var tokens = state.tokens;

        this.name = tokens.value(); tokens.next();

        return this;
    }
}
