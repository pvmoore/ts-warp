package warp.ast.stmt;

import warp.ModuleState;
import warp.ast.ASTNode;
import warp.lex.Token;
import warp.parse.ParseExpression;
import warp.parse.ParseStatement;
import warp.parse.ParseVariable;

/**
 * 'for' '(' VariableDecl 'of' Expression ')'
 *
 * ForOfStmt
 *      VariableDecl
 *      Expression
 *      Statement
 */
final public class ForOfStmt extends Statement {

    @Override
    public String toString() {
        return "for of";
    }

    @Override
    public Statement parse(ModuleState state, ASTNode parent) {
        parent.add(this);
        var tokens = state.tokens;

        tokens.skip("for");
        tokens.skip(Token.Kind.LBR);

        ParseVariable.parse(state, this);

        tokens.skip("of");

        ParseExpression.parse(state, this);

        tokens.skip(Token.Kind.RBR);

        ParseStatement.parseSingle(state, this);

        return this;
    }
}
