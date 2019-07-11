package warp.parse;

import org.apache.log4j.Logger;
import warp.ModuleState;
import warp.ast.ASTNode;
import warp.ast.Statement;
import warp.ast.TSDirective;
import warp.ast.decl.Declaration;
import warp.ast.decl.VariableDecl;
import warp.lex.Token;

final public class ParseStatement {
    final private static Logger log = Logger.getLogger(ParseStatement.class);

    public static void parse(ModuleState state, ASTNode parent) {
        log.trace("parse "+state.tokens.get());
        var tokens = state.tokens;

        /* Parse Statements until end of block|file */
        while(!tokens.eof() && tokens.kind()!= Token.Kind.RCURLY) {

            /* Handle 'declare' keyword */
            boolean isAmbient = tokens.isKeyword("declare");
            if(isAmbient) {
                tokens.next();
            }

            var stmt = parseSingleStatement(state, parent);

            if(isAmbient) {
                if(!(stmt instanceof Declaration)) {
                    state.addError("Expecting a declaration");
                } else {
                    ((Declaration)stmt).isAmbient = true;
                }
            }

            /* Semicolon? */
            if(tokens.kind() == Token.Kind.SEMICOLON) {
                tokens.next();
            } else {
                // todo - should be eof or rcurly here
            }
        }
    }

    private static Statement parseSingleStatement(ModuleState state, ASTNode parent) {
        log.trace("parseSingleStatement "+state.tokens.get());
        var tokens = state.tokens;
        var t      = tokens.get();

        switch(t.kind) {
            case TSDIRECTIVE:
                return new TSDirective().parse(state, parent);
        }

        switch(t.value) {
            case "let":
            case "const":
                return new VariableDecl().parse(state, parent);
            default:
                break;
        }

        throw new ParseError("Parse failed in file ["+state.file+"] @ "+tokens.get());
    }
//    private static Declaration parseDeclaration(ModuleState state, ASTNode parent) {
//        log.trace("parseDeclaration "+state.tokens.get());
//        var tokens = state.tokens;
//
//        tokens.setAmbient();
//        tokens.next();
//
//        var t = tokens.get();
//
//
//    }
}
