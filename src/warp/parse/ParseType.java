package warp.parse;

import org.apache.log4j.Logger;
import warp.Access;
import warp.ModuleState;
import warp.ast.ASTNode;
import warp.lex.Token;
import warp.types.FunctionType;
import warp.types.ObjectType;
import warp.types.Type;

import java.util.Map;

import static java.util.Map.entry;
import static java.util.Map.ofEntries;
import static warp.types.Type.Kind;

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

    public static Type parse(ModuleState state) {
        var tokens = state.tokens;
        var value = tokens.value();

        var t = simpleTypes.get(value);
        if(t!=null) {
            tokens.next();
            return t;
        }

        switch(value) {
            case "object":
                tokens.next();
                return new ObjectType();
        }

        switch(tokens.kind()) {
            case LBR:
                return new FunctionType().parse(state);

        }
        throw new ParseError("Unknown type @ "+tokens.get());
    }
    /**
     * name [?] ':' Type
     */
    public static Parameter parseParam(ModuleState state) {
        var tokens = state.tokens;

        var name = tokens.value();
        tokens.next();

        boolean optional = tokens.kind() == Token.Kind.QUESTION;

        if(optional) {
            tokens.next();
        }

        tokens.skip(Token.Kind.COLON);

        var type = ParseType.parse(state);

        type.isOptional = optional;

        return new Parameter(name, type, Access.NOT_SPECIFIED, false);
    }
    /**
     * This version allows a default argument which will be added to the AST.
     *
     * name [?] ':' Type [ '=' Expression ]
     */
    public static Parameter parseParam(ModuleState state, ASTNode parent) {
        var tokens = state.tokens;

        var access = Access.parse(state);

        var name = tokens.value();
        tokens.next();

        boolean optional = tokens.kind() == Token.Kind.QUESTION;

        if(optional) {
            tokens.next();
        }

        tokens.skip(Token.Kind.COLON);

        var type = ParseType.parse(state);

        type.isOptional = optional;

        if(tokens.kind() == Token.Kind.EQUALS) {
            tokens.next();

            ParseExpression.parse(state, parent);

            return new Parameter(name, type, access, true);
        }

        return new Parameter(name, type, access, false);
    }
}
