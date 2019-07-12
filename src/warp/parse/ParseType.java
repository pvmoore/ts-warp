package warp.parse;

import org.apache.log4j.Logger;
import warp.ModuleState;
import warp.ast.ASTNode;
import warp.types.ObjectType;
import warp.types.Type;

import java.util.Map;

import static java.util.Map.entry;
import static java.util.Map.ofEntries;
import static warp.types.Type.*;

/**
 * https://github.com/Microsoft/TypeScript/blob/master/doc/spec.md#a1-types
 */
final public class ParseType {
    final private static Logger log = Logger.getLogger(ParseType.class);
    final private static Map<String,Type> simpleTypes = ofEntries(
        entry("any", new Type(Kind.ANY)),
        entry("number", new Type(Kind.NUMBER)),
        entry("string", new Type(Kind.STRING)),
        entry("boolean", new Type(Kind.BOOLEAN)),
        entry("undefined", new Type(Kind.UNDEFINED)),
        entry("null", new Type(Kind.NULL)),
        entry("void", new Type(Kind.VOID)),
        entry("never", new Type(Kind.NEVER))
    );

    public static Type parse(ModuleState state, ASTNode parent) {
        var tokens = state.tokens;
        var value = tokens.value();
        var t = simpleTypes.get(value);
        if(t!=null) {
            tokens.next();
            return t;
        }
        if(value.equals("object")) {
            tokens.next();
            return new ObjectType();
        }
        throw new ParseError("Unknown type @ "+tokens.get());
    }
}
