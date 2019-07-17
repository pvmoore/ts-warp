package warp.ast.stmt;

import warp.ModuleState;
import warp.ast.ASTNode;
import warp.lex.Token;
import warp.parse.ParseExpression;
import warp.parse.ParseStatement;
import warp.parse.ParseVariable;

/**
 * This is the basic _for_ statement.
 *
 *  'for' '(' [INIT_STMTS] ';' [COND] ';' [POST_LOOP_EXPRS] ')' Statement
 *
 *  ForStmt
 *      VariableDecl | MultiVariableDecl    (0  or 1)
 *      Expression                          (0 or 1)      (@ condIndex)
 *      Expression                          (0 or more)   (@ postLoopIndex)
 */
final public class ForStmt extends Statement {
    private int condIndex = -1;
    private int postLoopIndex = -1;

    @Override
    public String toString() {
        return "for";
    }

    @Override
    public Statement parse(ModuleState state, ASTNode parent) {
        log.trace("parse "+state.tokens.get());
        parent.add(this);
        var tokens = state.tokens;

        tokens.skip("for");
        tokens.skip(Token.Kind.LBR);

        /* Inits */
        if(tokens.kind() != Token.Kind.SEMICOLON) {

            ParseVariable.parse(state, this);
        }
        tokens.skip(Token.Kind.SEMICOLON);

        /* Condition */
        if(tokens.kind() != Token.Kind.SEMICOLON) {
            condIndex = this.children.size();
            ParseExpression.parse(state, this);
        }
        tokens.skip(Token.Kind.SEMICOLON);

        /* Post loop exprs */
        if(tokens.kind() != Token.Kind.RBR) {
            postLoopIndex = this.children.size();

            while(tokens.kind() != Token.Kind.RBR) {
                ParseExpression.parse(state, this);

                tokens.expect(Token.Kind.COMMA, Token.Kind.RBR);
                tokens.skipIf(Token.Kind.COMMA);
            }
        }
        tokens.skip(Token.Kind.RBR);

        /* Body */
        ParseStatement.parseSingle(state, this);

        return this;
    }
}
