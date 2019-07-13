package warp.ast.stmt;

import warp.ModuleState;
import warp.ast.ASTNode;
import warp.ast.Statement;
import warp.lex.Token;
import warp.parse.ParseExpression;

/**
 * 'return' [Expression] [';']
 */
final public class ReturnStmt extends Statement {

    @Override public String toString() {
        return "return";
    }

    @Override public ReturnStmt parse(ModuleState state, ASTNode parent) {
        parent.add(this);
        var tokens = state.tokens;

        tokens.skip("return");

        var following = tokens.kind();

        if(following== Token.Kind.SEMICOLON || following == Token.Kind.RCURLY) {

        } else {
            ParseExpression.parse(state, this);
        }

        return this;
    }
}
