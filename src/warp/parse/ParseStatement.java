package warp.parse;

import org.apache.log4j.Logger;
import warp.ModuleState;
import warp.ast.ASTNode;
import warp.ast.BlockStmt;
import warp.ast.Statement;
import warp.ast.TSDirective;
import warp.ast.decl.ClassDecl;
import warp.ast.decl.Declaration;
import warp.ast.decl.FunctionDecl;
import warp.ast.stmt.ReturnStmt;
import warp.lex.Token;

/**
 *  https://github.com/Microsoft/TypeScript/blob/master/doc/spec.md#a3-statements
 */
final public class ParseStatement {
    final private static Logger log = Logger.getLogger(ParseStatement.class);

    public static void parseMultiple(ModuleState state, ASTNode parent) {
        log.trace("parseMultiple "+state.tokens.get());
        var tokens = state.tokens;

        /* Parse Statements until end of block|file */
        while(!tokens.eof() && tokens.kind() != Token.Kind.RCURLY) {

            /* Handle 'declare' keyword */
            boolean isAmbient = tokens.isKeyword("declare");
            if(isAmbient) {
                tokens.next();
            }

            var stmt = parseSingle(state, parent);

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

    public static Statement parseSingle(ModuleState state, ASTNode parent) {
        log.trace("parseSingle "+state.tokens.get());
        var tokens = state.tokens;
        var t      = tokens.get();

        switch(t.kind) {
            case TSDIRECTIVE:
                return new TSDirective().parse(state, parent);
            case LCURLY:
                /* must be a block */
                return new BlockStmt().parse(state, parent);
        }

        switch(t.value) {
            case "let":
            case "const":
                return ParseVariable.parse(state, parent);
            case "function":
                return new FunctionDecl().parse(state, parent);
            case "class":
                return new ClassDecl().parse(state, parent);
            case "return":
                return new ReturnStmt().parse(state, parent);
        }

        throw new ParseError("Parse failed in file ["+state.file+"] @ "+tokens.get());
    }
}
