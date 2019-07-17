package warp.ast.stmt;

import warp.ModuleState;
import warp.ast.ASTNode;
import warp.lex.Token;
import warp.parse.ParseExpression;
import warp.parse.ParseStatement;

/**
 * 'while' '(' Expression ') Statement
 *
 * WhileStmt
 *      Expression (condition)
 *      Statement
 */
final public class WhileStmt extends Statement {

    @Override public String toString() {
        return "while";
    }

    @Override
    public Statement parse(ModuleState state, ASTNode parent) {
        parent.add(this);
        var tokens = state.tokens;

        tokens.skip("while");
        tokens.skip(Token.Kind.LBR);
        ParseExpression.parse(state,this);
        tokens.skip(Token.Kind.RBR);

        ParseStatement.parseSingle(state, this);

        return this;
    }
}
