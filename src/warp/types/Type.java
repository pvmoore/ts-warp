package warp.types;

public class Type {
    final public Kind kind;
    public boolean isOptional = false;

    public static final Type UNKNOWN = new Type(Kind.UNKNOWN);

    public Type(Kind k) {
        this.kind = k;
    }
    @Override public String toString() {
        return kind.toString().toLowerCase();
    }


    public enum Kind {
        UNKNOWN,
        ANY,            // any
        BOOLEAN,        // boolean
        NUMBER,         // number
        STRING,         // string
        UNDEFINED,      // undefined
        NULL,           // null
        VOID,           // void
        NEVER,          // never (return type)

        OBJECT,         // {}
        CLASS,          // ?

        ARRAY,          // type[] or Array<Type>
        FUNCTION,
        TUPLE,          // [type,type]
        ALIAS,          // type x = y
        ENUM,           // enum

        UNION,          // type | type
        INTERSECTION,   // type & type
    }


}
