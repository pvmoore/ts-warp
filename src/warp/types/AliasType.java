package warp.types;

final public class AliasType extends Type {

    public AliasType() {
        super(Kind.ALIAS);
    }

    @Override public String toString() {
        return "[Alias]";
    }
}
