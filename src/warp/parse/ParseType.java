package warp.parse;

import org.apache.log4j.Logger;
import warp.ModuleState;
import warp.lex.Token;
import warp.misc.Util;
import warp.types.*;

import java.util.Map;

import static java.util.Map.entry;
import static java.util.Map.ofEntries;
import static warp.types.Type.Kind;

/**
 * https://github.com/Microsoft/TypeScript/blob/master/doc/spec.md#a1-types
 */
final public class ParseType {
    final private static Logger log = Logger.getLogger(ParseType.class);
    final private static Map<String,Type> primitiveTypes = ofEntries(
        entry("any", new Type(Kind.ANY)),
        entry("number", new Type(Kind.NUMBER)),
        entry("string", new Type(Kind.STRING)),
        entry("boolean", new Type(Kind.BOOLEAN)),
        entry("undefined", new Type(Kind.UNDEFINED)),
        entry("null", new Type(Kind.NULL)),
        entry("void", new Type(Kind.VOID)),
        entry("never", new Type(Kind.NEVER)),
        entry("bigint", new Type(Kind.BIGINT)),
        entry("symbol", new Type(Kind.SYMBOL)),
        entry("object", new ObjectType())
    );

    public static Type parse(ModuleState state) {
        var tokens = state.tokens;
        var value = tokens.value();

        Type type = primitiveTypes.get(value);
        if(type!=null) {
            tokens.next();
        }

        if(type==null) {
            switch(tokens.kind()) {
                case LBR:
                    type = new FunctionType().parse(state);
                    break;
                case LSQBR:
                    // todo - could be a LiteralExprType
                    type = new TupleType().parse(state);
                    break;
                case LCURLY:
                    // todo - could be a LiteralExprType
                    type = new ObjectType().parse(state);
                    break;
            }
        }
        if(type==null) {
            switch(value) {
                case "typeof":
                    type = new TypeofType().parse(state);
                    break;
                case "keyof":
                    type = new KeyofType().parse(state);
                    break;
            }
        }
        if(type==null) {
            var k = tokens.kind();
            var v = tokens.value();

            if(Util.in(k, Token.Kind.STRING, Token.Kind.NUMBER,
                          Token.Kind.LCURLY, Token.Kind.LSQBR) ||
                v.equals("true") || v.equals("false"))
            {
                type = new LiteralExprType().parse(state);

            } else if(k == Token.Kind.IDENTIFIER) {
                /* Assume it's a type name eg (type, enum, class or interface name or 'this') */
                type = new AliasType().parse(state);
            }
        }

        if(type==null) throw new ParseError("Unknown subtype @ "+tokens.get());


        /* array */
        if(tokens.kind() == Token.Kind.LSQBR) {
            tokens.next();
            tokens.skip(Token.Kind.RSQBR);
            type = new ArrayType(type);
        }

        /* Union */
        if(tokens.kind() == Token.Kind.PIPE) {
            tokens.next();
            type = new UnionType(type, ParseType.parse(state));
        }
        /* Intersections */
        if(tokens.kind() == Token.Kind.AMPERSAND) {
            tokens.next();
            type = new IntersectionType(type, ParseType.parse(state));
        }
        return type;
    }
}
