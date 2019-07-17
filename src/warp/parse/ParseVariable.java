package warp.parse;

import org.apache.log4j.Logger;
import warp.ModuleState;
import warp.ast.ASTNode;
import warp.ast.decl.Declaration;
import warp.ast.decl.var.MultiVariableDecl;
import warp.ast.decl.var.VariableDecl;
import warp.ast.decl.var.VariableDestructuringDecl;
import warp.lex.Token;

import java.util.ArrayList;
import java.util.function.Function;

/**
 * https://www.typescriptlang.org/docs/handbook/variable-declarations.html
 */
final public class ParseVariable {
    final private static Logger log = Logger.getLogger(ParseVariable.class);

    /**
     * VAR ::= (VariableDecl | VariableDestructuringDecl)
     *
     * VAR_DECL ::= (let | const) VAR { ',' VAR }
     */
    public static Declaration parse(ModuleState state, ASTNode parent) {
        log.trace("parse "+state.tokens.get());
        var tokens = state.tokens;

        var list = new ArrayList<Declaration>();

        while(true) {
            var d = doParse(state, parent);
            list.add(d);

            if(tokens.kind() == Token.Kind.COMMA) {
                tokens.next();
            } else {
                break;
            }
        }

        if(list.size()==1) {
            return list.remove(0);
        } else {
            var decl = new MultiVariableDecl();
            parent.add(decl);

            for(var d : list) {
                decl.add(d);
            }

            return decl;
        }
    }

    private static Declaration doParse(ModuleState state, ASTNode parent) {
        var tokens = state.tokens;

        final Function<Integer,Boolean> isDestructuring = (Integer offset)-> {
            var k = tokens.peek(offset).kind;
            return k == Token.Kind.LSQBR || k== Token.Kind.LCURLY;
        };

        /* Handle destructuring */
        if(isDestructuring.apply(0) || isDestructuring.apply(1)) {
            return new VariableDestructuringDecl().parse(state, parent);
        }

        return new VariableDecl().parse(state, parent);
    }
}
