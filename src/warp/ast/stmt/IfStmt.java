package warp.ast.stmt;

import warp.ModuleState;
import warp.ast.ASTNode;
import warp.ast.expr.Expression;
import warp.lex.Token;
import warp.parse.ParseExpression;
import warp.parse.ParseStatement;

/**
 * IfStmt
 *      Expression  (condition)
 *      Statement   (then)
 *      Statement   (optional else)
 */
final public class IfStmt extends Statement {

    @Override public String toString() {
        return "if";
    }

    public Expression getCondition() {
        return (Expression)firstChild();
    }
    public Statement getThenStmt() {
        return (Statement)children.get(1);
    }
    public Statement getElseStmt() {
        if(children.size()<3) return null;
        return (Statement)children.get(2);
    }

    /**
     * 'if' '(' Expression ')' Statement [ 'else' Statement ]
     */
    @Override
    public Statement parse(ModuleState state, ASTNode parent) {
        parent.add(this);
        var tokens = state.tokens;

        tokens.skip("if");

        /* condition */
        tokens.skip(Token.Kind.LBR);
        ParseExpression.parse(state, this);
        tokens.skip(Token.Kind.RBR);

        /* then */
        ParseStatement.parseSingle(state, this);

        /* optional else */
        if(tokens.isKeyword("else")) {
            tokens.skip("else");
            ParseStatement.parseSingle(state, this);
        }

        return this;
    }
}
