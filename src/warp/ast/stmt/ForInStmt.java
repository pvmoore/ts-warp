package warp.ast.stmt;

import warp.ModuleState;
import warp.ast.ASTNode;
import warp.lex.Token;
import warp.parse.ParseExpression;
import warp.parse.ParseStatement;
import warp.parse.ParseVariable;

/**
 * 'for' '(' Variable 'in' Expression ')'
 *
 * ForInStmt
 *      VariableDecl
 *      Expression
 *      Statement
 */
final public class ForInStmt extends Statement {

    @Override
    public String toString() {
        return "for in";
    }

    @Override
    public ForInStmt parse(ModuleState state, ASTNode parent) {
        parent.add(this);
        var tokens = state.tokens;

        tokens.skip("for");
        tokens.skip(Token.Kind.LBR);

        ParseVariable.parse(state, this);

        tokens.skip("in");

        ParseExpression.parse(state, this);

        tokens.skip(Token.Kind.RBR);

        ParseStatement.parseSingle(state, this);

        return this;
    }
}
