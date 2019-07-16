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
                    type = new TupleType().parse(state);
                    break;
                case LCURLY:
                    type = new ObjectType().parse(state);
                    break;
            }
        }
        if(type==null) {
            if(value.equals("typeof")) {
                type = new TypeofType().parse(state);
            }
        }

        if(type==null) throw new ParseError("Unknown type @ "+tokens.get());

        // todo - handle unions and intersections
        if(tokens.kind() == Token.Kind.PIPE) {
            Util.todo();
        }
        if(tokens.kind() == Token.Kind.AMPERSAND) {
            Util.todo();
        }

        /* array */
        if(tokens.kind() == Token.Kind.LSQBR) {
            tokens.next();
            tokens.skip(Token.Kind.RSQBR);
            type = new ArrayType(type);
        }
        return type;
    }
}
