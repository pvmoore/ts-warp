package warp.parse;

import org.apache.log4j.Logger;
import warp.ModuleState;
import warp.ast.ASTNode;
import warp.ast.decl.Declaration;
import warp.ast.decl.param.ParameterDecl;
import warp.ast.decl.param.ParameterDestructuringDecl;
import warp.lex.Token;

final public class ParseParameter {
    final private static Logger log = Logger.getLogger(ParseParameter.class);

    /**
     * CONSTRUCTOR_PARAM      ::= [Access] identifier [?] [':' Type] [ '=' Expression ]
     *
     *     CLASS_METHOD_PARAM ::= identifier [?] [':' Type] [ '=' Expression ]
     * INTERFACE_METHOD_PARAM ::= identifier [?] [':' Type]
     *
     * Destructuring:
     *
     * ARRAY_OR_TUPLE ::= '[' prop { ',' prop } ']' ':' Type
     * OBJECT         ::= '{' (prop | prop ':' identifier) { ',' (prop | prop ':' identifier) } '}' ':' Type
     *
     * Access is only allowed on constructor parameters.
     * Interface parameters are not allowed to have initialisers.
     */
    public static Declaration parse(ModuleState state, ASTNode parent) {
        log.trace("parseBinary "+state.tokens.get());
        var tokens = state.tokens;

        /* Handle destructuring */
        var k = tokens.kind();
        if(k == Token.Kind.LSQBR || k == Token.Kind.LCURLY) {
            return new ParameterDestructuringDecl().parse(state, parent);
        }

        return new ParameterDecl().parse(state, parent);
    }
}
