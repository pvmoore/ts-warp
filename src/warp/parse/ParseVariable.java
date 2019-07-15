package warp.parse;

import org.apache.log4j.Logger;
import warp.ModuleState;
import warp.ast.ASTNode;
import warp.ast.decl.Declaration;
import warp.ast.decl.var.DestructuringDecl;
import warp.ast.decl.var.VariableDecl;
import warp.lex.Token;

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

//        var isClassProperty = parent instanceof ClassDecl;
//        var isInterfaceProperty = parent instanceof InterfaceDecl;
//
//        if(isClassProperty) {
//            return new ClassPropertyDecl().parse(state, parent);
//        }
//        if(isInterfaceProperty) {
//            return new InterfacePropertyDecl().parse(state, parent);
//        }

        /* Handle destructuring */
        var k = tokens.peek(1).kind;
        if(k== Token.Kind.LSQBR || k== Token.Kind.LCURLY) {
            return new DestructuringDecl().parse(state, parent);
        }

        return new VariableDecl().parse(state, parent);
    }
}
