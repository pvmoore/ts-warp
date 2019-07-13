package warp;

public enum Access {
    NOT_SPECIFIED, PUBLIC, PROTECTED, PRIVATE;

    public boolean isPublic() { return this==NOT_SPECIFIED || this==PUBLIC; }

    @Override public String toString() {
        if(this==NOT_SPECIFIED) return "";
        return super.toString().toLowerCase();
    }

    public static boolean isAccessKeyword(String kw) {
        switch(kw) {
            case "public":
            case "private":
            case "protected":
                return true;
            default:
                return false;
        }
    }
    public static Access parse(ModuleState state) {
        var tokens = state.tokens;

        switch(tokens.value()) {
            case "private":
                tokens.next();
                return Access.PRIVATE;
            case "protected":
                tokens.next();
                return Access.PROTECTED;
            case "public":
                tokens.next();
                return Access.PUBLIC;
            default:
                return Access.NOT_SPECIFIED;
        }
    }
}
