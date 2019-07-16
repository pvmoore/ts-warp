package warp.ast.stmt;

import warp.ModuleState;
import warp.ast.ASTNode;
import warp.ast.expr.Expression;
import warp.lex.Token;
import warp.parse.ParseExpression;
import warp.parse.ParseStatement;

import java.util.ArrayList;
import java.util.List;

/**
 * SwitchStmt
 *      Expression condition  (always 1 of these as first child)
 *
 *      Expression case expr    \
 *      [Statement case stmt]   /  0 or more. Statement may not be present for a fall-through
 *
 *      Statement default stmt (0 or 1 of these - may be anywhere after condition)
 */
final public class SwitchStmt extends Statement {
    public int indexOfDefault = -1; /* -1 means there is no default */

    public List<Integer> caseIndexes = new ArrayList<>();

    @Override public String toString() {
        return "switch";
    }

    public List<Expression> getCaseExprs() {
        return null;
    }
    public List<Statement> getCaseStmts() {
        return null;
    }
    public Statement getDefault() {
        return null;
    }

    @Override
    public Statement parse(ModuleState state, ASTNode parent) {
        parent.add(this);
        var tokens = state.tokens;

        tokens.skip("switch");
        tokens.skip(Token.Kind.LBR);
        ParseExpression.parse(state, this);
        tokens.skip(Token.Kind.RBR);

        tokens.skip(Token.Kind.LCURLY);

        while(tokens.kind() != Token.Kind.RCURLY) {
            if(tokens.isKeyword("default")) {
                tokens.skip("default");
                tokens.skip(Token.Kind.COLON);

                indexOfDefault = this.children.size();

                if(!tokens.isKeyword("case") && !tokens.isKeyword("default")) {
                    ParseStatement.parseSingle(state, this);
                }
            } else if(tokens.isKeyword("case")) {
                tokens.skip("case");

                caseIndexes.add(this.children.size());

                ParseExpression.parse(state, this);
                tokens.skip(Token.Kind.COLON);

                if(!tokens.isKeyword("case") && !tokens.isKeyword("default")) {
                    ParseStatement.parseSingle(state, this);
                }

            } else {
                state.addError("Expecting 'case' or 'default'");
            }
        }

        tokens.skip(Token.Kind.RCURLY);

        return this;
    }
}
