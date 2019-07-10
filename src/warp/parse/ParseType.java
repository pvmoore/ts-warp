package warp.parse;

import org.apache.log4j.Logger;
import warp.ModuleState;
import warp.ast.ASTNode;
import warp.types.Type;

import java.util.Map;

import static java.util.Map.entry;
import static java.util.Map.ofEntries;
import static warp.types.Type.*;

final public class ParseType {
    private static Logger log = Logger.getLogger(ParseType.class);
    private static Map<String,Type> simpleTypes = ofEntries(
        entry("any", new Type(Kind.ANY)),
        entry("number", new Type(Kind.NUMBER)),
        entry("string", new Type(Kind.STRING)),
        entry("boolean", new Type(Kind.BOOLEAN)),
        entry("undefined", new Type(Kind.UNDEFINED)),
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
        throw new ParseError("Unknown type @ "+tokens.get());
    }
}
