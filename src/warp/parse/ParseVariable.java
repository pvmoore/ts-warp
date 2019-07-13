package warp.parse;

import org.apache.log4j.Logger;
import warp.ModuleState;
import warp.ast.ASTNode;
import warp.ast.decl.ClassDecl;
import warp.ast.decl.Declaration;
import warp.ast.decl.FunctionDecl;
import warp.ast.decl.var.DestructuringDecl;
import warp.ast.decl.var.PropertyDecl;
import warp.ast.decl.var.VariableDecl;
import warp.lex.Token;
import warp.misc.Util;

/**
 * https://www.typescriptlang.org/docs/handbook/variable-declarations.html
 */
final public class ParseVariable {
    final private static Logger log = Logger.getLogger(ParseVariable.class);

    /**
     *  NAME              ::= identifier
     *  REST              ::= '...'
     *  OBJ_DESTRUCTURE   ::= '{' { NAME|REST [',' [NAME|REST] ] } '}'
     *  ARRAY_DESTRUCTURE ::= '[' { NAME|REST [',' [NAME|REST] ] } ']'
     *  TUPLE_DESTRUCTURE ::= '[' { NAME|REST [',' [NAME|REST] ] } ']'
     *
     *  IDS      ::= NAME | OBJ_DESTRUCTURE | ARRAY_DESTRUCTURE | TUPLE_DESTRUCTURE)
     *
     *  VARIABLE ::= (let | const)       IDS [ ':' Type ] [ '=' Expression ] [ ';' ]
     *  PROPERTY ::= [Access] [readonly] IDS [ ':' Type ] [ '=' Expression ] [ ';' ]
     *
     *  DECL     ::= (VARIABLE | PROPERTY)
     */
    public static Declaration parse(ModuleState state, ASTNode parent) {
        log.trace("parse "+state.tokens.get());
        var tokens = state.tokens;
        var isClassProperty = parent instanceof ClassDecl;

        if(isClassProperty) {
            return parseProperty(state, parent);
        }
        if(parent instanceof FunctionDecl) {
            // param
        }

        /* Handle destructuring */
        var k = tokens.peek(1).kind;
        if(k== Token.Kind.LSQBR || k== Token.Kind.LCURLY) {
            return parseDestructuredDecl(state, parent);
        }

        return parseDecl(state, parent);
    }

    /**
     * eg.
     * let [a,b] = a;
     * let {a,b} = o;
     */
    private static DestructuringDecl parseDestructuredDecl(ModuleState state, ASTNode parent) {
        log.trace("parseDestructuredDecl "+state.tokens.get());
        return new DestructuringDecl().parse(state, parent);
    }

    /**
     *
     */
    private static VariableDecl parseParameter(ModuleState state, ASTNode parent) {
        Util.todo();
        return null;
    }
    /**
     * Class property.
     *
     * PROPERTY ::= [Access] [readonly] name [ ':' Type ] [ '=' Expression ] [ ';' ]
     */
    private static PropertyDecl parseProperty(ModuleState state, ASTNode parent) {
        log.trace("parseProperty "+state.tokens.get());
        return new PropertyDecl().parse(state, parent);
    }
    /**
     * Local/global variable declaration.
     *
     * DECL ::= (let | const) name [ ':' Type ] [ '=' Expression ] [ ';' ]
     */
    private static VariableDecl parseDecl(ModuleState state, ASTNode parent) {
        log.trace("parseDecl "+state.tokens.get());
        return new VariableDecl().parse(state, parent);
    }
}
