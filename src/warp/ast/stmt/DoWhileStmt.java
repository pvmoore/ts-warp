package warp.ast.stmt;

import warp.ModuleState;
import warp.ast.ASTNode;
import warp.lex.Token;
import warp.parse.ParseExpression;
import warp.parse.ParseStatement;

/**
 * 'do' Statement 'while' '(' Expression ')'
 */
final public class DoWhileStmt extends Statement {

    @Override public String toString() {
        return "do while";
    }

    @Override
    public Statement parse(ModuleState state, ASTNode parent) {
        parent.add(this);
        var tokens = state.tokens;

        tokens.skip("do");

        ParseStatement.parseSingle(state, this);

        tokens.skip("while");
        tokens.skip(Token.Kind.LBR);
        ParseExpression.parse(state, this);
        tokens.skip(Token.Kind.RBR);

        return this;
    }
}
