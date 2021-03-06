package warp.parse;

import org.apache.log4j.Logger;
import warp.ModuleState;
import warp.ast.ASTNode;
import warp.ast.decl.*;
import warp.ast.decl.func.FunctionDecl;
import warp.ast.stmt.*;
import warp.lex.Token;

/**
 *  https://github.com/Microsoft/TypeScript/blob/master/doc/spec.md#a3-statements
 */
final public class ParseStatement {
    final private static Logger log = Logger.getLogger(ParseStatement.class);

    /**
     * Parse Statements until end of block or file.
     */
    public static void parseMultiple(ModuleState state, ASTNode parent) {
        log.trace("parseMultiple "+state.tokens.get());
        var tokens = state.tokens;

        /* Parse Statements until end of block|file */
        while(!tokens.eof() && tokens.kind() != Token.Kind.RCURLY) {

            /* Handle 'export' ['default'] */
            if(tokens.isKeyword("export")) {

                new ExportStmt().parse(state, parent);

            } else {

                /* Handle 'declare' keyword */
                boolean isAmbient = tokens.isKeyword("declare");
                if(isAmbient) {
                    tokens.next();
                }

                var stmt = doParse(state, parent);

                if(isAmbient) {
                    if(stmt == null || !(stmt instanceof Declaration)) {
                        state.addError("Expecting a declaration");
                    } else {
                        ((Declaration)stmt).isAmbient = true;
                    }
                }
            }

            /* comma */
            if(tokens.kind()== Token.Kind.COMMA) {
                tokens.next();

                /* Semicolon? */
            } else if(tokens.kind() == Token.Kind.SEMICOLON) {
                tokens.next();

            } else {
                // todo - should be eof or rcurly here
            }
        }
    }

    /**
     * Parse a single Statement and return it. Any closing semicolon is left to the caller.
     */
    public static Statement parseSingle(ModuleState state, ASTNode parent) {
        log.trace("parseSingle " + state.tokens.get());

        return doParse(state, parent);
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
            case "namespace":
                return new NamespaceDecl().parse(state, parent);


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
            case "while":
                return new WhileStmt().parse(state, parent);
            case "do":
                return new DoWhileStmt().parse(state, parent);
            case "import":
                return new ImportStmt().parse(state, parent);
            case "export":
                return new ExportStmt().parse(state, parent);
            case "throw":
                return new ThrowStmt().parse(state, parent);
            case "try":
                return new TryCatchStmt().parse(state, parent);
        }

        /* Assume it's an Expression */
        ParseExpression.parse(state, parent);

        return null;
    }
}
