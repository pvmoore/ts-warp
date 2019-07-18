package warp.ast.stmt;

import warp.ModuleState;
import warp.ast.ASTNode;
import warp.lex.Token;
import warp.parse.ParseStatement;
import warp.parse.ParseVariable;

/**
 * TryCatchStmt
 *      BlockStmt     (try block)
 *      VariableDecl  (optional error variable)
 *      BlockStmt     (catch block)
 *      BlockStmt     (optional finally block)
 */
final public class TryCatchStmt extends Statement {
    public boolean hasFinally;
    public boolean hasErrorVariable;

    public BlockStmt getTryBlock() {
        return (BlockStmt)firstChild();
    }
    public BlockStmt getCatchBlock() {
        return (BlockStmt)children.get(hasErrorVariable ? 2 : 1);
    }
    public BlockStmt getFinallyBlock() {
        if(!hasFinally) return null;
        return (BlockStmt)children.get(hasErrorVariable ? 3 : 2);
    }

    @Override
    public String toString() {
        return "try catch" + (hasFinally ? " finally" : "");
    }
    /**
     * 'try' BlockStmt 'catch' [ '(' identifier ')' ] BlockStmt [ 'finally' BlockStmt ]
     */
    @Override
    public TryCatchStmt parse(ModuleState state, ASTNode parent) {
        parent.add(this);
        var tokens = state.tokens;

        tokens.skip("try");

        /* try block */
        ParseStatement.parseSingle(state, this);

        tokens.skip("catch");

        if(tokens.kind() == Token.Kind.LBR) {
            tokens.next();
            this.hasErrorVariable = true;

            ParseVariable.parse(state, this);

            tokens.skip(Token.Kind.RBR);
        }
        /* catch block */
        ParseStatement.parseSingle(state, this);

        /* Optional finally block */
        if(tokens.isKeyword("finally")) {
            tokens.next();
            ParseStatement.parseSingle(state, this);
            this.hasFinally = true;
        }

        return this;
    }
}
