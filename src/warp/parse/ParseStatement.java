package warp.parse;

import org.apache.log4j.Logger;
import warp.ModuleState;
import warp.ast.ASTNode;
import warp.ast.stmt.*;
import warp.ast.decl.*;
import warp.ast.decl.func.FunctionDecl;
import warp.lex.Token;

/**
 *  https://github.com/Microsoft/TypeScript/blob/master/doc/spec.md#a3-statements
 */
final public class ParseStatement {
    final private static Logger log = Logger.getLogger(ParseStatement.class);

    /**
     * Parse STatements until end of block or file.
     */
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

            var stmt = doParse(state, parent);

            if(isAmbient) {
                if(stmt==null || !(stmt instanceof Declaration)) {
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

    /**
     * Parse a single Statement and return it.
     */
    public static Statement parseSingle(ModuleState state, ASTNode parent) {
        log.trace("parseSingle "+state.tokens.get());
        var tokens = state.tokens;

        var stmt = doParse(state, parent);

        /* Semicolon? */
        if(tokens.kind() == Token.Kind.SEMICOLON) {
            tokens.next();
        } else {
            // todo - should be eof or rcurly here
        }
        return stmt;
    }

    private static Statement doParse(ModuleState state, ASTNode parent) {
        log.trace("doParse "+state.tokens.get());
        var tokens = state.tokens;
        var t      = tokens.get();

        switch(t.kind) {
            case TSDIRECTIVE:
                return new TSDirective().parse(state, parent);
            case LCURLY:
                /* must be a block */
                return new BlockStmt().parse(state, parent);
            case SEMICOLON:
                return new NoopStmt().parse(state, parent);
        }

        switch(t.value) {
            case "let":
            case "const":
                if(tokens.peek(1).value.equals("enum")) {
                    return new EnumDecl().parse(state, parent);
                } else {
                    return ParseVariable.parse(state, parent);
                }
            case "function":
                return new FunctionDecl().parse(state, parent);
            case "class":
                return new ClassDecl().parse(state, parent);
            case "interface":
                return new InterfaceDecl().parse(state, parent);
            case "type":
                return new TypeAliasDecl().parse(state, parent);
            case "enum":
                return new EnumDecl().parse(state, parent);
            case "return":
                return new ReturnStmt().parse(state, parent);
            case "if":
                return new IfStmt().parse(state, parent);
            case "break":
                return new BreakStmt().parse(state, parent);
            case "continue":
                return new ContinueStmt().parse(state, parent);
            case "switch":
                return new SwitchStmt().parse(state, parent);
            case "for":
                return ParseFor.parse(state, parent);
        }

        /* Assume it's an Expression */
        ParseExpression.parse(state, parent);

        return null;
    }
}
