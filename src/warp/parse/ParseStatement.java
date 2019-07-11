package warp.parse;

import org.apache.log4j.Logger;
import warp.ModuleState;
import warp.ast.ASTNode;
import warp.ast.Statement;
import warp.ast.decl.VariableDecl;
import warp.lex.Token;

final public class ParseStatement {
    private static Logger log = Logger.getLogger(ParseStatement.class);

    public static void parse(ModuleState state, ASTNode parent) {
        log.trace("parse "+state.tokens.get());
        var tokens = state.tokens;

        /* Parse Statements until end of block|file */
        while(!tokens.eof() && tokens.kind()!= Token.Kind.RCURLY) {
            parseSingleStatement(state, parent);

            /* Semicolon? */
            if(tokens.kind()== Token.Kind.SEMICOLON) {
                tokens.next();

                // todo - should be eof or rcurly here
            }
        }
    }

    private static Statement parseSingleStatement(ModuleState state, ASTNode parent) {
        log.trace("parseSingleStatement "+state.tokens.get());
        var tokens = state.tokens;
        var t      = tokens.get();

        switch(t.value) {
            case "let":
            case "const":
                return new VariableDecl().parse(state, parent);
            default:
                break;
        }

        throw new ParseError("Parse failed in file ["+state.file+"] @ "+tokens.get());
    }
}
