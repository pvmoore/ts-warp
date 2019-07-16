package warp.types;

public class Type {
    final public Kind kind;

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
        NEVER,          // never
        BIGINT,         // bigint
        SYMBOL,         // symbol

        OBJECT,         // {}
        CLASS,          // ?

        ARRAY,          // subtype[] or Array<Type>
        FUNCTION,
        TUPLE,          // [subtype,subtype]
        ALIAS,          // (type, enum, class or interface name)
        ENUM,           // enum

        KEYOF,          // keyof Type

        UNION,          // subtype | subtype
        INTERSECTION,   // subtype & subtype
    }


}
