package warp.types;

public class Type {

    public enum Kind {
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

        UNION,          // type | type
        INTERSECTION,   // type & type
    }

    public Kind kind;
    public boolean isOptional = false;

    public Type(Kind k) {
        this.kind = k;
    }

    @Override public String toString() {
        return "[" + kind.toString() + "]";
    }
}
